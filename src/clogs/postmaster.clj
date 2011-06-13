(ns clogs.postmaster
  (:use [clojure.java.io :as io]
        [clojure.java.io :only (reader)]))

;; a post is a .md file entitled post.md in the postdir that you specify
;; the first line of a post is a clojure map with any metadata you'd like
;; the map must include keys: :title (str) :summary (str) :author (str)
;; other keys used are :tags (str: comma delimited) :location (str) :linked-list (bool)

(def *postbox* "resources/postbox.txt")

(defn pb-add-to-postbox
  "Adds 'm to the end of the postbox."
  [m]
  (with-open [w (io/writer (io/file *postbox*)
                           :append true)]
    (spit w (str m \newline))))

(defn pb-new-postbox
  "Empties the postbox and replaces all posts with postmaps in 'pvec."
  [pvec]
  (let [_ (spit *postbox* nil)]
    (for [p pvec]
      (if (not (nil? p)) (pb-add-to-postbox p)))))

(defn pb-posts
  "Returns a seq of post maps."
  []
  (map read-string (line-seq (reader *postbox*))))

(defn pb-edit-post
  "Removes the postmap with postdir='pd, and replaces it with 'm. If 'm is nil, then the post is deleted"
  [pd m]
  (pb-new-postbox
   (for [p (pb-posts)]
     (cond (= pd (:postdir p)) m
	   :else p))))

(defn pb-delete-post
  "Removes the post with :postdir pd from the postbox."
  [pd]
  (pb-edit-post pd nil))

;; later: (find-post :date  "2010") (all posts with 2010 in the date) etc