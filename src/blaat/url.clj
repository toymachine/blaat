(ns blaat.url
  (:require [clj-time.core :as dt])
  (:use [ring.util.codec :only [url-encode url-decode]]
        [clojure.string :only [join split]]
        [blaat.util :only [encode-base64 hmac-sha1]]))

(def url-encoder (org.apache.commons.codec.net.URLCodec.))

(defn static-url [s]
  (str "/static" s))

(defn- encode-params [params]
  (for [[k v] params]
    [(.encode url-encoder (if (keyword? k) (name k) (str k))) (.encode url-encoder (str v))]))

(defn query-str
  ([query-params key expires]
    (let [params (if expires
                   (assoc query-params :expires (.getMillis expires))
                   ;else
                   query-params)
          qs (join "&" (for [[k v] (sort-by first (encode-params params))] (str k "=" v)))]
      (if key
        (str qs "&signature=" (encode-base64 (hmac-sha1 key qs)))
        ;else
        qs)))
  ([query-params key] (query-str query-params key false))
  ([query-params] (query-str query-params false false)))

(defn url
  ([path query-str] (str path "?" query-str))
  ([path] path))

(defn absolute-url
  ([path query-str] (str "https://www.blaat.com" (url path query-str)))
  ([path] (str "https://www.blaat.com" (url path))))



(comment

  (url "/")

  (absolute-url "/piet" {:piet 10 :klaas "aap"})

    (sign-params {"piet klaas" "aap vaak"
                  "jaap piet" "blaat aap"})


  (absolute-url "/aap" (query-str {"piet klaas" "aap vaak"
                        "jaap piet" "blaat aap"} "sdfasdfasd" (-> 15 dt/minutes dt/from-now)))

    (-> 15 dt/minutes dt/from-now)


  )
