(ns blaat.app
  (:require [blaat.handler :as handler]
            [blaat.model :as model]
            [blaat.cache :as cache]
            [clojurewerkz.spyglass.client :as c])
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

(def memcached-client (c/bin-connection "localhost:11211"))

(defn wrap-setup [app]
  (fn [req]
    (binding [cache/*client* memcached-client]
      (app req))))

(def blaat-app
    (app
       (wrap-setup)
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

