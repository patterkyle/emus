(ns emus.synth
  (:require [emus.tuning :as tuning]
            ["tone" :as tone]))

(defn make-synth [] (.toMaster (tone/Synth.)))

(defn make-synths [synth-count]
  (mapv make-synth (range synth-count)))

(defn play-note [synth note a4-freq tuning]
  (let [freq (tuning/note-frequency note a4-freq tuning)]
    (.triggerAttack synth freq)))

(defn stop [synth]
  (.triggerRelease synth))

(defn retune [synth note a4-freq tuning]
  (let [new-freq (tuning/note-frequency note a4-freq tuning)]
    (.setValueAtTime (.-frequency synth) new-freq)))
