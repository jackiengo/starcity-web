(ns starcity.datomic
  (:require [datomic.api :as d]
            [mount.core :as mount :refer [defstate]]
            [starcity.config :as config :refer [config]]
            [blueprints.core :as db]
            [taoensso.timbre :as timbre]))

;; =============================================================================
;; Helpers
;; =============================================================================


(defn- new-connection [uri]
  (timbre/info ::connecting {})
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (db/conform-db conn :db.part/starcity)
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
