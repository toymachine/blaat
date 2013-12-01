(ns blaat.cache
  (:require [clojurewerkz.spyglass.client :as c])
  (:refer-clojure :exclude [get set]))

(def ^:dynamic *client*) ;;set in app.clj

(defn set! [key value & {:keys [expiration] :or {expiration 300}}]
  (c/set *client* key expiration value))

(defn get [key]
  (c/get *client* key))

(comment

  (binding [*client* (c/bin-connection "localhost:11211")]
    (set! "aap" {:aap 10 :blaat "aap"}))

  (binding [*client* (c/bin-connection "localhost:11211")]
    (get "aap"))

  )
