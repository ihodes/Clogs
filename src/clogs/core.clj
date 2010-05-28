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
            [clojure.xml :as xml]
            [clojure.contrib.zip-filter :as zf]))


;; Time format (used in posts)
;; Used to parse out and process time
(def date-format
     (clj-time-fmt/formatter "MM-dd-yyyy"))
;; this isn't how I want to do things: this can't be efficient.
(defn get-all-post-urls
  "Returns list of URLs in the [dir] directory with file:// as the protocol."
  [dir]
  (map #(str "file://" %)
       (map #(.getAbsolutePath %)
            (filter #(.isFile %) (file-seq (java.io.File. dir))))))
;; Returns the publish date of a post (URL)
;; format of date: month/day/year [xx/xx/xxxx]
;; format of output: java.util.Date
;; (clj-time-fmt/parse date-format
(defn get-pub-date
  "Returns the publish date of a post (specified by the URL, with protocol).
   Outputting a list of DatesTime objects (from Joda time lib)."
  [post]
  (clj-time-fmt/parse date-format
                      (first
                       (map html/text
                            (html/select (fetch-url post)
                                         [:span.pubdate]))))) ;; be sure to 
;; ... have a pdate specified in the post.

;; used for sorting
(defn newer-pub?
  "Returns true if post1 is newer than post2. Posts are specified by URLs."
  [post1 post2]
  (> (time-coerce/to-long (get-pub-date post1))
     (time-coerce/to-long (get-pub-date post2))))

;; add in routes like <(GET "\about" (slurp "path/to/about.html"))> as needed
;; 
;; TODO: add in regex to validate paths, and redirect certain failed routes
(defroutes clogs
  (GET "/" [] (slurp "/Users/ihodes/clogs/html/index.html"))
  (GET "/:year/:name" [name] (render-new-index name))
  (GET "/:something" [something] (str "\"" something "\" is not a valid URI here"))
  (route/not-found "Page not found: please go back home."))

;(run-jetty clogs {:port 8080})