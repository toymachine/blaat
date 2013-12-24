(ns blaat.user
  (:require [blaat.db :as db]
            [clj-bcrypt-wrapper.core :as crypt]
            [blaat.validate :as v])
  (:use [blaat.i18n]))


(def ^:dynamic *logged-in-user* nil)

(defn logged-in-user []
  *logged-in-user*)

(defn logged-in-user? []
  (boolean (logged-in-user)))


(defn ex-data* [ex]
 (if-let [data (ex-data ex)]
    data
    ;;else recurse/check cause
    (when-let [cause (.getCause ex)]
      (recur cause))))

(defn create-account [email password]

  ;;TODO validate email and password, catch unique email exception?
  (if-let [msg (v/validate-password password)]
    (throw (ex-info (_t "Account creation failed, invalid pasword") {:msg msg})))
  (if-let [msg (v/validate-email email)]
    (throw (ex-info (_t "Account creation failed, invalid email") {:msg msg})))

  (let [user-id (db/tempid :db.part/user)]
    (try
      (db/transact [{:db/id user-id :account/email email}
                    {:db/id user-id :account/password (crypt/encrypt password)}])

      (catch Exception ex
        (do
          (prn (ex-data* ex))
          (throw (ex-info (_t "Account creation failed") (ex-data* ex))))))))

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

  (create-account "harry3@potter.nl" "123456")

  (get-user-id-by-email-and-password "harry3@potter.nl" "123456")
  (get-user-id-by-email-and-password "harry4@potter.nl" "123456")

  17592186045418

    (get-in {:session {:user-id 1234}} [:session :user-id])

    (:account/password (get-user-by-id 17592186045418))

  (logged-in-user?)

  (q '[:find ?e
       :in $ ?user-id
       :where [?e db/id ?user-id]] (current-db) 12345)

  (crypt/compare "123457" (crypt/encrypt "123456"))

  (boolean "piet")

  )
