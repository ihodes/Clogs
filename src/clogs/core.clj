(ns clogs.core
  (:require [net.cgrand.enlive-html :as html]
            [clogs.processor :as p]
            [clogs.render :as r]
            [clogs.postmaster :as pm]))

(def *posts-to-show* 5)

(def *index* "resources/index.html")
(def *archives* "resources/archives.html")
(def *feed* "resources/feed.xml")

(defn pre-publish-post
  "Processes the post.md in 'postdir, adding the metadata to the postbox,
   creating the index.html for the post and dating the post."
  [postdir]
  (let [meta (p/process-post-meta (p/extract-post-meta postdir) postdir)
        rawcontent (p/extract-post-content postdir)
        post (assoc meta
               :content (r/markdown rawcontent))]
    (p/replace-post-meta postdir meta)
    (pm/add-to-postbox meta)
    (spit (str postdir "index.html")
          (r/concat-strings
           (r/base-render {:title (post :title)
                           :body (r/just-post-snippet post)})))))

(defn build-index
  "Creates the index with the last 'n posts."
  ([] (build-index *posts-to-show*))
  ([n] (spit *index*
             (r/concat-strings
              (r/base-render
               {:title nil
                :body (r/index-snippet
                       (map p/assoc-content (reverse (take-last n (pm/posts)))))})))))

(defn build-rss
  "Creates the rss feed with the last 'n posts."
  ([] (build-rss *posts-to-show*))
  ([n] (spit *feed*
             (r/concat-strings
              (r/rss-render
               (map p/assoc-escaped-content (reverse (take-last n (pm/posts)))))))))

(defn build-archives
  []
  (spit *archives*
        (r/concat-strings
         (r/base-render
          {:title "The Archives"
           :body (r/archive-snippet (reverse (pm/posts)))}))))

(defn- prepend-to-archives
  "Prepends a formatted archive snippet string to  *archives*.

   Places it before the first <article>."
  [s]
  (spit *archives*
        (r/concat-strings
         (html/emit*
          (html/at (first (html/html-resource "archives.html")) 
                   [[:article (html/nth-of-type 1)]]
                   (html/before
                    (str \newline s \newline)))))))

(defn post-to-archive
  "Prepends the specified post to the archives."
  [postdir]
  (let [meta (p/process-post-meta (p/extract-post-meta postdir))
        rawcontent (p/extract-post-content postdir)
        post (assoc meta :content (r/markdown rawcontent))]
    (prepend-to-archives
     (r/archive-post-string post))))

(defn publish-post
  "Publish post to index, archives and rss, as well as create the permalink."
  [postdir]
  (pre-publish-post postdir)
  (post-to-archive postdir)
  (build-index *posts-to-show*)
  (build-rss *posts-to-show*))
