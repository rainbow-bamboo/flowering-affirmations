(ns components.data.insertions
  (:require [odoyle.rules :as o]
            [components.rules.events :as ev]))

(defn insert-cards [*session prompt]
  (swap! *session
         (fn [session]
           (let [{:keys [type content]} prompt]
             (-> session
                 (o/insert ::ev/card {::ev/insertion {:type type
                                                      :content content}})
                 o/fire-rules)))))
; This is the fucntion that allows us to read a collection of facts
; into a rule session. I'm following the odoyle convention of having the
; *session be the first parameter, and choosing to have the facts be the last.
; This way it reads as, create-facts! in X session with Y configuration and Z facts.
; Note the the use of do which allows us to group the two calls
; to execute when there are facts to add. 
(defn insert-facts [*session insert-fn facts]
  (if (seq facts)
    (do
      (insert-fn *session (first facts))
      (insert-facts *session
                    insert-fn
                    (rest facts)))
    true))
