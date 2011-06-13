(ns clogs.core
  (:require [clogs.publisher :as pub]))


(defn help
  "Prints out how to use Clogs."
  [] (prn "a post is a .md file entitled post.md in the postdir that you specify
 the first line of a post is a clojure map with any metadata you'd like
 the map must include keys: :title (str) :summary (str) :author (str)
 other keys used are :tags (str: comma delimited) :location (str) :linked-list (bool)

 To publish a post, (publish-post 'p) where p is the postdir. "))

(defn publish-post
  [postdir]
  (pub/publish-post postdir))

(defn delete-post
  [postdir]
  (pub/complete-delete-post postdir))