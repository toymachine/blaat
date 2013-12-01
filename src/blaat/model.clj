(ns blaat.model
  (:use [blaat.db]
        [cemerick.friend.credentials :only [hash-bcrypt]]
        [datomic.api :only [db q] :as d]))

(defn create-account [email password]
  ;;TODO validate email and password
  (let [member-id (d/tempid :db.part/user)]
    (d/transact (conn) [{:db/id member-id :account/email email}
                        {:db/id member-id :account/password (hash-bcrypt password)}])))

(defn get-account-by-email [email]
  (q '[:find ?password
       :in $ ?email
       :where [?c account/email ?email]
              [?c account/password ?password]] (db (conn)) email))

(comment

  (create-account "harry2@potter.nl" "123456")

  (get-account-by-email "harry2@potter.nl")

  )
