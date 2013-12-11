(ns blaat.handler
  (:require [blaat.tmpl :as tmpl]
            [blaat.util :as util]
            [blaat.user :as user]
            [clojure.tools.logging :as log]
            [formative.core :as f]
            [formative.parse :as fp]
            )
  (:use [blaat.url]
        [blaat.i18n]
        [blaat.cache]
        [blaat.form]
        [blaat.response]))

(defn main-response [& {:keys [title content script]}]
  (response
    :body (tmpl/main :title title :content content :script script
                     :logged-in-user? (user/logged-in-user?)
                     :user-name (user/user-name (user/logged-in-user)))))

(defn create-account-form []
  {:action (url "/register")
   :method "post"
   :fields [{:name :email :type :email}
            {:name :password :type :password}]
   :validations [[:required [:email :password] (_t "Please enter both email and password")]]
   :submit-label (_t "Create account")})

;;TODO make sure not logged in
(defn create-account [request]
  (let [form (load-form request (create-account-form))]
    (main-response :title (_t "Create account") :content (render-form form))))

(defn create-account-action [request]
  (validate-form request (create-account-form)
   (fn [values]
     (let [{:keys [email password]} values]
       (user/create-account email password) ;;TODO handle error about duplicate email and sync db
       (redirect (url "/")))))) ;;TODO add redirect to correct url

(defn- validate-password [{:keys [email password]}]
  (when (and (seq email) (seq password))
    (when-not (user/get-user-id-by-email-and-password email password)
      {:keys [:email :password] :msg (_t "Invalid email or password")})))

(defn login-form []
  {:action (url "/login")
   :method "post"
   :fields [{:name :email :type :email}
            {:name :password :type :password}]
   :validations [[:required [:email :password] (_t "Please enter both email and password")]]
   :validator validate-password
   :submit-label (_t "Log in")})

(defn login [request]
  (let [form (load-form request (login-form))]
    (main-response :title (_t "Log in") :content (render-form form))))

(defn login-action [request]
  (validate-form request (login-form)
    (fn [values]
      (let [{:keys [email password]} values
            user-id (user/get-user-id-by-email-and-password email password)]
        (-> (redirect (url "/")) ;;TODO add redirect url to form
            (assoc :session {:user-id user-id})))))) ;sets up new session, e.g. if there was any it is now overwritten

(defn logout-form []
  {:id "logout-form"
   :action (url "/logout")
   :submit-label (_t "Log out")})

(defn logout [request]
  "bumper page that shows a logout button. the button is pressed automatically when javascript is enabled.
  this lets us use POST for the actual logout preventing CSRF"
  (let [script "$(function() { $('#logout-form #field-submit').click(); });"]
    (main-response :title (_t "Log out") :content (render-form (logout-form)) :script script)))

(defn logout-action [request]
  (-> (redirect (url "/"))
      (assoc :session {}))) ;clear session, which contained the logged-in user-id

(defn index [request]
  (main-response :title (_t "Main") :content "Main Content"))

(comment

  (fp/parse-params (login-form) {} :validate true)

  (assoc-in {} [:session :member-id] 1234)

    (let [request {}]
        (-> (redirect (url "/"))
            (assoc :session (assoc (:session request) :member-id 1234))))


  )
