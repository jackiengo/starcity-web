(ns starcity.controllers.common
  (:require [ring.util.response :as response]))


(defn session-data
  "Produce the data that should be stored in the session for `account`."
  [account]
  {:db/id              (:db/id account)
   :account/email      (:account/email account)
   :account/role       (:account/role account)
   :account/activated  (:account/activated account)
   :account/first-name (:account/first-name account)
   :account/last-name  (:account/last-name account)})


(defn ok
  [body]
  (-> (response/response body)
      (response/content-type "text/html")))

(defn render [t]
  (apply str t))

(def render-ok
  (comp ok render))

(defn malformed
  [body]
  (-> (response/response body)
      (response/status 400)
      (response/content-type "text/html")))

(def render-malformed
  (comp malformed render))
