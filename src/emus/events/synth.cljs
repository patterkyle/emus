(ns emus.events.synth
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [emus.synth :as synth]))

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
                       :temperament (:synth/temperament db)}}))

(defn note-equal? [note-a note-b]
  (and (= (:pitch-class note-a) (:pitch-class note-b))
       (= (:octave note-a) (:octave note-b))))

(defn get-synth [notes note]
  (-> (filter (partial note-equal? note) notes) first :synth))

(re-frame/reg-event-fx
 :synth/stop-note
 (fn-traced [{:keys [db]} [_ note]]
   (let [synth (get-synth (:synth/notes db) note)]
     {:db (assoc db :synth/notes
                 (into #{}
                       (remove (partial note-equal? note) (:synth/notes db))))
      :synth/stop-synth! synth})))

(re-frame/reg-event-fx
 :synth/stop-all
 (fn-traced [{:keys [db]} [_]]
   {:db (assoc db :synth/notes #{})
    :synth/stop-all! (:synth/synths db)}))

(re-frame/reg-event-fx
 :synth/retune
 (fn-traced [{:keys [db]} [_ a4-freq temperament]]
   {:db db
    :synth/retune! {:notes (:synth/notes db)
                    :a4-freq a4-freq
                    :temperament temperament}}))

(re-frame/reg-event-fx
 :synth/a4-freq-change
 (fn-traced [{:keys [db]} [_ new-a4-freq]]
   {:db (assoc db :synth/a4-freq new-a4-freq)
    :dispatch [:synth/retune new-a4-freq (:synth/temperament db)]}))

(re-frame/reg-event-fx
 :synth/temperament-change
 (fn-traced [{:keys [db]} [_ new-temperament]]
   {:db (assoc db :synth/temperament new-temperament)
    :dispatch [:synth/retune (:synth/a4-freq db) new-temperament]}))

(re-frame/reg-event-fx
 :synth/oscillator-change
 (fn-traced [{:keys [db]} [_ new-oscillator]]
   {:db (assoc db :synth/oscillator new-oscillator)
    :synth/set-oscillator! {:synths (:synth/synths db)
                            :oscillator new-oscillator}}))

;; effects

(re-frame/reg-fx
 :synth/play-note!
 (fn [{:keys [synth note a4-freq temperament]}]
   (synth/play-note synth note {:a4-freq a4-freq :temperament temperament})))

(re-frame/reg-fx
 :synth/stop-synth!
 (fn [synth]
   (synth/stop synth)))

(re-frame/reg-fx
 :synth/stop-all!
 (fn [synths]
   (doseq [synth synths]
     (synth/stop synth))))

(re-frame/reg-fx
 :synth/retune!
 (fn [{:keys [notes a4-freq temperament]}]
   (doseq [note notes]
     (synth/retune (:synth note) note a4-freq temperament))))

(re-frame/reg-fx
 :synth/set-oscillator!
 (fn [{:keys [synths oscillator]}]
   (doseq [synth synths]
     (synth/set-oscillator synth oscillator))))
