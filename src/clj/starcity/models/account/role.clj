(ns starcity.models.account.role
  (:require [clojure.spec :as s]
            [toolbelt.predicates :refer [entity?]]))

(s/def ::role
  #{:account.role/admin
    :account.role/member
    :account.role/onboarding
    :account.role/applicant
    :account.role/collaborator})

;;; Roles

(def admin :account.role/admin)
(def onboarding :account.role/onboarding)
(def applicant :account.role/applicant)
(def member :account.role/member)
(def collaborator :account.role/collaborator)

;; Role Hierarchy

(derive admin applicant)
(derive admin member)
(derive admin onboarding)

;;; Predicates

(defn- is-role [role account]
  (= role (:account/role account)))

(s/fdef is-role
        :args (s/cat :role ::role :account entity?)
        :ret boolean?)

(def applicant? (partial is-role applicant))
(def member? (partial is-role member))
(def admin? (partial is-role admin))
(def onboarding? (partial is-role onboarding))
(def collaborator? (partial is-role collaborator))

;;; Transactions

(defn change-role
  "Produce transaction data to change `account`'s role to `role`"
  [account role]
  {:db/id        (:db/id account)
   :account/role role})

(s/fdef change-role
        :args (s/cat :account entity? :role ::role)
        :ret (s/keys :req [:db/id :account/role]))
