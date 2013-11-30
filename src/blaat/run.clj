(ns blaat.run
  (:require [blaat.app :as app])
  (:use [net.cgrand.moustache]
        [ring.adapter.jetty :only [run-jetty]]))

(defn start [] (run-jetty #'app/blaat-app {:port 8080 :join? false}))


(comment

  (start)

  )
