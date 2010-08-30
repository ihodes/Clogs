(ns clogs.render
  (:require [net.cgrand.enlive-html :as html]
            [clogs.dates :as dates])
  (:import [com.petebevin.markdown MarkdownProcessor]))

;; defines the html/xml to be used as templates
;; loads this from settings.clogs later...
(def *index-template-file* "templates/index_template.html")
(def *archive-template-file* "templates/archives_template.html")
(def *post-template-file* "templates/post_template.html")
(def *base-template-file* "templates/base_template.html")
(def *rss-template-file* "templates/feed_template.xml")

(defn concat-strings
  "Concats a list of strings."
  [ss]
  (apply str ss))

(defn escape-html
  "Escapes lt, gt, amp and quot from 's"
  [s]
  (-> s str
      (.replace "&" "&amp;")
      (.replace "<" "&lt;")
      (.replace ">" "&gt;")
      (.replace "\"" "&quot;")))

(defn markdown
  "Returns the HTML version of the Markdown string 's."
  [s]
  (.markdown (MarkdownProcessor.) s))

;; taken from dnolen's excellent http://github.com/swannodette/enlive-tutorial
(defmacro maybe-content
  ([expr] `(if-let [x# ~expr] (html/content x#) identity))
  ([expr & exprs] `(maybe-content (or ~expr ~@exprs))))
(defmacro maybe-substitute
  ([expr] `(if-let [x# ~expr] (html/substitute x#) identity))
  ([expr & exprs] `(maybe-substitute (or ~expr ~@exprs))))

(html/deftemplate base-render *base-template-file*
  [{:keys [title body]}]
  [:title] (html/append (when-not (nil? title) (str " | " title)))
  [:body] (maybe-substitute body))

(html/defsnippet just-post-snippet *post-template-file* [:body]
  [{:keys [title date content]}]
  [:article :h1] (html/content title)
  [:article :time] (html/content (clgdate-fmt date))
  [:article :time] (html/set-attr :datetime (datetime-fmt date))
  [:article :section] (html/content content))

(html/defsnippet index-snippet *index-template-file* [:body]
  [posts]
  [:article] (html/clone-for
              [post posts]
              [:article :h1 :a] (html/do->
                                 (html/set-attr :href (post :url))
                                 (html/content (post :title)))
              [:article :time] (html/do->
                                (html/content (clgdate-fmt (post :date)))
                                (html/set-attr :datetime
                                               (datetime-fmt (post :date))))
              [:article :section] (html/content (post :content))))

(html/defsnippet single-archive-post *archive-template-file* [:article]
  [postmap]
  [:h1 :a] (html/do->
            (html/content (postmap :title))
            (html/set-attr :href (postmap :url)))
  [:time] (html/do->
           (html/content (clgdate-fmt (postmap :date)))
           (html/set-attr :datetime (datetime-fmt (postmap :date))))
  [:span#summary] (html/content (postmap :summary)))

;; takes a vector of {:title :date :summary :url} maps 
(html/defsnippet archive-snippet *archive-template-file* [:body]
  [abbrev-posts]
  [:article] (html/clone-for [abbr abbrev-posts]
                             [:article :h1 :a] (html/do->
                                                (html/content (abbr :title))
                                                (html/set-attr :href
                                                               (abbr :url)))
                             [:article :time] (html/content
                                               (clgdate-fmt (abbr :date)))
                             [:article :time] (html/set-attr
                                               :datetime
                                               (datetime-fmt (abbr :date)))
                             [:article :span#summary] (html/content
                                                       (abbr :summary))))

;; post = {:title :date :url :escapedcontent}
;; possible bug: this strips <?xml version="1.0" encoding="UTF-8"?>
;; from the xml template
(html/deftemplate rss-render (html/xml-resource *rss-template-file*)
  [posts]
  [:item] (html/clone-for [c posts]
                          [:item :title] (html/content (c :title))
                          [:item :pubDate] (html/content (rssdate-fmt
                                                          (c :date)))
                          [:item :link] (html/content (c :url))
                          [:item :description] (html/content
                                                (escape-html
                                                 (c :escapedcontent)))))