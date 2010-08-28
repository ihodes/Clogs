(ns clogs.core
  (:import (java.util Date)
           (java.net URL))
  (:require [net.cgrand.enlive-html :as html]
            [clj-time.core :as clj-time]
            [clj-time.format :as clj-time-fmt]
            [clj-time.coerce :as time-coerce]
            [clogs.parser :as parser]
            [clogs.render :as render]))

;; Folders
(def *absolute-root* "resources/") ;; root URL
(def *posts-folder* (str *absolute-root* "p/")) ;; folder where permalinked posts go
(def *raw-posts-folder* (str *absolute-root* "p/posts/")) ;; folder where XML posts go

;; XML files
(def *raw-index* (str *absolute-root* "index.xml")) ;; master index.xml file for newest posts
(def *raw-archive* (str *absolute-root* "archive.xml")) ;; master archive.xml file for newest archive

;; HTML files
(def *index* (str *absolute-root* "index.html")) ;; index.html file location
(def *archives* (str *absolute-root* "archives.html")) ;; archive.html file location

;; basic date format (for getting date from XML)
(def clg-time-fmt (clj-time-fmt/formatter "dd-MM-yyyy"))
(defn basic-clgdate-formatter
  [date]
  (let [y (clj-time/year (clj-time-fmt/parse clg-time-fmt date))
        m (clj-time/month (clj-time-fmt/parse clg-time-fmt date))
        d (clj-time/day (clj-time-fmt/parse clg-time-fmt date))]
    (str m "." d "." y)))
;; generic transform that does nothing cool now.
;; eg MM-DD-YYYY -> MM/DD, YYYY
(def txclgdate basic-clgdate-formatter)

 
(defn escape-html
  "Takes in a string of HTML content and makes it safe for storing
   in an XML file. e.g. > becomes &gt;, < becomes &lt;."
  [htmlstring]
  (-> htmlstring str
      (.replace "&" "&amp;") (.replace "<" "&lt;") (.replace ">" "&gt;") (.replace "\"" "&quot;")))
;; kindly borrowed from cgrand


(def p {:author "Isaac A. Hodes" :content "Here is my post, and god saw it was good. Amen"
        :title "The holy Post" :clgdate-norm "14-02-2010" :perm-url "the_holy_post"})



;; TODO: handle creating year/ directory if it doesn't already exit
(defn init-post
  "Initializes a post from a map to a post.xml XML file.

   perm-url.xml is put into *raw-posts-folder*/year/

   example:
      (init-post {:title \"A Post\" :author \"Isaac Hodes\"
                  :content \"Some escaped html content here,
                  for the main post.\"
                  :clgdate-norm \"23-09-2009\"
                  :perm-url \"a_nice_url_name\"})

   :clgdate-norm must be in the form of DD-MM-YYYY.
   :perm-url will be the name of the file, both XML and HTML.
   "
  [post-map]
  (let [year (.getYear (clj-time-fmt/parse clg-time-fmt (:clgdate-norm post-map)))
        s (. java.io.File separator)]         ;; parses the year from the date string
    (do
      (spit (str *raw-posts-folder* year s (:perm-url post-map) ".xml") ;; path to post
                  (render/render (render/render-raw-post post-map)))
      (println (str *raw-posts-folder* year s (:perm-url post-map) ".xml")))));; renders XML file

(defn build-post
  "Spits out a post into *posts-folder*/year/name.html.

   Also does some basic transformations... not sure they should go here.
   Right now just merges in a formatted date (adds the :clgdate key)
   formatted by the function specified by txclgdate.
    
   Takes in path to a post.xml."
  [path]
  (let [post-map (parser/parse-post path) ;;grabbing postmap from the xml post file
        year (.getYear (clj-time-fmt/parse clg-time-fmt (:clgdate-norm post-map)))
        s (. java.io.File separator)
        clgdate (txclgdate (:clgdate-norm post-map)) ;;transform clgdate-norm into a nice-to-read form
        post-map (merge {:clgdate clgdate} post-map)] ;; stick it into the postmap
    (spit (str *posts-folder* year s (:perm-url post-map) ".html")
                (render/render (render/render-post post-map)))))

(defn prepend-raw-post-a
  "Prepends the XML file at `path` to *raw-archive*."
  [path])
  

(defn prepend-raw-post-i
  "Prepends the XML file at `path` to  *raw-index*."
  [path]
  (let [post (slurp path)]
    (spit *raw-index*
          (apply str (html/emit*
                      (html/at (first (html/html-resource *raw-index*)) ;; at the raw index-file
                          [:post] (html/before post))))))) ;; sticks the post before the first post

(defn pop-raw-post-i
  "Removes (pops) the last post from *raw-index*."
  []
  (spit *raw-index*
        (apply str (html/emit*
                    (html/at (first (html/html-resource *raw-index*))
                             [[:post last-child]] nil))))) 

(defn build-index
  "Builds the index from the *raw-index* file and spits it to *index*."
  []
  (spit *index*
              (apply str (render/render-index
                              (parser/assemble-map-posts *raw-posts*)))))

(defn build-archive
  "Builds the archive from the *raw-archive* file and spits it to *archive*."
  []
  (spit *archive*
              (apply str
               (render/render-archive (parser/assemble-map-posts *raw-archive*)))))
