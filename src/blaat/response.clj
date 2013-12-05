(ns blaat.response
  (:use [blaat.cache]
        [blaat.url])
  (:require [blaat.util :as util]
            [ring.util.response :as ring-response]))

(defn response [& {:keys [status content-type headers body]
                   :or {status 200 content-type "text/html" headers {} body ""}}]
  {:status status
   :headers {"Content-type" "text/html"}
   :body body})

(defn flash [response data]
  (let [flash-id (util/rand-string 16)]
    (cache-set! flash-id data)
    (assoc response :flash flash-id)))

(defn redirect [uri]
  (ring-response/redirect uri))
