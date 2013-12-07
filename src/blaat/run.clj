(ns blaat.run
  (:require [blaat.app :as app])
  (:use [org.httpkit.server :only [run-server]]))


(def server (atom nil))

(defn start []
  (when-not @server
    (reset! server (run-server #'app/blaat-app {:port 8080 :join? false}))))

(defn stop []
  (when @server
    (@server)
    (reset! server nil)))

(comment

(start)

  (stop)
  @server


 )
