(ns public.datomic
  (:require [blueprints.core :as db]
            [datomic.api :as d]
            [mount.core :refer [defstate]]
            [public.config :as config :refer [config]]
            [taoensso.timbre :as timbre]))

;; =============================================================================
;; Helpers
;; =============================================================================


(defn- new-connection [uri]
  (timbre/info ::connecting {})
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (db/conform-db conn (config/datomic-part config))
    conn))


(defn- disconnect [uri conn]
  (timbre/info ::disconnecting {})
  (d/release conn))


;; =============================================================================
;; API
;; =============================================================================


(defstate conn
  :start (new-connection (config/datomic-uri config))
  :stop  (disconnect (config/datomic-uri config) conn))

(defn tempid []
  (d/tempid (config/datomic-part config)))
