(ns blaat.user
  (:require [blaat.db :as db]
            [clj-bcrypt-wrapper.core :as crypt]
            [blaat.validate :as v]
            [blaat.mail :as mail]
            [blaat.url :as url]
            [blaat.secret :as secret]
            [clj-time.core :as dt])
  (:use [blaat.i18n :only [_t]]))



(def ^:dynamic *logged-in-user* nil)

(defn logged-in-user []
  *logged-in-user*)

(defn logged-in-user? []
  (boolean (logged-in-user)))


(defn- send-email-validation-mail [user-id user-email]
   (let [email-validation-url
          (url/absolute-url "/validate-account"
          (url/query-str {:user-id user-id} secret/email-validation (-> 15 dt/minutes dt/from-now)))]
    (mail/send-message :to user-email
                       :subject (_t "Please verify to enable your account")
                       :body (str (_t "Please click the following link to verify your account: ") email-validation-url))))

(defn create-account [name email password]
  "creates an account with given name, email (pk) and password"
  (when-not (seq name)
    (throw (ex-info (_t "Account creation failed, no name given"))))
  (when-let [msg (v/validate-password password)]
    (throw (ex-info (_t "Account creation failed, invalid pasword") {:msg msg})))
  (when-let [msg (v/validate-email email)]
    (throw (ex-info (_t "Account creation failed, invalid email") {:msg msg})))

  (let [tmp-user-id (db/tempid :db.part/user)
        tx-result
              (db/transact [{:db/id tmp-user-id :user/name name}
                            {:db/id tmp-user-id :account/state :pending}
                            {:db/id tmp-user-id :account/email email}
                            {:db/id tmp-user-id :account/password (crypt/encrypt password)}
                            {:db/id tmp-user-id :account/created-at (java.util.Date.)}])

        user-id (db/resolve-tempid tx-result tmp-user-id)]

       (when user-id (send-email-validation-mail user-id email))

        user-id))

(defn validate-account [user-id]
  (when-not (number? user-id)
    (throw (ex-info (_t "Validate account failed, no user-id given")))

  (db/transact [{:db/id user-id :account/state :active}])))


(defn get-user-id-by-email-and-password [email password]
  "returns user-id when account with email exists and given plaintext password is correct otherwise nil"
  (when (and (seq email) (seq password) (string? email) (string? password))
    (when-let [result
      (first (db/q '[:find ?c ?password
                     :in $ ?email
                     :where [?c account/email ?email]
                            [?c account/password ?password]] email))]
        (let [[user-id encrypted-password] result]
          (when (crypt/check-password password encrypted-password)
            user-id)))))

(defn get-user-by-email
  "returns the user entity by the given email or nil if it does not exist"
  [email]
    (when email
      (when-let [user-id
         (ffirst (db/q '[:find ?user-id
                         :in $ ?email
                         :where [?user-id account/email ?email]] email))]
            (db/entity user-id))))

(defn get-user-by-id
  "returns the user entity by the given user-id or nil if it does not exist"
  [user-id]
    (when user-id
      (when (ffirst (db/q '[:find ?e :in $ ?e :where [?e]] user-id))
        (db/entity user-id))))

(defn wrap-logged-in-user [app]
  "ring middleware to set logged in user var when logged in user is present in session"
  (fn [request]
    (let [user-id (get-in request [:session :user-id])
          user (when user-id (get-user-by-id user-id))]
      (binding [*logged-in-user* user]
        (app request)))))


(defn user-name [user]
  (if (seq (:user/name user))
    (:user/name user)
    (:account/email user)))

(comment


  (get-user-by-email "")

  (create-account "harry3@potter.nl" "123456")

  (get-user-id-by-email-and-password "henk@aap.nl" "123456")
  (get-user-id-by-email-and-password "harry4@potter.nl" "123456")

  17592186045418

    (get-in {:session {:user-id 1234}} [:session :user-id])

    (:account/password (get-user-by-id 17592186045418))

  (logged-in-user?)

  (validate-account (create-account "Harry" "harrfdsfdx4fas89fb@potter.nl" "P4fadsf"))


   (number? nil)

  (send-email-validation-mail 12345 "henkpunt@gmail.com")

  (boolean "piet")

  )
