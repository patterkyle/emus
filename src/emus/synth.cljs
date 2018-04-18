(ns emus.synth
  (:require [emus.tuning :as tuning]
            ["tone" :as tone]))

(defn make-synths [synth-count]
  (mapv #(.toMaster (tone/Synth.)) (range synth-count)))

(defn play-note [synth note a4-freq tuning]
  (let [freq (tuning/note-frequency note a4-freq tuning)]
    (.triggerAttack synth freq)))

(defn stop-note [synth note a4-freq tuning]
  (let [freq (tuning/note-frequency note a4-freq tuning)]
    (.triggerRelease synth freq)))

(defn stop [synth]
  (.triggerRelease synth))
