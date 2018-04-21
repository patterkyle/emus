(ns emus.keyboard
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            ["qwerty-hancock" :as qwerty-hancock]))

(defn stop-button []
  [:button {:on-click #(re-frame/dispatch [:keyboard/stop-button-click])}
   "Stop"])

(defn sustain-checkbox []
  [:div {:class "sustain-checkbox"}
   [:label "Sustain: "]
   [:input {:type "checkbox"
            :on-change #(re-frame/dispatch [:keyboard/sustain-change])}]])

(defn keyboard-inner []
  (reagent/create-class
   {:reagent-render (fn [] [:div {:id "qwerty-hancock"}])
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
   [keyboard-inner]
   [sustain-checkbox]
   [stop-button]])
