(ns blaat.handler
  (:require [blaat.tmpl :as tmpl]))

(defn response [& {:keys [status content-type headers body]
                   :or {status 200 content-type "text/html" headers {} body ""}}]
  {:status status
   :headers {"Content-type" "text/html"}
   :body body})

(defn main [request]
  (response
    :body (tmpl/main :title "Main" :content "Main Content")))
