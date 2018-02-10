(ns public.models.unit-test
  (:require [blueprints.models.unit :as unit]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.test :refer :all]
            [datomic.api :as d]
            [public.test.datomic :as db :refer [with-conn]]))

(use-fixtures :once db/conn-fixture)

(defn unit
  [name]
  {:db/id     (d/tempid :db.part/user)
   :unit/name name})

(defn member
  [email status ends & [unit]]
  {:db/id              (d/tempid :db.part/user)
   :account/first-name email
   :account/email      email
   :account/role       :account.role/member
   :account/licenses   (merge
                        {:member-license/status status
                         :member-license/ends   ends}
                        (when unit
                          {:member-license/unit (:db/id unit)}))})

;; (defn renewal-license
;;   [account-id ends unit-id]
;;   {:db/id            account-id
;;    :account/licenses {:member-license/unit   unit-id
;;                       :member-license/status :member-license.status/renewal
;;                       :member-license/ends   ends}})

(defn sdate [y m d]
  (c/to-date (t/date-time y m d)))

(deftest available?
  (with-conn conn
    (let [unit-1 (unit "unit-1")
          jon    (member "jon@abc.com" :member-license.status/active (sdate 2017 2 28) unit-1)
          db     (db/speculate (d/db conn) [unit-1 jon])]

      (testing "Units with active licenses that have not yet expired are not available."
        (is (not (unit/available? db (unit/by-name db "unit-1") (sdate 2017 2 27)))))

      (testing "Units with end-dates in the past are available."
        (is (unit/available? db (unit/by-name db "unit-1") (sdate 2017 3 1))))

      #_(testing "Renewal licences are taken into consideration."
        (let [renewal (renewal-license (:db/id jon) (sdate 2017 5 31) (:db/id unit-1))
              db      (db/speculate (d/db conn) [unit-1 jon renewal])]

          (is (false? (unit/available? db (unit/by-name db "unit-1") (sdate 2017 3 1))))
          (is (true? (unit/available? db (unit/by-name db "unit-1") (sdate 2017 6 1)))))))))
