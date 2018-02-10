(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [clojure.tools.namespace.find :as nfind]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.alpha :as s]
            [datomic.api :as d]
            [public.server]
            [public.seed :as seed]
            [public.log]
            [public.nrepl]
            [public.config :as config :refer [config]]
            [public.countries]
            [net.cgrand.reload]
            ;; convenience
            [public.datomic :refer [conn]]
            [taoensso.timbre :as timbre]
            [mount.core :as mount :refer [defstate]]
            [clj-livereload.server :as livereload]
            [clojure.java.io :as io]))

(timbre/refer-timbre)


;; reloaded ===================================================================


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


(defn autoreload []
  ;; Autoreload on change during dev
  (doseq [n (nfind/find-namespaces-in-dir (io/file "src/clj/starcity/controllers"))]
    (net.cgrand.reload/auto-reload n)))


(defn start []
  (mount/start-with-args {:env :dev}))


(def stop
  mount/stop)


(defn go []
  (stest/instrument)
  (start)
  (autoreload)
  :ready)


(defn reset []
  (stop)
  (refresh-all :after 'user/go)
  (autoreload))
