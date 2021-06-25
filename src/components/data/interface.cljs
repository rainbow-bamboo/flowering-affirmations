(ns components.data.interface
  (:require [components.data.http-json :as h]
            [components.data.insertions :as i]))

(defn read-cards [*session root path]
  (h/read-cards *session root path))
