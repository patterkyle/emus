(ns emus.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [emus.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn-traced [_ _]
   db/default-db))
