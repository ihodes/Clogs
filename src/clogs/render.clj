(ns clogs.render
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
(def *index-template-file* "resources/templates/template_index.html")
(def *archive-template-file* "resources/templates/template_archive.html")
(def *post-template-file* "resources/templates/template_post.html")
(def *raw-post-template* "resoucres/p/posts/post.xml")

(defn render
  "Little helper for taking a list of strings and making a string from it."
  [xs]
  (apply str xs))


;; models a post: 
(html/defsnippet post-model *index-template-file* [:.cg-post]
  [{:keys [title author pubdate post-text perm-url]}]
  [:span.cg-post-title] (html/content title)
  [:a.cg-post-title ] (html/set-attr :href perm-url)
  [:.cg-post-author] (html/content author)
  [:.cg-post-pubdate] (html/content pubdate)
  [:.cg-post-text] (html/html-content post-text))


;; models an archive listing 
(html/defsnippet archive-listing-model *archive-template-file* [:.cg-post]
  [{:keys [title author pubdate perm-url]}]
  [:span.cg-post-title] (html/content title)
  [:a.cg-post-title ] (html/set-attr :href perm-url)
  [:.cg-post-author] (html/content author)
  [:.cg-post-pubdate] (html/content pubdate))

;; renders the html file for archive.html
;; takes in a clojure map as emitted by parser/assemble-map-posts
(html/deftemplate render-archive *archive-template-file*
  [{:keys [posts]}]
  [:.cg-wrap] (html/clone-for [post posts]
                              (html/content (archive-listing-model post))))

;; renders the html file for an individual post
;; takes in a clojure map as emitted by parser/assemble-map-posts
(html/deftemplate render-post *post-template-file*
  [post]
  [:.cg-wrap] (html/content (post-model post)))

;; models an XML post
(html/deftemplate render-raw-post *raw-post-template*
  [{:keys [title author content date perma-url]}]
  [:title] (html/content title)
  [:perma-url] (html/content perma-url)
  [:author] (html/content author)
  [:content] (html/content content)
  [:clgdate] (html/content date))

;; renders the html file for index.html
;; takes in a clojure map as emitted by parser/assemble-map-posts
(html/deftemplate render-index *index-template-file*
  [{:keys [posts]}]
  [:.cg-wrap] (html/clone-for [post posts]
                              (html/content (post-model post))))