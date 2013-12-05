(ns blaat.handler
  (:require [blaat.tmpl :as tmpl]
            [blaat.util :as util]
            [clojure.tools.logging :as log]
            [formative.core :as f]
            [formative.parse :as fp])
  (:use [blaat.url]
        [blaat.i18n]
        [blaat.cache]
        [blaat.form]
        [blaat.response]
        [blaat.user :only [get-user-id-by-email-and-password]]))

(defn create-account [request]
  (response
    :body (tmpl/main :title "Create account" :content (tmpl/create-account-form))))

(defn create-account-action [request]
  (redirect (url "/account/create")))

(defn- validate-password [{:keys [email password]}]
  (when (and (seq email) (seq password))
    (when-not (get-user-id-by-email-and-password email password)
      {:keys [:email :password] :msg (_t "Invalid username or password")})))

(defn login-form []
  {:action (url "/login")
   :method "post"
   :fields [{:name :email :type :email}
            {:name :password :type :password}]
   :validations [[:required [:email :password] (_t "Please enter both email and password")]]
   :validator validate-password
   :submit-label (_t "Login")})

(defn login [request]
  (let [form (load-form request (login-form))]
    (response
      :body (tmpl/main :title (_t "Login") :content (render-form form)))))

(defn login-action [request]
  (validate-form request (login-form)
    (fn [values]
      (let [{:keys [email password]} values
            user-id (get-user-id-by-email-and-password email password)]
        (-> (redirect (url "/")) ;;TODO add redirect url to form
            (assoc :session {:user-id user-id})))))) ;sets up new session, e.g. if there was any it is now overwritten

(defn main [request]
  (response
    :body (tmpl/main :title (_t "Main") :content "Main Content")))

(comment

  (fp/parse-params (login-form) {} :validate true)

  (assoc-in {} [:session :member-id] 1234)

    (let [request {}]
        (-> (redirect (url "/"))
            (assoc :session (assoc (:session request) :member-id 1234))))


  )
