(ns blaat.handler
  (:require [cemerick.friend :as friend]
            [blaat.tmpl :as tmpl]
            [ring.util.response :as ring-response]
            )
  (:use [blaat.url]))

(defn response [& {:keys [status content-type headers body]
                   :or {status 200 content-type "text/html" headers {} body ""}}]
  {:status status
   :headers {"Content-type" "text/html"}
   :body body})

(defn redirect [url]
  (ring-response/redirect (dyn-url url)))

(defn create-account [request]
  (response
    :body (tmpl/main :title "Create account" :content (tmpl/create-account-form))))

(defn create-account-action [request]
  (redirect "/account/create"))

(defn login [request]
  (response
    :body (tmpl/main :title "Login" :content (tmpl/login-form))))

(defn main [request]
  (friend/authorize #{::admin}
    (response
      :body (tmpl/main :title "Main" :content "Main Content"))))
