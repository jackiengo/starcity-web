(defproject starcity/public "3.0.1-SNAPSHOT"
  :description "Starcity's public website: https://starcity.com"
  :url "https://starcity.com"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [;; clojure
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [org.clojure/tools.cli "0.3.5"]
                 ;; web
                 [http-kit "2.2.0"]
                 [compojure "1.6.0"]
                 [cheshire "5.8.0"]
                 [ring/ring "1.6.3"]
                 [ring-middleware-format "0.7.2"]
                 [buddy "2.0.0"]
                 [bouncer "1.0.1"]
                 [optimus "0.20.1"]
                 [starcity/datomic-session-store "0.1.0"]
                 [starcity/customs "1.0.0"]
                 [starcity/facade "0.4.0"]
                 [clj-time "0.14.2"]
                 ;; reloaded
                 [mount "0.1.12"]
                 ;; datomic
                 [io.rkn/conformity "0.5.1"]
                 [starcity/blueprints "2.2.0"]
                 ;; utility
                 [aero "1.1.2"]
                 [com.taoensso/timbre "4.10.0"]
                 [starcity/drawknife "1.0.0"]
                 [starcity/toolbelt-core "0.4.0"]
                 [starcity/toolbelt-datomic "0.2.0"]
                 [enlive "1.1.6"]
                 [com.akolov.enlive-reload "0.2.1"]]

  :jvm-opts ["-server"
             "-Xmx4g"
             "-XX:+UseCompressedOops"
             "-XX:+DoEscapeAnalysis"
             "-XX:+UseConcMarkSweepGC"]

  :repositories {"releases" {:url        "s3://starjars/releases"
                             :username   :env/aws_access_key
                             :passphrase :env/aws_secret_key}}

  :plugins [[s3-wagon-private "1.2.0"]]

  :jar-name "starcity-web.jar"

  :repl-options {:init-ns user})
