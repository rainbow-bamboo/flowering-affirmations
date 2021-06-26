(ns components.rules.events
  (:require [odoyle.rules :as o]
            [components.rules.cards :as c]))


(defn rand-hsl []
  {:hue (Math/round(rand 360))
   :saturation (Math/round (rand 100))
   :lightness (Math/round (rand 100))})

(def valid-letters ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m"])


;; The event rules are reponsible for the next-id counter
;; because insertions will go through this namespace

(def event-rules
  (o/ruleset
   {::new-card
    [:what
     [::card ::insertion prompt]
     [::global ::next-id id {:then false}]
     :then
     (let [{:keys [type content]} prompt]
       (o/insert! id {:card/type type
                      :card/content content
                      :card/color (rand-hsl)
                      :card/selected? false
                      :card/flower (rand-nth valid-letters)})
       (o/insert! ::global ::next-id (+ 1 id)))]
    
    ::pick-flower
    [:what
     [::flower ::pick flower-id]
     [flower-id :card/content content]
     [flower-id :card/flower flower]
     [flower-id :card/color color]
     [::c/derived ::c/selected-affirmations selected-affirmations]
     :then
     (o/insert! flower-id {:card/selected? true})
     (o/insert! ::c/selected {::c/affirmations (conj selected-affirmations {:content content
                                                                            :flower flower
                                                                            :color color})})]}))

