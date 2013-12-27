(ns blaat.url
  (:require [clj-time.core :as dt]
            [clj-time.coerce :as dt-coerce])
  (:use [ring.util.codec :only [url-encode url-decode]]
        [clojure.string :only [join split]]
        [blaat.util :only [encode-base64 hmac-sha1]]))

(def url-encoder (org.apache.commons.codec.net.URLCodec.))


(defn- encode-params [params]
  (for [[k v] params]
    [(.encode url-encoder (if (keyword? k) (name k) (str k))) (.encode url-encoder (str v))]))

(defn- build-query-str [params]
   (join "&" (for [[k v] (sort-by first (encode-params params))] (str k "=" v))))

(defn- signature [key message]
   (encode-base64 (hmac-sha1 key message)))

(defn query-str
  ([query-params key expires]
    (let [params (if expires
                   (assoc query-params :expires (.getMillis expires))
                   ;else
                   query-params)
          qs (build-query-str params)]
      (if key
        (str qs "&signature=" (signature key qs))
        ;else
        qs)))
  ([query-params key] (query-str query-params key false))
  ([query-params] (query-str query-params false false)))

(defn url
  ([path query-str] (str path "?" query-str))
  ([path] path))

(defn static-url [s]
  (str "/static" s))

(defn absolute-url
  ([path query-str] (str "https://www.blaat.com" (url path query-str)))
  ([path] (str "https://www.blaat.com" (url path))))

(defn valid-params? [key params]
  (if-let [given-signature (:signature params)]
    (let [qs (build-query-str (dissoc params :signature))
          calculated-signature (signature key qs)
          valid-signature? (= calculated-signature given-signature) ;;TODO prevent timing attack
          expires-dt (when-let [expires (:expires params)] (dt-coerce/from-long (Long/parseLong expires)))
          valid-expiry? (when expires-dt (dt/before? (dt/now) expires-dt))]
       (and valid-signature? valid-expiry?))))


(comment

  (url "/")

  (absolute-url "/piet" {:piet 10 :klaas "aap"})

    (sign-params {"piet klaas" "aap vaak"
                  "jaap piet" "blaat aap"})

  (dt/after? (dt/now) (dt-coerce/from-long (Long/parseLong "1388177929815")))

  (absolute-url "/aap" (query-str {"piet klaas" "aap vaak"
                        "jaap piet" "blaat aap"} "sdfasdfasd" (-> 15 dt/minutes dt/from-now)))

    (-> 15 dt/minutes dt/from-now)


  )
