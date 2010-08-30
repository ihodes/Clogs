(ns clogs.core
  (:import (java.util Date)
           (java.net URL))
  (:require [net.cgrand.enlive-html :as html]
            [clj-time.core :as clj-time]
            [clj-time.format :as clj-time-fmt]
            [clj-time.coerce :as time-coerce]
            [clogs.parser :as parser]
            [clogs.render :as render]))

(def *posts-folder* (str "resources/p/"))
(def *index* "index.html") 
(def *archives* "archives.html") 
(def *colophon* "colophon.html")

(defn prepend-to-archives
  "Prepends a formatted archive snippet string to  *archives*.

   Places it before the first <article>"
  [s]
  (spit (str "resources/" *archives*)
        (apply str (html/emit*
                    (html/at (first (html/html-resource *archives*)) 
                             [[:article (html/nth-of-type 1)]]
                             (html/before
                              (str \newline s \newline)))))))