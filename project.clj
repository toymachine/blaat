(defproject blaat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"spy-memcached" {:url "http://files.couchbase.com/maven2/"}}
  :dependencies [
         [org.clojure/clojure "1.5.1"]
         [org.clojure/clojurescript "0.0-2030"]
         [org.clojure/tools.logging "0.2.6"]
				 [org.clojure/data.json "0.2.3"]
         [ring/ring-core "1.2.1"]
         [ring/ring-devel "1.2.1"]
         [ring-anti-forgery "0.3.0"]
         [lein-light-nrepl "0.0.1"]
         [commons-codec "1.6"]
         [clojurewerkz/spyglass "1.1.0"]
         [com.datomic/datomic-pro "0.9.4331"]
         [net.cgrand/moustache "1.2.0-alpha2"]
         [formative "0.8.7"]
         [clj-bcrypt-wrapper "0.1.0"]
				 [http-kit "2.1.13"]
				 [criterium "0.4.2"]]
  :plugins [[lein-ring "0.8.8"]]
  :ring {:handler blaat.app/blaat-app}
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]}
  :main blaat.run)
