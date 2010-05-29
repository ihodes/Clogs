(ns clogs.render-index
  (:require [net.cgrand.enlive-html :as html])
  (:use compojure.core
        [clojure.contrib.duck-streams :only (spit)]))

;; This module handles formatting a clojure hash-map into a HTML template
;; with proper tags (specified below).
;;
;; tags/classes (indented as nested):
;;    .cg-wrap
;;        .cg-post
;;            .cg-post-title
;;            .cg-post-author
;;            .cg-post-pubdate
;;            .cg-post-text
;;
;;
;;
;;
;; defines the html to be used as a template
;; loads this from settings.clogs later
(def *index-template-file* "resources/template_index.html")

(defn render
  "Little helper for taking a list of strings and making a string from it."
  [xs]
  (apply str xs))

;; models a post: needs to add permalink portion later
;; and needs to handle images, later
(html/defsnippet post-model *index-template-file* [:.cg-post]
  [{:keys [title author pubdate post-text]}]
  [:.cg-post-title] (html/content title)
  [:.cg-post-author] (html/content author)
  [:.cg-post-pubdate] (html/content pubdate)
  [:.cg-post-text] (html/html-content post-text))

(html/deftemplate render-index *index-template-file*
  [{:keys [posts]}]
  [:.cg-wrap] (html/clone-for [post posts]
                              (html/content (post-model post))))