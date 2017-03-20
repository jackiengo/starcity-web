(ns starcity.controllers.landing
  (:require [ring.util.response :as response]
            [selmer.parser :as selmer]
            [starcity.services.mailchimp :as mailchimp]
            [starcity.views.common :refer [public-defaults]]
            [taoensso.timbre :as timbre]))

(defn- log-subscriber-request
  [email {:keys [status body]}]
  (timbre/info :mailchimp/new-subscriber {:email email :status status}))

(def ^:private url-after-newsletter-signup
  "/?newsletter=subscribed#newsletter")

(defn newsletter-signup [{:keys [params] :as req}]
  (if-let [email (:email params)]
    (do
      (mailchimp/add-interested-subscriber! email (partial log-subscriber-request email))
      (response/redirect url-after-newsletter-signup))
    (response/redirect "/")))

(defn show
  "Show the landing page."
  [req]
  (selmer/render-file "landing.html" (public-defaults req)))
