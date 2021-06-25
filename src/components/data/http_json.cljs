(ns components.data.http-json
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [components.data.insertions :as i]))


;; Intention
;; this interface reads data from an api call, and 
;; then stores it into a rules session.


; This function does the asynchronous call to http/get in order to
; read the json data from the browser and make it avaliable as edn.
; It uses a go block to handle the async, and calls insert facts
; on the response


(defn insert-facts-from-path [*session store-func! path root]
  (go (let [response (<! (http/get path))]
        (i/insert-facts *session store-func! (get-in response [:body root])))))

(defn read-cards [*session root path]
  (insert-facts-from-path *session
                          i/insert-cards
                          path
                          root))
