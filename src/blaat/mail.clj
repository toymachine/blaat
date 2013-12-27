(ns blaat.mail
  (:require [postal.core :as p])
  (:use [clojure.core.async :only [thread]]))


(def aws_smtp_user "AKIAIXQHG6G37IOWE4OQ")
(def aws_smtp_password "Aie71HuqGN8dcaG03s3TGXR0srz80Pcv60kn/BT8OGC7")

;;TODO really queue mail, or at least have an idea how deep the mail queue is
(defn send-message [& {:keys [from to subject body] :or {from "noreply@blaat.com"
                                                         to "henkpunt@gmail.com"
                                                         subject ""
                                                         body ""}}]
  (thread
    (p/send-message ^{:user aws_smtp_user
                      :pass aws_smtp_password
                      :host "email-smtp.us-east-1.amazonaws.com"
                      :port 587}
                 {:from "henkpunt@gmail.com" :to "henkpunt@gmail.com" ;;TODO use actual from to
                  :subject subject :body body})))


(comment

      (send-message :subject "Please verify to enable your account"
                    :body "Please verify to enable your account")


  )

