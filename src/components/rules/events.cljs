(ns components.rules.events
  (:require [odoyle.rules :as o]
            [components.rules.cards :as c]))

(defn rand-between [a b]
  (+ a (rand (- b a))))

(defn rand-hsl []
  {:hue (Math/round (rand 360))
   :saturation (Math/round (rand-between 10 95))
   :lightness (Math/round (rand-between 10 85))})


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
    
    ::close-affirmation-screen
    [:what
     [::global ::hide-affirmation true]
     :then
     (o/insert! ::global ::show-affirmation? false)
     (o/insert! ::c/derived ::c/selected-affirmations [])]

    ::pick-flower
    [:what
     [::flower ::pick flower-id]
     [flower-id :card/content content {:then false}]
     [flower-id :card/flower flower {:then false}]
     [flower-id :card/color color {:then false}]
     [::c/derived ::c/selected-affirmations selected-affirmations {:then false}]
     :then
     (o/insert! flower-id {:card/selected? true})
     (o/insert! ::c/derived {::c/selected-affirmations (conj selected-affirmations 
                                                             {:content content
                                                              :flower flower
                                                              :color color})})]}))

