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
       [:a#rainbow {:href "https://rainbowbamboo.com"} "🌈"]
       (static-garden *session)
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
        [:button.hide-affirmation-button
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

    static-garden
    [:what
     [::c/derived ::c/affirmations all-affirmations]
     [::e/global ::e/show-affirmation? show-affirmation?]
     [::c/derived ::c/selected-affirmations selected-affirmations]
     :then
     (let [*session (orum/prop)
           valid-selection? (seq selected-affirmations)]
       [:div.affirmation-screen
        {:class (if show-affirmation? "hidden" nil)}
        [:h1 "You're Flowering"]
        #_[:button.show-affirmation-button
         {:disabled (if valid-selection? false true)
          :class (if valid-selection? "valid-selection" nil)
          :on-click #(insert! *session ::e/global {::e/show-affirmation? true})}
         (if valid-selection? "Reveal your affirmation" "Pick some")]
        [:div.affirmations
         (map (fn [a]
                (let [{:keys [hue saturation lightness]} (:color a)]
                  [:span.flower
                   {:key (str "flower-" (:id a))
                    :style {:color (str "hsl(" hue "," saturation "%," lightness "%)")}
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
