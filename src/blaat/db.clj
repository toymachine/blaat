(ns blaat.db
  (:use [datomic.api :only [db q] :as d]))

(def db-uri "datomic:free://localhost:4334/blaat")

(defn conn []
 (d/connect db-uri))

(defn current-db []
  (db (conn)))

(defn make-db []
  (d/create-database db-uri))

(defn make-schema []
  (d/transact (conn) [{:db/id (d/tempid :db.part/db)
                       :db/ident :account/email
                       :db/unique :db.unique/value
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "An accounts email"
                       :db.install/_attribute :db.part/db}

                      {:db/id (d/tempid :db.part/db)
                       :db/ident :account/password
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "An accounts encrypted password"
                       :db.install/_attribute :db.part/db}
                      ]))

