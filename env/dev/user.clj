(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            ;; [clojure.spec.test.alpha :as stest]
            [datomic.api :as d]
            [starcity.server]
            [starcity.seed :as seed]
            [starcity.log]
            [starcity.nrepl]
            [starcity.config :as config :refer [config]]
            [starcity.scheduler]
            [net.cgrand.reload]
            ;; convenience
            [starcity.datomic :refer [conn]]
            [taoensso.timbre :as timbre]
            [mount.core :as mount :refer [defstate]]
            [toolbelt.core]
            [clj-livereload.server :as livereload]))


(timbre/refer-timbre)

;; Autoreload on change during dev
(net.cgrand.reload/auto-reload *ns*)


;; =============================================================================
;; Reloaded Workflow


(defn- in-memory-db?
  "There's a more robust way to do this, but it's not really necessary ATM."
  []
  (= "datomic:mem://localhost:4334/starcity"
     (config/datomic-uri config)))


(defstate seed
  :start (when (in-memory-db?)
           (timbre/debug "seeding dev database...")
           (seed/seed conn)))


(defstate livereload
  :start (livereload/start! {:paths ["resources/templates"
                                     "resources/public/assets"]
                             :debug? true})
  :stop  (livereload/stop!))


(defn start []
  (mount/start-with-args {:env :dev}))


(def stop mount/stop)


(defn go []
  (start)
  ;; (stest/instrument)
  :ready)


(defn reset []
  (stop)
  (refresh :after 'user/go))
