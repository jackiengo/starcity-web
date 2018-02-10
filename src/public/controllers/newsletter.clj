(ns public.controllers.newsletter
  (:require [blueprints.models.events :as events]
            [datomic.api :as d]
            [net.cgrand.enlive-html :as html]
            [ring.util.response :as response]
            [public.controllers.common :as common]
            [public.datomic :refer [conn]]))


(html/defsnippet newsletter "templates/subscribed-newsletter.html" [:main] [])


(defn show
  [req]
  (common/render-ok
   (common/page req {:css-bundles ["public.css"]
                     :js-bundles  ["main.js"]
                     :main        (newsletter)})))


(defn subscribe!
  "Subscribe the specified email address (params) to our newsletter."
  [req]
  (when-let [email (get-in req [:params :email])]
    @(d/transact-async conn [(events/add-newsletter-subscriber email)]))
  (response/redirect "/newsletter"))
