(ns clogs.core
  (:use net.cgrand.moustache
	ring.middleware.stacktrace
	ring.util.response
	ring.middleware.params
	[ring.adapter.jetty :only [run-jetty]])
  (:require [clogs.publisher :as pub]))


(declare clogs-app)

(def server (doto (Thread. #(run-jetty #'hn-srch-app {:port 8080})) .start))


;; routing
(def clogs-app
     )