(ns components.rules.renders
  (:require [odoyle.rules :as o]
            [odoyle.rum :as orum]
            [components.rules.cards :as c]
            [components.rules.events :as e]))

(defn insert! [*session id attr->value]
  (swap! *session
         (fn [session]
           (-> session
               (o/insert id attr->value)
               o/fire-rules))))

(def render-rules
  (orum/ruleset
   {app-root
    [:then
    (let [*session (orum/prop)]
     [:div#app-root
      [:header
       [:h1 "You're Flowering"]
       (all-affirmations *session)]])]
    
    all-affirmations
    [:what
     [::c/derived ::c/affirmations all-affirmations]
     :then
     (let [*session (orum/prop)]
       [:div.affirmations
        (map (fn [a] 
               (let [{:keys [hue saturation lightness]} (:color a)]
                 [:span.flower
                  {:style {:color (str "hsl(" hue "," saturation "%," lightness "%)")}
                   :class (if (:selected? a) "selected" nil)
                   :on-click #(insert! *session ::e/flower {::e/pick (:id a)})}
                  (:flower a)])) all-affirmations)])]}))