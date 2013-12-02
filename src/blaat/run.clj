(ns blaat.run
  (:require [blaat.app :as app])
  (:use [ring.adapter.jetty :only [run-jetty]]))


(def server (atom nil))

(defn start []
  (when-not @server
    (reset! server (run-jetty #'app/blaat-app {:port 8080 :join? false}))))

(defn stop []
  (when @server
    (.stop @server)
    (reset! server nil)))

(comment

(start)

  (stop)
  @server


 )
