(ns clogs.parser
  (:require [clojure.xml :as xml]))
;; Module handles parsing a (specially formatted) xml file into a easily-
;; readable clojure hash-map.
;; 
;; See below for information as to how the xml must be formatted
;; and how the map is structured.

(defn- raw-posts
  "Specify a path to the file to be parsed (.xml), get a XML tree of the posts."
  [path]
  (let [posts (get
               (first (xml-seq (xml/parse (java.io.File. path))))
               :content)]
    (for [post posts] (:content post)))) ;; essentially I'm stripping the
;; first result of parse + xml-seq of its boilerplate
  
(defn- get-tags
  "Gets the relevant tags from raw-posts."
  [posts]
  (first (for [post posts]
           (for [part post] (get part :tag)))))
;; Just grabbing the tags of the second-level nested nodes
;; in my XML (see below)

(defn- get-content
  "Gets the content associated with relevant tags."
  [posts]
  (for [post posts]
    (for [part post] (first (get part :content)))))
;; Similar to get-tags. Content is partitioned per post.

(defn- zip-tags-to-content
  "Assembles a postmap to specifications (see below)."
  [tags content]
  (assoc {} :posts
         (map #(zipmap tags %) content)))
;; Zips up the tags and content into a map.

(defn parse-post
  "Parses a simple XML file into a clojure map for a post.
   The post is nested in one <post> tag."
  [path]
  (let [post (get (first (xml-seq
                          (xml/parse (java.io.File. path))))
                  :content)]
    (zipmap
     (map #(get % :tag) post)
     (map #(first (get % :content)) post))))

;; this is the wrapper function - takes in the posts.xml file and emits a map
;; for use in render/render-index and render/render-archives and render/render-rss
(defn assemble-map-posts
  "Takes in a file's path and returns the final postmap. (used for posts.xml)"
  [path]
  (let [posts (raw-posts path)]
    (let [tags (get-tags posts)
          content (get-content posts)]
      (zip-tags-to-content tags content))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;
;;;;  Here's how a postmap should look:
;; 
;; {:posts [{:title "A title 1"
;;           :author "Someone"
;;           :pubdate "The time"
;;           :post-text "Here's some content"}
;;          {:title "A title 2"
;;           :author "Someone"
;;           :pubdate "The time"
;;           :post-text "Here's some content"}
;;          {:title "A title 3"
;;           :author "Someone"
;;           :pubdate "The time"
;;           :post-text "Here's some content"}]}
;;
;;;; Parsed from an .xml file that loos like this:
;; <posts>
;;    <post>
;;      <title> A title 1 </title>
;;      <author> Someone </author
;;      <pubdate> March 17th 2011 </pubdate>
;;      <post-text> Here's &lt;em&gt;emphasis!&lt;/em&gt; </post-text>
;;    </post>
;;    ...
;; </posts>
;;;; Note that HTML is escaped in the XML.
