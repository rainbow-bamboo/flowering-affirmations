(ns components.rules.renders
  (:require [odoyle.rules :as o]
            [odoyle.rum :as orum]
            [components.rules.cards :as c]))

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
       [:h1 "Rainbow Affirmations"]
       (all-affirmations *session)]])]
    
    all-affirmations
    [:what
     [::c/derived ::c/affirmations all-affirmations]
     :then
     (let [*session (orum/prop)]
       (println all-affirmations)
       [:ul.affirmations
        (map (fn [a] [:li (:content a)]) all-affirmations)])]}))