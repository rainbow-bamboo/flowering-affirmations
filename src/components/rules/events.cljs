(ns components.rules.events
  (:require [odoyle.rules :as o]
            [components.rules.cards :as c]))


 

;; The event rules are reponsible for the next-id counter
;; because insertions will go through this namespace

(def event-rules
  (o/ruleset
   {::new-card
    [:what
     [::insertion ::card prompt]
     [::global ::next-id id {:then false}]
     :then
     (let [{:keys [type content]} prompt]
       (o/insert! id {:prompt/type type
                      :prompt/content content})
       (o/insert! ::global ::next-id (+ 1 id)))]}))