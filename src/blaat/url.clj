(ns blaat.url)

(defn static-url [s]
  (str "/static" s))

(defn dyn-url [s]
  s)
