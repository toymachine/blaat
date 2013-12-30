(ns blaat.flash
  (:use [blaat.cache])
  (:require [blaat.util :as util]))

(defn set-data [response data]
  (let [flash-id (util/rand-string 16)]
    (cache-set! flash-id data)
    (assoc response :flash flash-id)))

(defn get-data [request]
  (when-let [flash-id (:flash request)]
     (cache-get flash-id)))

(defn form [response form]
  (data response {:form form}))

(defn success [response & msgs]
  (data response {:success msgs}))
