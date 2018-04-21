(ns emus.views.keyboard
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            ["qwerty-hancock" :as qwerty-hancock]))

(defn stop-button []
  [:button {:on-click #(re-frame/dispatch [:keyboard/stop-button-click])}
   "Stop"])

(defn sustain-checkbox [checked?]
  [:div {:class "sustain-checkbox"}
   [:label "Sustain: "]
   [:input {:type "checkbox"
            :checked checked?
            :on-change #(re-frame/dispatch [:keyboard/sustain-change])}]])

(defn tuning-select []
  [:div {:class "tuning-select"}
   [:label "Temperament: "]])

(defn keyboard-inner [keyboard-id width height start-note octaves]
  (reagent/create-class
   {:reagent-render (fn [] [:div {:id "qwerty-hancock"}])
    :component-did-mount
    (fn [_]
      (let [qh (new qwerty-hancock/QwertyHancock
                    #js {:id keyboard-id
                         :width width
                         :height height
                         :startNote start-note
                         :octaves octaves})]
        (set! (.-keyDown qh) #(re-frame/dispatch [:keyboard/keydown %]))
        (set! (.-keyUp qh) #(re-frame/dispatch [:keyboard/keyup %]))))}))

(defn keyboard []
  (let [sustain? (re-frame/subscribe [:synth/sustain?])
        keyboard-id "qwerty-hancock"
        width 600
        height 150
        start-note "C4"
        octaves 3]
    [:div {:id "keyboard"}
     [keyboard-inner keyboard-id width height start-note octaves]
     [sustain-checkbox @sustain?]
     [stop-button]]))
