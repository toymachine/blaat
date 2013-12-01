(ns blaat.core
  (:use [datomic.api :only [db q] :as d]
        [clojure.string :only [join]]))

;(def db-uri "datomic:ddb://eu-west-1/blaat/mydbname?aws_access_key_id=AKIAJUVWVJEDWM7DKK2A&aws_secret_key=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")
(def db-uri "datomic:free://localhost:4334/blaat")

(defn conn []
 (d/connect db-uri))

(defn make-db []
  (d/create-database db-uri))

(def alpha "abcdefghijklmnopqrstuvw")

(defn rand-name [n]
   (join
      (for [i (range n)] (.charAt alpha (rand-int (.length alpha))))
    ))

(defn add-person-attribute []
  (d/transact (conn) [{:db/id (d/tempid :db.part/db)
                       :db/ident :person/name
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "A person's name"
                       :db.install/_attribute :db.part/db}]))

(defn add-a-person [name]
  (d/transact (conn) [{:db/id (d/tempid :db.part/user) :person/name name}]))

(defn get-all-people []
  (q '[:find ?n :where [?c person/name ?n ]] (db (conn))))

(defn -main []
        (prn (get-all-people)))

(comment

(rand-name 10)
(add-person-attribute)
(get-all-people)
(add-a-person "Henk")
(add-a-person "Klaas") 
)
;read/write troughput: 10/5
;clojure.core=> (time (dotimes [n 1000] (add-a-person (rand-name 10) cnn)))
;"Elapsed time: 110946.795964 msecs", Storage Size (in bytes)*: 225976

;read/write troughput: 40/20
;blaat.core=> (time (dotimes [n 1000] (add-a-person (rand-name 10) cnn)))
;'"Elapsed time: 33606.268777 msecs"
;Storage Size (in bytes)*:
;494499
;Item Count*:
;2068

;read/write troughput: 80/40
;blaat.core=> (time (dotimes [n 1000] (add-a-person (rand-name 10) cnn)))
;"Elapsed time: 31743.701813 msecs"
; not getting faster , network bound?
;blaat.core=> (time (dotimes [n 1000] (add-a-person (rand-name 10) cnn)))
;"Elapsed time: 24321.453491 msecs"

;read/write troughput: 80/80
;blaat.core=> (time (dotimes [n 1000] (add-a-person (rand-name 10) cnn)))
;"Elapsed time: 21160.990606 msecs"

; new instance type m3.2xlarge (network high bandwidth)
;blaat.core=> (time (dotimes [n 1000] (add-a-person (rand-name 10) cnn)))
;"Elapsed time: 16900.186572 msecs"
;
;blaat.core=> (time (dotimes [n 1000] (add-a-person (rand-name 10) cnn)))
;"Elapsed time: 18417.022658 msecs"
