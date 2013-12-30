(ns blaat.response
  (:use [blaat.cache]
        [blaat.url]
        [blaat.i18n])
  (:require [blaat.util :as util]
            [ring.util.response :as ring-response]))

(defn response [& {:keys [status content-type headers body]
                   :or {status 200 content-type "text/html" headers {} body ""}}]
  {:status status
   :headers {"Content-type" "text/html"}
   :body body})

(defn redirect [uri]
  (ring-response/redirect uri))

(defn security-error []
  (throw (ex-info (_t "Security Error") {})))


(comment

  (security-error)

  )
