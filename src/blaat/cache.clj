(ns blaat.cache
  (:require [clojurewerkz.spyglass.client :as c]))

(def ^:dynamic *client*) ;;set in app.clj

(defn cache-set! [key value & {:keys [expiration] :or {expiration 300}}]
  @(c/set *client* key expiration value))

(defn cache-get [key]
  (c/get *client* key))

(comment

  (binding [*client* (c/bin-connection "localhost:11211")]
    (cache-set! "aap" {:aap 10 :blaat "aap"}))

  (binding [*client* (c/bin-connection "localhost:11211")]
    (cache-get "aap"))


)
