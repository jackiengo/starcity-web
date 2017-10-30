{:dev {:source-paths ["src/clj" "env/dev"]
       :plugins      [[lein-cooper "1.2.2" :exclusions [org.clojure/clojure]]]
       :dependencies [[clj-livereload "0.2.0"]]}

 :uberjar {:aot          :all
           :main         starcity.core
           :source-paths ["src/clj"]}}
