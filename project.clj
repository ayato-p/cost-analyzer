(defproject cost-analyzer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"
            :year 2015
            :key "mit"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [rkworks/baum "0.1.3"]
                 [com.google.gdata/core "1.47.1"]
                 [com.google.api-client/google-api-client "1.20.0"]
                 [com.google.api.client/google-api-client-http "1.2.3-alpha"]
                 [com.google.http-client/google-http-client-jackson "1.20.0"]]
  :main cost-analyzer.core
  :uberjar-name "cost-analyzer.jar"
  :profiles {:uberjar {:aot :all}})
