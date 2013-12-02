(ns blaat.handler
  (:require [blaat.tmpl :as tmpl]
            [blaat.util :as util]
            [ring.util.response :as ring-response]
            [clojure.tools.logging :as log]
            [formative.core :as f]
            [formative.parse :as fp])
  (:use [blaat.url]
        [blaat.i18n]
        [blaat.cache]))

(defn response [& {:keys [status content-type headers body]
                   :or {status 200 content-type "text/html" headers {} body ""}}]
  {:status status
   :headers {"Content-type" "text/html"}
   :body body})

(defn flash [response data]
  (let [flash-id (util/rand-string 16)]
    (prn (cache-set! flash-id data))
    (prn "flash" flash-id data)
    (assoc response :flash flash-id)))

(defn redirect [url]
  (ring-response/redirect (dyn-url url)))

(defn create-account [request]
  (response
    :body (tmpl/main :title "Create account" :content (tmpl/create-account-form))))

(defn create-account-action [request]
  (redirect "/account/create"))

(defn login-form []
  {:action (dyn-url "/login")
   :method "post"
   :fields [{:name :email :type :email}
            {:name :password :type :password}]
   :validations [[:required [:email :password] (_t "Please enter both email and password")]]
   :values {}
   :submit-label (_t "Login")})


(defn login [request]
  (if-let [flash-id (:flash request)]
    (prn flash-id (cache-get flash-id)))
  (response
    :body (tmpl/main :title "Login" :content (f/render-form (login-form)))))

(defn login-action [request]
  (let [params (:params request)]
    (prn params)
    (fp/with-fallback (fn [problems]
                        (-> (redirect "/login")
                            (flash {:form {:params params :problems problems}})))
      (fp/parse-params (login-form) params :validate true))))

(defn main [request]
  (response
    :body (tmpl/main :title "Main" :content "Main Content")))

(comment
  (login-form)
(f/render-form (login-form))

  (fp/with-fallback (fn [p] (prn p) (redirect "/login"))
    (fp/parse-params (login-form) {:secret-code "xxx"} :validate true))

  )
