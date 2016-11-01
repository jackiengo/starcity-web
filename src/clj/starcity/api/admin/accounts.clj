(ns starcity.api.admin.accounts
  (:require [compojure.core :refer [defroutes GET POST]]
            [starcity.api.admin.accounts.list :as list]
            [starcity.api.common :as api]
            [starcity.util :refer [str->int]]))

(defroutes routes
  (GET "/" [limit offset direction sort-key view]
       (fn [_] (list/fetch (str->int limit)
                          (str->int offset)
                          (keyword direction)
                          (keyword sort-key)
                          (keyword view)))))
