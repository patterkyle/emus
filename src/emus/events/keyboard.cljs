(ns emus.events.keyboard
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn parse-note-name [note-name]
  (let [note-regex #"^([A-G]#?)(\d)$"
        [_ note-name' octave] (re-matches note-regex note-name)
        notes (zipmap ["C" "C#" "D" "D#" "E" "F" "F#" "G" "G#" "A" "A#" "B"]
                      (range 12))]
    {:pitch-class (notes note-name')
     :octave (js/parseInt octave)}))

(re-frame/reg-event-fx
 :keyboard/keydown
 (fn-traced [{:keys [db]} [_ note-name]]
   (let [note (parse-note-name note-name)]
     {:db db
      :dispatch [:synth/play-note note]})))

(re-frame/reg-event-fx
 :keyboard/keyup
 (fn-traced [{:keys [db]} [_ note-name]]
   (let [note (parse-note-name note-name)
         effect (if-not (:synth/sustain? db)
                  {:dispatch [:synth/stop-note note]})]
     (merge {:db db} effect))))

(re-frame/reg-event-fx
 :keyboard/stop-button-click
 (fn-traced [{:keys [db]} _]
   {:db db
    :dispatch [:synth/stop-all]}))

(re-frame/reg-event-fx
 :keyboard/sustain-change
 (fn-traced [{:keys [db]} _]
   {:db (update db :synth/sustain? not)
    :dispatch [:synth/stop-all]}))
