(ns emus.cofx
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-cofx
 :synth/current-synth
 (fn [cofx _]
   (let [synths (re-frame/subscribe [:synth/synths])
         synth-index (re-frame/subscribe [:synth/index])]
     (assoc cofx :synth/current-synth
            (nth @synths @synth-index)))))
