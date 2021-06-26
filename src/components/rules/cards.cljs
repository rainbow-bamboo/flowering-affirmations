(ns components.rules.cards
  (:require [odoyle.rules :as o]
            [tick.alpha.api :as t]
            [odoyle.rum :as orum]))


(def card-rules
  (o/ruleset
   {::affirmations
   [:what
    [id :card/type "affirmation"]
    [id :card/content content]
    [id :card/color color]
    [id :card/selected? selected?]
    [id :card/flower flower]
    :then-finally
    (->> (o/query-all o/*session* ::affirmations)
         shuffle
         (o/insert! ::derived ::affirmations))]}))
