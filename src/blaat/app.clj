(ns blaat.app
  (:require [blaat.handler :as handler])
  (:use [net.cgrand.moustache]
        [ring.middleware.resource]
        [ring.middleware.file-info]))

(def production?
  (= "production" (get (System/getenv) "APP_ENV")))

(def development?
  (not production?))



(def blaat-app
    (app
       (wrap-resource "public")
       (wrap-file-info)
       [""] {:get handler/main}))

(comment

  )

