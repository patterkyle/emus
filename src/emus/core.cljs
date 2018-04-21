(ns emus.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [emus.views.main :as views]
            [emus.events.main]
            [emus.subs]
            [emus.synth]))

(defn ^:dev/after-load start []
  (re-frame/dispatch-sync [:initialize-db])
  (reagent/render [views/main-panel] (js/document.getElementById "root")))

(defn ^:export main []
  (start))
