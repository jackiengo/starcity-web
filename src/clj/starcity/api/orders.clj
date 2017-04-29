(ns starcity.api.orders
  (:require [compojure.core :refer [defroutes DELETE GET]]
            [datomic.api :as d]
            [starcity
             [datomic :refer [conn]]
             [util :refer [str->int]]]
            [starcity.models.order :as order]
            [starcity.util
             [request :as req]
             [response :as res]]))

;; =============================================================================
;; Handlers
;; =============================================================================

(defn fetch-orders [req]
  (let [account (req/requester (d/db conn) req)]
    (res/transit-ok
     {:result (->> (order/orders (d/db conn) account)
                   (map order/clientize)
                   (sort-by :price #(if (and %1 %2) (> %1 %2) false)))})))

(defn delete-order
  [req order-id]
  (let [account (req/requester (d/db conn) req)
        order   (d/entity (d/db conn) order-id)]
    (if (not= (:db/id account) (-> order :order/account :db/id))
      (res/transit-forbidden {:error "You do not own this order."})
      (do
        @(d/transact conn [[:db.fn/retractEntity order-id]])
        (res/transit-ok {:message "ok"})))))

;; =============================================================================
;; Routes
;; =============================================================================

;; /api/v1/orders
(defroutes routes
  (GET "/" [] fetch-orders)
  (DELETE "/:order-id" [order-id]
          (fn [req]
            (delete-order req (str->int order-id)))))
