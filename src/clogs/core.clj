(ns clogs.core
  (:import (java.io File)
           (java.util Date)
           (java.net URL))
  (:use compojure.core
        ring.adapter.jetty
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers
        clojure.xml
        [clojure.contrib.def :only (defvar-)])
  (:require [compojure.route :as route]
            [net.cgrand.enlive-html :as html]))

;; Directory where all the posts are kept (organized by year)
;; eg /home/user/posts
;; and then posts would be organized like:
;; ~/posts/1995/post_of_something.markdown
(defvar- *post-dir* "/Users/ihodes/clogs/posts")

;; Simple URL fetch
(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn hn-headlines []
    (map html/text (html/select (fetch-url ) [:td.title :a])))

;; Returns the publish date of a post (file)
;; format of date: month/day/year [xx/xx/xxxx]
;; format of output: java.util.Date
(defn get-pub-date
  [post]
  (map html/text (html/select (fetch-url post) [:span#pubdate])))

;; Returns list of n newest posts in dir of form dir/year/posts.markdown
;; Starts with newest year first: older year directories aren't
;; searched if n posts are found in years before it.
(defn find-n-newest-posts
  [dir n]
  (file-seq (java.io.File. dir)))

;; Filler function: needs to replace with Enlive stuff later.
;; 
;; PLAN: (TODO)
;; Will pass in list of n posts (java.io.File) and parse them
;; and output index.html to a file when done. [side effect]
(defn render-new-index
     [msg]
     (html (doctype :html5) [:html [:body msg]]))

;; Moves the new_index.html file into the index.html
;; file; used after render-new-index is done.
(defn refresh-index-files
  []
  (println "Done"))

;; Routing function
;; add in routes like <(GET "\about" (slurp "path/to/about.html"))> as needed
;; 
;; TODO: add in regex to validate paths, and redirect certain failed routes
(defroutes clogs
  (GET "/" [] (slurp "/Users/ihodes/clogs/html/index.html"))
  (GET "/:year/:name" [name] (render-new-index name))
  (GET "/:something" [something] (str "\"" something "\" is not a valid URI here"))
  (route/not-found "Page not found: please go back home."))

(run-jetty clogs {:port 8080})