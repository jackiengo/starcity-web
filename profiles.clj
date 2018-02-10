{:dev {:source-paths ["src" "env/dev"]
       :plugins      [[lein-cooper "1.2.2" :exclusions [org.clojure/clojure]]]
       :dependencies [[com.datomic/datomic-free "0.9.5544"]
                      [clj-livereload "0.2.0"]]
       :cooper       {"public" ["sass" "--watch" "-E" "UTF-8" "style/sass/public.scss:resources/public/assets/css/public.css"]}}

 :uberjar {:aot          :all
           :main         public.core
           :source-paths ["src"]

           :dependencies [[com.datomic/datomic-pro "0.9.5544" :exclusions [com.google.guava/guava]]
                          [org.postgresql/postgresql "9.4.1211"]]

           :repositories {"my.datomic.com" {:url      "https://my.datomic.com/repo"
                                            :username :env/datomic_username
                                            :password :env/datomic_password}}}}
