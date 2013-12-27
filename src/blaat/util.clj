(ns blaat.util)

(def alphabet (map char (concat (range 48 58) (range 66 92) (range 97 123))))

(defn rand-string [n]
    (apply str (repeatedly n #(rand-nth alphabet))))

(defn encode-base64
  ([data]
    (org.apache.commons.codec.binary.Base64/encodeBase64URLSafeString data)))

(defn hmac-sha1 [key message]
  (let [key-spec (javax.crypto.spec.SecretKeySpec. (.getBytes key) "HmacSHA1")
        mac (javax.crypto.Mac/getInstance "HmacSHA1")]
        (.init mac key-spec)
    (let [raw-hmac (.doFinal mac (.getBytes message))]
      raw-hmac)))

(comment

  (encode-base64 (hmac-sha1 "abcdefg" "blaataap3"))

  (apply str (take 10 (repeatedly (rand-nth chars))))

  (apply str (repeatedly 10 #(rand-nth alphabet)))

  (repeatedly rand-nth alphabet)

  (rand-string 20)

  )
