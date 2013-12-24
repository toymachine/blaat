(ns blaat.db
  (:require [datomic.api :as d]))

(def db-uri-development "datomic:free://localhost:4334/blaat")

(def ^:dynamic *db-uri* db-uri-development)

(def tempid d/tempid)

(defn connect []
 (d/connect *db-uri*))


(def ^:dynamic *current-db* nil)

(def ^:dynamic *basis-ts* nil) ;list of db-after basis-t's for this requests transactions

(defn current []
  (or *current-db* (d/db (connect))))

(defn q [qry & inputs]
  (apply d/q qry (current) inputs))

(defn entity [entity-id]
  (d/entity (current) entity-id))

(defn wrap-db [app]
  "ring middleware to set the current db once per request, so that it is consistent during the request
  also to sync db so that user who posted stuff gets to see his own stuff when connecting to a random peer
  (avoids problems due to slave delay)"
  (fn [request]
    (let [request-session (:session request)
          basis-t (:basis-t request-session) ;;check if any basis-t
          connection (connect)]

      (binding [*current-db*
                  (if basis-t
                    ;;there is a basit-t, wait till this peer/slave is up to date wrt the given basis-t but only wait for 1000ms
                    (if-let [current-db (deref (d/sync connection basis-t) 1000 nil)]
                      current-db
                      ;;timeout on the sync, just get the lates available then
                      (d/db connection))
                    ;;no basis-t just get the latest available version
                    (d/db connection))
                *basis-ts* (atom [])] ;;set up list of basis-ts to gather during this request

        (let [response (app request) ;;<--- generate response

              response-session (:session response)
              basis-ts @*basis-ts*]
          (if-let [new-basis-t (when (seq basis-ts) (apply max basis-ts))]
            ;;if any transaction occured, we have new basis-t and we put it in the session to be found on next request
            (assoc response :session (assoc (or response-session request-session) :basis-t new-basis-t))
            ;;else no new basis-t:
            (if response-session
              ;;updated session in response, we remove any basis-t from it if any
              (assoc response :session (dissoc response-session :basis-t))
              ;;no response session
              (if basis-t
                ;;but there was basis-t in request session, remove it
                (assoc response :session (dissoc request-session :basis-t))
                ;;no basis-t, no response-session, just normal response
                response))))))))





(defn transact [tx-data]
  (let [result @(d/transact (connect) tx-data)
        basis-t (d/basis-t (:db-after result))]
    (swap! *basis-ts* conj basis-t)
    result))

(defn create-db []
  (when (d/create-database *db-uri*)
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
                            ])
    (System/exit 0)))

(defn delete-db []
  (d/delete-database *db-uri*)
  (System/exit 0))

(comment

  (make-db)

  (apply max [1 2 3 4])

  (make-schema)

  (conn)

  (d/sync)

  (or {:piet "aap"} {:blaat "klaas"})

  (:basis-t nil)

  (assoc nil :piet "klaas")

  (dissoc {} :piet)

  (assoc-in {:body "Aap" :session {:blaat 20}} [:session :basis-t] 1024)

  )
