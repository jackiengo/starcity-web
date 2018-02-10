(ns public.util.request
  (:require [clojure.spec.alpha :as s]
            [datomic.api :as d]
            [toolbelt.datomic :as td]))

(defn requester
  "Produce the `account` entity that initiated this `request`."
  [db request]
  (let [id (get-in request [:identity :db/id])]
    (d/entity db id)))

(s/fdef requester
        :args (s/cat :db td/db? :request map?)
        :ret (s/nilable td/entityd?))
