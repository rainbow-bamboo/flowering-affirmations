(ns components.rules.cards
  (:require [odoyle.rules :as o]
            [tick.alpha.api :as t]
            [odoyle.rum :as orum]))


(def card-rules
  (o/ruleset
   {::affirmations
   [:what
    [id :prompt/type "affirmation"]
    [id :prompt/content content]
    :then-finally
    (->> (o/query-all o/*session* ::affirmations)
         (o/insert! ::derived ::affirmations))]}))