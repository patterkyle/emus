(ns emus.keyboard
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            ["qwerty-hancock" :as qwerty-hancock]))

(defn keyboard-inner []
  (reagent/create-class
   {:reagent-render (fn [] [:div
                            [:div {:id "qwerty-hancock"}]
                            [:button {:on-click
                                      #(re-frame/dispatch
                                        [:keyboard/stop-button-click])}
                             "Stop"]])
    :component-did-mount
    (fn [_]
      (let [qh (new qwerty-hancock/QwertyHancock
                    #js {:id "qwerty-hancock"
                         :width 600
                         :height 150
                         :startNote "C4"
                         :octaves 3})]
        (set! (.-keyDown qh) #(re-frame/dispatch [:keyboard/keydown %]))
        (set! (.-keyUp qh) #(re-frame/dispatch [:keyboard/keyup %]))))}))

(defn keyboard []
  [:div {:id "keyboard"}
   [keyboard-inner]])
