(ns blaat.db
  (:use [datomic.api :only [db q] :as d]))

(def db-uri-development "datomic:free://localhost:4334/blaat")

(def ^:dynamic *db-uri* db-uri-development)

(def tempid d/tempid)

(defn connect []
 (d/connect *db-uri*))

(def ^:dynamic *current-db* nil)

(def ^:dynamic *basis-ts* nil) ;list of db-after basis-t's for this requests transactions

(defn current-db []
  *current-db*)

(defn wrap-db [app]
  "ring middleware to set the current db once per request, so that it is consistent during the request
  also to sync db so that user who posted stuff gets to see his own stuff when connecting to a random peer
  (avoids problems due to slave delay)"
  (fn [request]
    (let [basis-t (get-in request [:session :basis-t]) ;;check if any basis-t
          connection (connect)]
      (binding [*current-db*
                  (if basis-t
                    ;;wait till this peer (slave is up to date wrt the basis) (TODO timeout)
                    @(d/sync connection basis-t)
                    ;;else just get the latest available version
                    (db connection))
                *basis-ts* (atom [])] ;;set up list of basis-ts to gather during this request
        (let [response (app request)
              session (:session request)
              basis-ts @*basis-ts*
              max-basis-t (when (seq basis-ts) (apply max basis-ts))] ;;if any transaction occured, find the max basis-t
          (if max-basis-t
            (assoc-in response [:session :basis-t] max-basis-t) ;;if any max basis-t put it in the session
            ;;else
            response))))))

(defn transact [tx-data]
  (let [result @(d/transact (connect) tx-data)
        basis-t (d/basis-t (:db-after result))]
    (swap! *basis-ts* conj basis-t)
    result))

(defn make-db []
  (d/create-database *db-uri*))

(defn make-schema []
  (d/transact (connect) [{:db/id (d/tempid :db.part/db)
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

(comment

  (make-db)

  (apply max [1 2 3 4])

  (make-schema)

  (conn)

  (d/sync)



  (assoc-in {:body "Aap" :session {:blaat 20}} [:session :basis-t] 1024)

  )
