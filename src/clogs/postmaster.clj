(ns clogs.postmaster
  (:use [clojure.java.io :as io]
        [clojure.java.io :only (reader)]))

(def *postbox* "resources/postbox.txt")

(defn add-to-postbox
  "Adds 'm to the end of the postbox."
  [m]
  (with-open [w (io/writer (io/file *postbox*)
                           :append true)]
    (spit w (str \newline m))))

;; lazy seq of post-maps in the postbox
(def posts
     (map read-string (line-seq (reader *postbox*))))

;; later: (find-post :date  "2010") (all posts with 2010 in the date) etc