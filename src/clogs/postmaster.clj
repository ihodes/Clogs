(ns clogs.postmaster
  (:use [clojure.contrib.io :only (append-spit)]
        [clojure.java.io :only (reader)]))

(def *postbox* "resources/postbox.txt")

(defn add-to-postbox
  "Adds 'm to the end of the postbox."
  [m]
  (append-spit *postbox* (str \newline m)))

;; lazy seq of post-maps in the postbox
(def posts
     (map #(do (println "hey") (read-string %)) (line-seq (reader *postbox*))))

;; later: (find-post :date  "2010") (all posts with 2010 in the date) etc