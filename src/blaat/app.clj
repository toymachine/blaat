(ns blaat.app
  (:require [blaat.handler :as handler]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as creds])
  (:use [net.cgrand.moustache]
        [ring.middleware.resource]
        [ring.middleware.file-info]
        [ring.middleware.keyword-params]))

(def production?
  (= "production" (get (System/getenv) "APP_ENV")))

(def development?
  (not production?))


(def credential-fn [])

(def blaat-app
    (app
       (wrap-resource "public")
       (wrap-file-info)
       (friend/authenticate {:credential-fn credential-fn
                             :workflows [(workflows/interactive-form)]})
       (wrap-keyword-params)
       [""] {:get handler/main}
       ["login"] {:get handler/login}
       ["account" "create"] {:get handler/create-account
                             :post handler/create-account-action}
     ))

(comment

  )

