(ns admin.db
  (:require [admin.application.list.db :as applications]
            [admin.application.entry.db :as application]
            [admin.account.list.db :as accounts]
            [admin.notify.db :as notify]))

(def default-value
  {applications/root-db-key applications/default-value
   application/root-db-key  application/default-value
   accounts/root-db-key     accounts/default-value
   notify/root-db-key       notify/default-value
   :route                   :home})
