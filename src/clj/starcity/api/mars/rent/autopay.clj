(ns starcity.api.mars.rent.autopay
  (:require [compojure.core :refer [defroutes GET POST]]
            [datomic.api :as d]
            [starcity
             [auth :as auth]
             [datomic :refer [conn]]]
            [starcity.models
             [account :as account]
             [autopay :as autopay]
             [news :as news]]
            [starcity.util.response :as res]
            [taoensso.timbre :as timbre]))

(defn- subscribed-handler
  "Handles requests to determine whether or not the requesting user is
  subscribed to autopay or not."
  [req]
  (let [account (auth/requester req)]
    (res/json-ok {:subscribed (autopay/subscribed? conn account)})))

(def ^:private already-subscribed
  (res/json-unprocessable {:error "You are already subscribed to autopay -- cannot subscribe again."}))

(defn- subscribe-handler
  "Subscribes requesting user to autopay. "
  [{:keys [params] :as req}]
  (let [account (auth/requester req)]
    (if (autopay/subscribed? conn account)
      already-subscribed
      (try
        ;; TODO: This should be an event.
        (autopay/subscribe! conn account)
        ;; Dismiss the news item that prompted `account` to set up autopay, as it's now setup.
        (d/transact conn [(->> news/autopay-action (news/by-action conn account) news/dismiss)])
        (res/json-ok {:status (autopay/setup-status conn account)})
        (catch Exception e
          (timbre/error e ::subscribe {:account (account/email account)})
          (throw e))))))

(defroutes routes
  (GET "/subscribed" [] subscribed-handler)

  (POST "/subscribe" [] subscribe-handler))
