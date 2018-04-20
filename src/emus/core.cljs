(ns emus.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [emus.events :as events]
            [emus.subs :as subs]
            [emus.cofx :as cofx]
            [emus.synth :as synth]
            [emus.keyboard :as keyboard]
            [emus.views :as views]))

(defn ^:dev/after-load start []
  (re-frame/dispatch-sync [:initialize-db])
  (reagent/render [views/main-panel] (js/document.getElementById "root")))

(defn ^:export main []
  (start))
