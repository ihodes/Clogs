(ns clogs.core
  (:import (java.util Date)
           (java.net URL))
  (:use compojure.core
        ring.adapter.jetty)
  (:require [compojure.route :as route]
            [net.cgrand.enlive-html :as html]
            [clj-time.core :as clj-time]
            [clj-time.format :as clj-time-fmt]
            [clj-time.coerce :as time-coerce]
            [clogs.parser :as parser]
            [clogs.render :as render]
            [clojure.contrib.duck-streams :as ducks]))

;;XML files
(def *raw-index* "resources/index.xml") ;; master index.xml file for newest posts
(def *raw-archive* "resources/archive.xml") ;; master archive.xml file for newest archive

(def *absolute-root* "") ;; root URL
(def *posts-folder* "/p/") ;; folder where permalinked files go
(def *raw-posts-folder* "/p/posts/") ;; folder where XML posts go

;;HTML files
(def *index* "resources/index.html") ;; index.html file location
(def *archive* "resources/archive.html") ;; archive.html file location

;; basic date parser (gets Date from a string required in posting)
(def clg-time-fmt (clj-time-fmt/formatter "dd-MM-yyyy"))

(defn init-post
  "Initializes a post from a map to a post.xml XML file.

   perm-url.xml is put into *raw-posts-folder*/year/

   example:
      (init-post {:title \"A Post\" :author \"Isaac Hodes\"
                  :content \"Some escaped html content here,
                  for the main post.\"
                  :clgdate \"09-23-2009\"
                  :perm-url \"a_nice_url_name\"})

   :clgdate must be in the form of MM-DD-YYYY.
   :perm-url will be the name of the file, both XML and HTML.
   "
  [post-map]
  (let [year (.getYear (parse clg-time-fmt (:clgdate post-map)))]         ;; parses the year from the date string
    (ducks/spit (str *raw-posts-folder* year (:perm-url post-map) ".xml") ;; path to post
                (render/render (render/render-raw-post post-map)))))      ;; renders XML file

;; inserts a post into the *raw-index* file (prepends)
(defn prepend-post-raw-index
  "Prepends the XML file at `path` to  *raw-index*."
  [path]
  (let [post (slurp path)]
    (spit *raw-index*
          (apply str (html/emit*
                      (html/at (first (html/html-resource *raw-index*)) ;; at the raw index-file
                          [:post] (html/before post))))))) ;; sticks the post before the first post

;; removes the last raw post in the *raw-index*
(defn pop-raw-post
  "Removes (pops) the last post from *raw-index*."
  []
  

  
;; builds the index from the *raw-index* file and spits it to *index*
(defn build-index
  []
  (ducks/spit *index*
              (render/render (render/render-index
                              (parser/assemble-map-posts *raw-posts*)))))

;; builds the archive from the *raw-archive* file and spits it to *archive*
(defn build-archive
  []
  (ducks/spit *archive*
              (render/render
               (render/render-archive (parser/assemble-map-posts *raw-archive*)))))

;; Main process used to coordinate all post-parsing and page-rendering actions.
;;
;; (defn push-post-to-xml
;;      "Takes a path to a XML post and prepends it to the *posts* file."
;;      [path]
;;      (let [post-map (parser/parse-post path)]
;;        ));;;;;;;;;;;WORKING HERE
       
;; (defn pop-post-from-xml
;;   "Pops a post from the end of the posts.xml file."
;;   ());;working here

  
