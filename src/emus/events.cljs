(ns emus.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [emus.db :as db]
            [emus.synth :as synth]))

(re-frame/reg-event-db
 :initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-fx
 :synth/play-note
 [(re-frame/inject-cofx :synth/current-synth)]
 (fn-traced [{:keys [db] :as cofx} [_ note]]
   {:db
    (assoc db
           :synth/index (mod (inc (:synth/index db)) (:synth/count db))
           :synth/notes (conj (:synth/notes db)
                              (assoc note :synth (:synth/current-synth cofx))))
    :synth/play-note! {:synth (:synth/current-synth cofx)
                       :note note
                       :a4-freq (:synth/a4-freq db)
                       :tuning (:synth/tuning db)}}))

(defn note-equal? [note-a note-b]
  (and (= (:pitch-class note-a) (:pitch-class note-b))
       (= (:octave note-a) (:octave note-b))))

(defn get-synth [notes note]
  (:synth (first (filter (partial note-equal? note) notes))))

(re-frame/reg-event-fx
 :synth/stop-note
 (fn-traced [{:keys [db]} [_ note]]
   (let [synth (get-synth (:synth/notes db) note)]
     {:db (assoc db :synth/notes
                 (into #{}
                       (remove (partial note-equal? note) (:synth/notes db))))
      :synth/stop-note! {:synth synth
                         :note note
                         :a4-freq (:synth/a4-freq db)
                         :tuning (:synth/tuning db)}})))

(re-frame/reg-event-fx
 :synth/stop-all
 (fn-traced [{:keys [db]} [_]]
   {:db (assoc db :synth/notes #{})
    :synth/stop-all! (:synth/synths db)}))

(re-frame/reg-fx
 :synth/play-note!
 (fn [{:keys [synth note a4-freq tuning]}]
   (synth/play-note synth note a4-freq tuning)))

(re-frame/reg-fx
 :synth/stop-note!
 (fn [{:keys [synth note a4-freq tuning]}]
   (synth/stop-note synth note a4-freq tuning)))

(re-frame/reg-fx
 :synth/stop-all!
 (fn [synths]
   (doseq [synth synths]
     (synth/stop synth))))
