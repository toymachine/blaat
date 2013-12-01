(ns blaat.app
  (:require [blaat.handler :as handler]
            [blaat.model :as model])
  (:use [net.cgrand.moustache]
        [ring.middleware.resource]
        [ring.middleware.file-info]
        [ring.middleware.params]
        [ring.middleware.keyword-params]
        [ring.middleware.flash]
        [ring.middleware.stacktrace]
        [ring.middleware.session]
        [ring.middleware.session.cookie]))

(def production?
  (= "production" (get (System/getenv) "APP_ENV")))

(def development?
  (not production?))


(def blaat-app
    (app
       (wrap-stacktrace)
       (wrap-resource "public")
       (wrap-file-info)
       (wrap-params)
       (wrap-keyword-params)
       (wrap-session {:store (cookie-store {:key "sdfnOIU&!#kHJBMN"})})
       (wrap-flash)
       [""] {:get handler/main}
       ["login"] {:get handler/login
                  :post handler/login-action}
       ["account" "create"] {:get handler/create-account
                             :post handler/create-account-action}
     ))

(comment

  (blaat-app {:url "/login"})

  )

