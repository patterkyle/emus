(ns emus.db
  (:require [emus.synth :as synth]))

(def synth-count 4)

(def default-db
  {:app-name "emus"

   :synth/a4-freq 440
   :synth/temperament :equal
   :synth/oscillator :triangle
   :synth/notes #{}
   :synth/count synth-count
   :synth/synths (synth/make-synths synth-count)
   :synth/index 0
   :synth/sustain? true})
