(ns public.auth
  (:require [datomic.api :as d]
            [public.datomic :refer [conn]]))

;; =============================================================================
;; API
;; =============================================================================

(defn ^{:deprecated "1.6.0"} requester
  "Produces the entity of the user that made the request."
  [req]
  (let [account-id (get-in req [:identity :db/id])]
    (d/entity (d/db conn) account-id)))
