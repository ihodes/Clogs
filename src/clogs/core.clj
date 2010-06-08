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
(def *raw-posts* "resources/index.xml") ;; master index.xml file for newest posts
(def *raw-archive* "resources/archive.xml") ;; master archive.xml file for newest archive

(def *absolute-root* "") ;; root URL
(def *posts-folder* "/p/") ;; folder where permalinked files go

;;HTML files
(def *index* "resources/index.html") ;; index.html file location
(def *archive* "resources/archive.html") ;; archive.html file location

;; builds a post into *posts-folder*
;; processes the xml file adding a link to it to the new perma-post
(defn build-perma-post
  "Takes path to xml file and builds the permenant html file, and processes
the input xml file to add a permalink to it. Spits permafile into *posts*."
  [path name]
  (do (ducks/spit (str *posts-folder* name ".html")
                  (render/render
                   (render/render-post (parser/parse-post path))))
      (ducks/spit path
                  (str "<post>\n"
                       "    <perm-url>" *posts-folder* name
                       ".html</perm-url>\n" 
                       (apply str (drop 7 (slurp path)))))))
                    

;; builds the index from the *raw-posts* file and spits it to *index*
(defn build-index
  []
  (ducks/spit *index*
              (render/render
               (render/render-index (parser/assemble-map-posts *raw-posts*)))))

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

  
