(ns clogs.render
  (:require [net.cgrand.enlive-html :as html])
  (:use compojure.core
        [clojure.contrib.duck-streams :only (spit)]))

;; defines the html/xml to be used as a template(s)
;; loads this from settings.clogs later...
(def *index-template-file* "resources/templates/template_index.html")
(def *archive-template-file* "resources/templates/template_archive.html")
(def *post-template-file* "resources/templates/template_post.html")
(def *raw-post-template* "resources/p/posts/post.xml")

(defn render
  "Concats a list of strings."
  [x]
  (apply str x))

;; models a post in the index and individual post page
(html/defsnippet post-model *index-template-file* [:.cg-post]
  [{:keys [title author clgdate content perm-url]}]
  [:span.cg-post-title] (html/content title)
  [:a.cg-post-title ] (html/set-attr :href perm-url)
  [:.cg-post-author] (html/content author)
  [:.cg-post-pubdate] (html/content clgdate)
  [:.cg-post-text] (html/html-content content))

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
  [{:keys [title author content clgdate-norm perm-url]}]
  [:title] (html/content title)
  [:perm-url] (html/content perm-url)
  [:author] (html/content author)
  [:content] (html/content content)
  [:clgdate-norm] (html/content clgdate-norm))

;; renders the html file for index.html
;; takes in a clojure map as emitted by parser/assemble-map-posts
(html/deftemplate render-index *index-template-file*
  [{:keys [posts]}]
  [:.cg-wrap] (html/clone-for [post posts]
                              (html/content (post-model post))))