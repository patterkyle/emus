(ns emus.tuning)

(def just-intervals
  [1 (/ 16 15) (/ 9 8) (/ 6 5) (/ 5 4) (/ 4 3) (/ 65 45)
   (/ 3 2) (/ 8 5) (/ 5 3) (/ 16 9) (/ 15 8) 2])

(def pythagorean-intervals
  [1 (/ 256 243) (/ 9 8) (/ 32 27) (/ 81 64)
   (/ 4 3) (/ 729 512) (/ 3 2) (/ 128 81)
   (/ 27 16) (/ 16 9) (/ 243 128) 2]);

(def quarter-comma-intervals
  [1 1.044907 1.118035 1.196279 (/ 5 4) 1.337481 1.397542
   1.495349 (/ 25 16) 1.671851 1.788854 1.869186 2]);

(def equal-intervals (mapv #(js/Math.pow 2 (/ % 12)) (range 13)))

(defn get-intervals [temperament]
  (case temperament
    :just just-intervals
    :pythagorean pythagorean-intervals
    :quarter-comma quarter-comma-intervals
    :equal equal-intervals))

(defn frequency [root-freq half-steps intervals]
  (let [pitch-class (mod (+ (mod half-steps 12) 12) 12)
        interval (nth intervals pitch-class)
        octave (js/Math.floor (/ half-steps 12))]
    (* root-freq interval (js/Math.pow 2 octave))))

(defn- half-steps-from-d4 [{:keys [pitch-class octave]}]
  (+ (- pitch-class 2) (* (- octave 4) 12)))

(defn note-frequency [note a4-freq temperament]
  (let [intervals (get-intervals temperament)
        d4-freq (* a4-freq (/ 1 (nth intervals 7)))
        half-steps (half-steps-from-d4 note)]
    (frequency d4-freq half-steps intervals)))
