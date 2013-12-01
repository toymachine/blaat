(ns blaat.util)

(def alphabet (map char (concat (range 48 58) (range 66 92) (range 97 123))))

(defn rand-string [n]
    (apply str (repeatedly n #(rand-nth alphabet))))

(comment

  (apply str (take 10 (repeatedly (rand-nth chars))))

  (apply str (repeatedly 10 #(rand-nth alphabet)))

  (repeatedly rand-nth alphabet)

  (rand-string 20)

  )
