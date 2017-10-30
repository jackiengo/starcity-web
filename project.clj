(defproject starcity/web "3.0.0-SNAPSHOT"
  :description "The web app for https://joinstarcity.com"
  :url "https://joinstarcity.com"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [;; Clojure
                 [org.clojure/clojure "1.9.0-beta3"]
                 [org.clojure/core.async "0.3.443"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [org.clojure/test.check "0.9.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 ;; Web
                 [http-kit "2.2.0"]
                 [compojure "1.5.1"]
                 [cheshire "5.6.3"]
                 [ring/ring "1.5.0"]
                 [ring-middleware-format "0.7.2"]
                 [buddy "1.1.0"]
                 [bouncer "1.0.0"]
                 [optimus "0.19.1"]
                 [starcity/datomic-session-store "0.1.0"]
                 [starcity/customs "1.0.0-SNAPSHOT"]
                 [starcity/facade "1.0.0-SNAPSHOT"]
                 ;; HTTP APIs
                 [org.apache.httpcomponents/httpclient "4.5.2"] ; dep resolution?
                 ;; Time
                 [clj-time "0.14.0"]
                 [im.chit/hara.io.scheduler "2.4.8"]
                 ;; Datomic
                 [io.rkn/conformity "0.4.0"]
                 [starcity/blueprints "2.0.0-SNAPSHOT" :exclusions [com.datomic/datomic-free]]
                 [com.datomic/datomic-pro "0.9.5554" :exclusions [com.google.guava/guava]]
                 [com.amazonaws/aws-java-sdk-dynamodb "1.11.6"]
                 ;; Utility
                 [mount "0.1.11"]
                 [aero "1.1.2"]
                 [com.taoensso/timbre "4.8.0"]
                 [starcity/drawknife "1.0.0-SNAPSHOT"]
                 [starcity/toolbelt "1.0.0-SNAPSHOT" :exclusions [com.datomic/datomic-free]]
                 [enlive "1.1.6"]
                 [com.akolov.enlive-reload "0.2.1"]]

  :jvm-opts ["-server"
             "-Xmx4g"
             "-XX:+UseCompressedOops"
             "-XX:+DoEscapeAnalysis"
             "-XX:+UseConcMarkSweepGC"]

  :repositories {"releases"       {:url        "s3://starjars/releases"
                                   :username   :env/aws_access_key
                                   :passphrase :env/aws_secret_key}
                 "my.datomic.com" {:url      "https://my.datomic.com/repo"
                                   :username :env/datomic_username
                                   :password :env/datomic_password}}

  :plugins [[s3-wagon-private "1.2.0"]]

  :repl-options {:init-ns user}

  :clean-targets ^{:protect false} [:target-path]

  :uberjar-name "web-standalone.jar"

  :cooper {"internal" ["sass" "--watch" "-E" "UTF-8" "style/sass/main.sass:resources/public/assets/css/starcity.css"]
           "public"   ["sass" "--watch" "-E" "UTF-8" "style/sass/public.scss:resources/public/assets/css/public.css"]}

  :main starcity.core)
