(ns blaat.form
  (:require [blaat.util :as util]
            [blaat.flash :as flash]
            [formative.core :as f]
            [formative.parse :as fp]
            [ring.util.response :as ring-response])
  (:use [blaat.cache]
        [blaat.response]))


(defn render-form [form]
  (f/render-form form))

(defn load-form [request form]
  (let [[values problems]
    (when-let [data (flash/get-data request)]
       [(get-in data [:form :values]) (get-in data [:form :problems])])]

    (merge form {:values values
                 :problems problems})))

(defn when-valid-form [request form valid-response-fn]
  (let [values (:params request)
        on-problems (fn [problems]
                      (let [problems (read-string (pr-str problems))];;note problems is a lazy seq of lazy seqs that cannot be serialize, this solves it
                        (-> (ring-response/redirect (:action form))
                            (flash/form {:values values :problems problems}))))]
    (fp/with-fallback on-problems
      (fp/parse-params form values :validate true)
      ;;if we are here all is ok (e.g. no exception thrown)
      (valid-response-fn values))))
