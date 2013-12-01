(defproject blaat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
         [org.clojure/clojure "1.5.1"]
				 [org.clojure/data.json "0.2.3"]
         [com.datomic/datomic-free "0.8.3561"]
				 [net.cgrand/moustache "1.2.0-alpha2"]
				 [lib-noir "0.7.6"]
				 [criterium "0.4.2"]
         [com.cemerick/friend "0.2.0"]]
  :plugins [[lein-ring "0.8.8"]]
  :ring {:handler blaat.app/blaat-app}
  :main blaat.core)
