(ns starcity.controllers.application.community-fitness
  (:require [bouncer.core :as b]
            [datomic.api :as d]
            [ring.util.response :as response]
            [starcity.controllers.application.common :as common]
            [starcity.controllers.utils :refer :all]
            [starcity.datomic :refer [conn]]
            [starcity.models.application :as application]
            [starcity.views.application.community-fitness :as view]))

;; =============================================================================
;; Helpers
;; =============================================================================

(defn- can-view-community-fitness?
  [{:keys [identity] :as req}]
  (when-let [application-id (:db/id (application/by-account-id (:db/id identity)))]
    (application/personal-information-complete? application-id)))

(defn- validate-params
  [params]
  (b/validate
   params
   {:prior-community-housing [(required "Please tell us about any prior community housing experience.")]
    :skills                  [(required "Please tell us about your skills.")]
    :free-time               [(required "Please tell us about how you spend your free time.")]
    :why-interested          [(required "Please tell us about why you want to live with others.")]}))

(defn- pull-data
  [account-id]
  (letfn [(-format [{:keys [community-fitness/prior-community-housing
                            community-fitness/why-interested
                            community-fitness/skills
                            community-fitness/free-time
                            community-fitness/dealbreakers]}]
            {:prior-community-housing prior-community-housing
             :why-interested            why-interested
             :skills                  skills
             :free-time               free-time
             :dealbreakers            dealbreakers})]
    (if-let [cf-id (-> (application/by-account-id account-id) :member-application/community-fitness :db/id)]
      (-format (d/pull (d/db conn) [:community-fitness/prior-community-housing
                                    :community-fitness/why-interested
                                    :community-fitness/skills
                                    :community-fitness/free-time
                                    :community-fitness/dealbreakers] cf-id))
      {})))

;; =============================================================================
;; API
;; =============================================================================

(defn show-community-fitness
  [{:keys [identity] :as req}]
  (let [current-steps (application/current-steps (:db/id identity))
        data          (pull-data (:db/id identity))]
    (ok (view/community-fitness req current-steps data))))

(defn save!
  [{:keys [identity params] :as req}]
  (let [account-id (:db/id identity)
        vresult    (validate-params params)]
    (if-let [_ (valid? vresult)]
      (let [application-id (:db/id (application/by-account-id account-id))]
        (application/update-community-fitness! application-id params)
        (response/redirect "/application/submit"))
      (let [current-steps (application/current-steps account-id)]
        (malformed (view/community-fitness req current-steps params
                                           :errors (errors-from vresult)))))))

(def restrictions
  (common/restrictions can-view-community-fitness?))
