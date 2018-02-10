(ns public.nrepl
  (:require [clojure.tools.nrepl.server :refer [start-server stop-server]]
            [mount.core :as mount :refer [defstate]]
            [public.config :as config :refer [config]]
            [taoensso.timbre :as timbre]))

(defn- start-nrepl [port]
  (timbre/debug ::starting {:port port})
  (start-server :port port))

(defstate nrepl
  :start (start-nrepl (config/nrepl-port config))
  :stop  (stop-server nrepl))
