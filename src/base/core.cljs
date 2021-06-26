(ns base.core
  (:require [rum.core :as rum]
            [components.rules.renders :as r]
            [components.rules.interface :as cr]
            [components.data.interface :as data]))



(rum/defc app []
  (r/app-root cr/*card-session))


(defn ^:export main
  []
  (data/read-cards cr/*card-session :cards "/json/cards.json")
  (rum/hydrate (app) (js/document.querySelector "#app")))
