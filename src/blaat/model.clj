(ns blaat.model
  (:use [blaat.db]
        [datomic.api :only [db q] :as d])
  (:require [noir.util.crypt :as crypt]))

(defn create-account [email password]
  ;;TODO validate email and password
  (let [member-id (d/tempid :db.part/user)]
    (d/transact (conn) [{:db/id member-id :account/email email}
                        {:db/id member-id :account/password (crypt/encrypt password)}])))

(defn get-member-id-by-email-and-password [email password]
  "returns member-id when account with email exists and given plaintext password is correct otherwise nil"
  (when-let [result
    (first (q '[:find ?c ?password
                 :in $ ?email
                 :where [?c account/email ?email]
                        [?c account/password ?password]] (current-db) email))]
      (let [[member-id encrypted-password] result]
        (when (crypt/compare password encrypted-password)
          member-id))))

(comment

  (create-account "harry3@potter.nl" "123456")

  (get-member-id-by-email-and-password "harry3@potter.nl" "123456")
  (get-member-id-by-email-and-password "harry4@potter.nl" "123456")

  (crypt/compare "123457" (crypt/encrypt "123456"))

  )
