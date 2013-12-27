(ns blaat.url
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
  ([query-params key]
    (let [qs (join "&" (for [[k v] (sort-by first (encode-params query-params))] (str k "=" v)))]
      (if key
        (str qs "&signature=" (encode-base64 (hmac-sha1 key qs)))
        ;else
        qs)))
  ([query-params] (query-str query-params false)))

(defn url
  ([path query-params] (str path "?" (query-str query-params)))
  ([path] path))

(defn absolute-url
  ([path query-params] (str "https://192.168.33.10" (url path query-params)))
  ([path] (str "https://192.168.33.10" (url path))))



(comment

  (url "/")

  (absolute-url "/piet" {:piet 10 :klaas "aap"})

    (sign-params {"piet klaas" "aap vaak"
                  "jaap piet" "blaat aap"})


  (query-str {"piet klaas" "aap vaak"
              "jaap piet" "blaat aap"} "sdfasdfasd")

  )
