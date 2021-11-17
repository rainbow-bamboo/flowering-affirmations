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
       (all-affirmations *session)
       (selected-affirmation *session)]])]
    
    selected-affirmation
    [:what
     [::c/derived ::c/selected-affirmations selected-affirmations]
     [::e/global ::e/show-affirmation? show?]
     :then
     (let [*session (orum/prop)
           selected-flower (if (seq selected-affirmations) (rand-nth selected-affirmations) nil)
           other-flowers (filter #(not (= (:content %) (:content selected-flower))) selected-affirmations)]
       [:div.card
        {:class (if (not show?) "hidden" nil)}
        [:h2.hide-affirmation-button
         {:on-click #(insert! *session ::e/global {::e/hide-affirmation true})}
         "Back to the garden"]

        (let [{:keys [hue saturation lightness]} (:color selected-flower)]
          [:div.flower.chosen
           {:style {:color (str "hsl(" hue "," saturation "%," lightness "%)")}}
           (:flower selected-flower)])
        [:div.affirmation (:content selected-flower)]
        [:div.other-flowers
         (map (fn [f]
                (let [{:keys [hue saturation lightness]} (:color f)]
                  [:span.flower
                   {:style {:color (str "hsl(" hue "," saturation "%," lightness "%)")}}
                   (:flower f)]))
              other-flowers)]
        #_(rainbow-homepage *session)])]
    
    all-affirmations
    [:what
     [::c/derived ::c/affirmations all-affirmations]
     [::e/global ::e/show-affirmation? show-affirmation?]
     :then
     (let [*session (orum/prop)]
       [:div.affirmation-screen
        {:class (if show-affirmation? "hidden" nil)}
        [:h1 "You're Flowering"]
        [:h2.show-affirmation-button
         {:on-click #(insert! *session ::e/global {::e/show-affirmation? true})}
         "Pick some and then click here to reveal your affirmation"]
        [:div.affirmations
         (map (fn [a]
                (let [{:keys [hue saturation lightness]} (:color a)]
                  [:span.flower
                   {:style {:color (str "hsl(" hue "," saturation "%," lightness "%)")}
                    :class (if (:selected? a) "selected" nil)
                    :on-click #(insert! *session ::e/flower {::e/pick (:id a)})}
                   (:flower a)])) all-affirmations)]
        #_(rainbow-homepage *session)])]
    
    rainbow-homepage
    [:then
     (let [*session (orum/prop)]
       [:a.rainbow-homepage {:href "http://rainbowbamboo.org"}
        [:img {:src "/img/rainbow-logo.png"
               :alt "rainbow bamboo homepage"}]])]}))




