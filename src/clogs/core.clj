(ns clogs.core
  (:import (java.util Date)
           (java.net URL))
  (:require [net.cgrand.enlive-html :as html]
            [clj-time.core :as clj-time]
            [clj-time.format :as clj-time-fmt]
            [clj-time.coerce :as time-coerce]
            [clogs.parser :as parser]
            [clogs.render :as render]))

;; Dirs
(def *absolute-root* "resources/") ;; root URL
(def *posts-folder* (str *absolute-root* "p/")) ;; folder where permalinked posts go

;; HTML files
(def *index* "index.html") ;; index.html file location
(def *archives* "darchives.html") ;; archive.html file location
;;(def *archives* (str *absolute-root* "archives.html")) ;; archive.html file location
(def *colophon* "colophon.html") ;; colophone.html file locations

 
(defn escape-html
  "Escapes lt, gt, amp and quot from 's"
  [s]
  (-> s str
      (.replace "&" "&amp;")
      (.replace "<" "&lt;")
      (.replace ">" "&gt;")
      (.replace "\"" "&quot;")))

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