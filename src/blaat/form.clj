(ns blaat.form
  (:use [blaat.cache]))

(defn load-form [request form]
  (let [[values problems]
    (when-let [flash-id (:flash request)]
       (when-let [data (cache-get flash-id)]
         [(get-in data [:form :values]) (get-in data [:form :problems])]))]

    (merge form {:value values
                 :problems problems})))
