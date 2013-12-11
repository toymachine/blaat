(ns blaat.user
  (:require [blaat.db :as db]
            [clj-bcrypt-wrapper.core :as crypt]))

(def ^:dynamic *logged-in-user* nil)

(defn logged-in-user []
  *logged-in-user*)

(defn logged-in-user? []
  (boolean (logged-in-user)))

(defn create-account [email password]
  ;;TODO validate email and password, catch unique email exception?
  (let [user-id (db/tempid :db.part/user)
        result  (db/transact [{:db/id user-id :account/email email}
                              {:db/id user-id :account/password (crypt/encrypt password)}])]
    (prn result)))

(defn get-user-id-by-email-and-password [email password]
  "returns user-id when account with email exists and given plaintext password is correct otherwise nil"
  (when (and (seq email) (seq password) (string? email) (string? password))
    (when-let [result
      (first (q '[:find ?c ?password
                  :in $ ?email
                  :where [?c account/email ?email]
                         [?c account/password ?password]] (current-db) email))]
        (let [[user-id encrypted-password] result]
          (when (crypt/check-password password encrypted-password)
            user-id)))))

(defn get-user-by-id [user-id]
  "returns the user entity by the given user-id or nil if it does not exist"
  (when user-id
    (let [db (current-db)
          exists? (ffirst (d/q '[:find ?e :in $ ?e :where [?e]] db user-id))]
      (when exists?
        (d/entity db user-id)))))

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
