(ns blaat.handler
  (:require [blaat.tmpl :as tmpl]
            [blaat.util :as util]
            [clojure.tools.logging :as log]
            [formative.core :as f]
            [formative.parse :as fp])
  (:use [blaat.url]
        [blaat.i18n]
        [blaat.cache]
        [blaat.form]))


(defn create-account [request]
  (response
    :body (tmpl/main :title "Create account" :content (tmpl/create-account-form))))

(defn create-account-action [request]
  (redirect (url "/account/create")))

(defn login-form []
  {:action (url "/login")
   :method "post"
   :fields [{:name :email :type :email}
            {:name :password :type :password}]
   :validations [[:required [:email :password] (_t "Please enter both email and password")]]
   :submit-label (_t "Login")})

(defn login [request]
  (let [form (load-form request (login-form))]
    (response
      :body (tmpl/main :title (_t "Login") :content (render-form form)))))

(defn login-action [request]
  (validate-form request (login-form)
    (fn [values]
      (prn "valid!" values))))


(defn main [request]
  (response
    :body (tmpl/main :title (_t "Main") :content "Main Content")))

(comment

  (fp/parse-params (login-form) {} :validate true)

  )
