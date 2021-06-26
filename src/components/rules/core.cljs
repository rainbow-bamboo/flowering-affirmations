(ns components.rules.core
  (:require [odoyle.rules :as o]
            [components.rules.renders :as r]
            [components.rules.events :as ev]
            [components.rules.cards :as c]))

(def initial-session
  (-> (reduce o/add-rule (o/->session) (concat r/render-rules c/card-rules ev/event-rules))
      (o/insert ::ev/global {::ev/next-id 0})
      (o/insert ::c/derived {::c/affirmations []
                             ::c/selected-affirmations []})
      o/fire-rules))

(def *card-session (atom initial-session))