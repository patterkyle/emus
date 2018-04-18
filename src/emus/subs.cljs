(ns emus.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :app-name
 (fn [db]
   (:app-name db)))

(re-frame/reg-sub
 :synth/tuning
 (fn [db]
   (:synth/tuning db)))

(re-frame/reg-sub
 :synth/a4-freq
 (fn [db]
   (:synth/a4-freq db)))

(re-frame/reg-sub
 :synth/notes
 (fn [db]
   (:synth/notes db)))

(re-frame/reg-sub
 :synth/synths
 (fn [db]
   (:synth/synths db)))

(re-frame/reg-sub
 :synth/index
 (fn [db]
   (:synth/index db)))
