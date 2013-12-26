(ns blaat.mail
 (:require [postal.core :as p]))


(def aws_smtp_user "AKIAIXQHG6G37IOWE4OQ")
(def aws_smtp_password "Aie71HuqGN8dcaG03s3TGXR0srz80Pcv60kn/BT8OGC7")

(defn send-message [& {:keys [from to subject body] :or {from "noreply@blaat.com"
                                                         to "henkpunt@gmail.com"
                                                         subject ""
                                                         body ""}}]
    (p/send-message ^{:user aws_smtp_user
                      :pass aws_smtp_password
                      :host "email-smtp.us-east-1.amazonaws.com"
                      :port 587}
                 {:from "henkpunt@gmail.com" :to "henkpunt@gmail.com" ;;TODO use actual from to
                  :subject subject :body body}))


(comment

  (p/send-message ^{:user aws_smtp_user
                  :pass aws_smtp_password
                  :host "email-smtp.us-east-1.amazonaws.com"
                  :port 587}
                 {:from "henkpunt@gmail.com" :to "henkpunt@gmail.com"
                  :subject "Test from Amazon SES" :body "Test!!!2"})


        (send-message :subject "Please verify to enable your account"
                      :body "Please verify to enable your account")


  )

