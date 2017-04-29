(ns onboarding.prompts.services.moving
  (:require [ant-ui.core :as a]
            [onboarding.prompts.content :as content]
            [re-frame.core :refer [dispatch subscribe]]
            [cljsjs.moment]
            [reagent.core :as r]))

(def time-picker (r/adapt-react-class (.-TimePicker js/window.antd)))

(defn- form
  [keypath commencement {:keys [needed date time]}]
  (let [commencement (js/moment. commencement)]
    [a/card
     [:div.field
      [:label.label "Do you need help moving in?"]
      [:p.control
       [a/radio-group
        {:on-change #(dispatch [:prompt/update keypath :needed (= (.. % -target -value) "yes")])
         :value     (cond (true? needed) "yes" (false? needed) "no" :otherwise nil)}
        [a/radio {:value "yes"} "Yes"]
        [a/radio {:value "no"} "No"]]]]
     (when needed
       [:div
        [:div.field
         [:label.label "What date will you be moving in on?"]
         [:p.control
          [a/date-picker
           {:value         (js/moment. date)
            :on-change     #(dispatch [:prompt/update keypath :date (.toDate %)])
            :disabled-date #(.isBefore % commencement)
            :allow-clear   false
            :format        "MM-DD-YYYY"}]]]
        [:div.field
         [:label.label "At what time will you be moving in?"]
         [:p.control
          [time-picker
           {:value                 (when time (js/moment. time))
            :on-change             #(dispatch [:prompt/update keypath :time (.toDate %)])
            :format                "HH:mm"
            :disabled-hours        #(concat (range 0 9) (range 20 24))
            :disabled-minutes      (fn [] (remove #(= (mod % 30) 0) (range 0 61)))
            :disabled-seconds      #(range 0 61)
            :hide-disabled-options true}]]]])]))

(defmethod content/content :services/moving
  [{:keys [keypath commencement data] :as item}]
  [:div.content
   [:p "Starcity provides moving services to assist you with the lugging and lifting at Starcity on move-in day. " [:strong "Moving services are $50 per hour with a 2 hour minimum."]]
   [form keypath commencement data]])
