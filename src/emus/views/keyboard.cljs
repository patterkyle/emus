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

(defn a4-freq-input [a4-freq]
  [:div {:class "a4-input"}
   [:label "A4 Frequency: "]
   [:input {:type "number"
            :min 220
            :max 880
            :value a4-freq
            :on-change #(re-frame/dispatch
                         [:synth/a4-freq-change
                          (-> % .-target .-value js/parseInt)])}]])

(defn temperament-select [temperament]
  [:div {:class "temperament-select"}
   [:label "Temperament: "]
   [:select {:value temperament
             :on-change #(re-frame/dispatch
                          [:synth/temperament-change
                           (-> % .-target .-value keyword)])}
    [:option {:value "just"} "just"]
    [:option {:value "pythagorean"} "pythagorean"]
    [:option {:value "quarter-comma"} "quarter-comma"]
    [:option {:value "equal"} "equal"]]])

(defn oscillator-select [oscillator]
  [:div {:class "oscillator-select"}
   [:label "Oscillator: "]
   [:select {:value oscillator
             :on-change #(re-frame/dispatch
                          [:synth/oscillator-change
                           (-> % .-target .-value keyword)])}
    [:option {:value "sine"} "sine"]
    [:option {:value "triangle"} "triangle"]
    [:option {:value "sawtooth"} "sawtooth"]
    [:option {:value "square"} "square"]]])

(defn qwerty-hancock []
  (let [keyboard-id "qwerty-hancock"
        width 600
        height 150
        start-note "C4"
        octaves 3]
    (reagent/create-class
     {:reagent-render (fn [] [:div {:id keyboard-id}])
      :component-did-mount
      (fn [_]
        (let [qh (new qwerty-hancock/QwertyHancock
                      #js {:id keyboard-id
                           :width width
                           :height height
                           :startNote start-note
                           :octaves octaves})]
          (set! (.-keyDown qh) #(re-frame/dispatch [:keyboard/keydown %]))
          (set! (.-keyUp qh) #(re-frame/dispatch [:keyboard/keyup %]))))})))

(defn keyboard []
  (let [sustain? (re-frame/subscribe [:synth/sustain?])
        a4-freq (re-frame/subscribe [:synth/a4-freq])
        temperament (re-frame/subscribe [:synth/temperament])
        oscillator (re-frame/subscribe [:synth/oscillator])]
    [:div {:id "keyboard"}
     [qwerty-hancock]
     [sustain-checkbox @sustain?]
     [a4-freq-input @a4-freq]
     [temperament-select @temperament]
     [oscillator-select @oscillator]
     [stop-button]]))
