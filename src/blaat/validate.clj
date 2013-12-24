(ns blaat.validate
  (:use [blaat.i18n]))

(defn first-failed [validations]
  (first (for [[x y] validations :when x] y)))

(defn validate-password [password]
  (let [cnt #(count (filter % password))
        validations
           [[(not (seq password)) (_t "Password cannot be empty")]
            [(< (count password) 6) (_t "Password must contain at least 6 characters")]
            [(> (count password) 32) (_t "Password can have a most 32 characters")]
            [(< (cnt #(Character/isUpperCase %)) 1) (_t "Password must have at least 1 uppercase character")]
            [(< (cnt #(Character/isLowerCase %)) 1) (_t "Password must have at least 1 lowercase character")]
            [(< (cnt #(Character/isDigit %)) 1) (_t "Password must contain at least 1 digit")]]]
    (first-failed validations)))

(defn validate-email [email]
  (if (not (seq email))
    (_t "Email cannot be empty")
      (if (not (re-matches #"[^^]+@[^$]+" email))
        (_t "Invalid email"))))


(comment

  (validate-email "aap@piet.nl")

  )
