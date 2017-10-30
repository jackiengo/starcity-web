(ns starcity.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [mount.core :as mount :refer [defstate]]
            [toolbelt.core :as tb]))

;; =============================================================================
;; Config Loader/State
;; =============================================================================


(defstate config
  :start (-> (io/resource "config.edn")
             (aero/read-config {:resolver aero/root-resolver
                                :profile  (:env (mount/args))})))


;; =============================================================================
;; Environment
;; =============================================================================


(defn is-development?
  [config]
  (= :dev (:env (mount/args))))


(defn is-staging?
  [config]
  (= :stage (:env (mount/args))))


(defn is-production?
  [config]
  (= :prod (:env (mount/args))))


;; =============================================================================
;; Webserver
;; =============================================================================


(defn webserver-port
  "Port to start the webserver on."
  [config]
  (tb/str->int (get-in config [:webserver :port])))


(defn session-name
  "The name of the session cookie."
  [config]
  (get-in config [:webserver :session :name]))


(defn secure-sessions?
  "Should sessions be secure?"
  [config]
  (get-in config [:webserver :session :secure]))


(defn session-domain
  "The domain for the session cookie."
  [config]
  (get-in config [:webserver :session :domain]))


;; =============================================================================
;; Datomic
;; =============================================================================

(defn datomic-uri
  "URI of the Datomic database connection."
  [config]
  (get-in config [:datomic :uri]))


;; =============================================================================
;; nrepl
;; =============================================================================


(defn nrepl-port
  "Port to start the nrepl server on."
  [config]
  (tb/str->int (get-in config [:nrepl :port])))


;; =============================================================================
;; Hosts
;; =============================================================================


(defn hostname
  "The hostname of this server."
  [config]
  (get-in config [:hosts :this]))


(defn apply-hostname
  "The hostname of the apply service."
  [config]
  (get-in config [:hosts :apply]))


(defn odin-hostname
  "The hostname of the odin service."
  [config]
  (get-in config [:hosts :odin]))


(defn onboarding-hostname
  "The hostname of the onboarding service."
  [config]
  (get-in config [:hosts :onboarding]))


;; =============================================================================
;; Logs
;; =============================================================================


(defn log-level
  [config]
  (get-in config [:log :level]))


(defn log-appender
  "The timbre appender to use."
  [config]
  (get-in config [:log :appender]))


(defn log-file
  "The file to log to."
  [config]
  (get-in config [:log :file]))
