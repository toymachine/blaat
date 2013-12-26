(ns blaat.mail
 (:require [postal.core :as p]))


(def aws_smtp_user "AKIAIXQHG6G37IOWE4OQ")
(def aws_smtp_password "Aie71HuqGN8dcaG03s3TGXR0srz80Pcv60kn/BT8OGC7")

(comment

  (p/send-message ^{:user aws_smtp_user
                  :pass aws_smtp_password
                  :host "email-smtp.us-east-1.amazonaws.com"
                  :port 587}
                 {:from "henkpunt@gmail.com" :to "henkpunt@gmail.com"
                  :subject "Test from Amazon SES" :body "Test!!!2"})


  )

