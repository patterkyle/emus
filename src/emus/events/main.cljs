(ns emus.events.main
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [emus.cofx]
            [emus.db :as db]
            [emus.events.synth]
            [emus.events.keyboard]))

(re-frame/reg-event-db
 :initialize-db
 (fn-traced [_ _]
   db/default-db))
