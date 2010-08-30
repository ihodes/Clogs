(ns clogs.core
  (:require [net.cgrand.enlive-html :as html]
            [clogs.processor :as p]
            [clogs.render :as r]
            [clogs.postmaster :as pm]))

(def *index* "resources/index.html")
(def *archives* "resources/archives.html")

(defn publish-post
  "Processes the post.md in 'postdir, adding the metadata to the postbox,
   creating the index.html for the post and dating the post."
  [postdir]
  (let [meta (p/process-post-meta (p/extract-post-meta postdir))
        rawcontent (p/extract-post-content postdir)
        post (assoc meta :content (r/markdown rawcontent)
                    :excapedcontent (r/escape-html rawcontent))] 
    (p/replace-post-meta postdir meta)
    (pm/add-to-postbox meta)
    (spit (str postdir "index.html")
          (r/base-render {:title (post :title)
                          :body (r/just-post-snippet post)}))))

(defn build-index
  "Creates the index with the last 'n posts."
  ([] (build-index 5))
  ([n] (spit *index*
             (r/base-render
              {:title nil
               :body (r/index-snippet
                      (take-last n pm/posts))}))))

(defn prepend-to-archives
  "Prepends a formatted archive snippet string to  *archives*.

   Places it before the first <article>."
  [s]
  (spit *archives*
        (apply str (html/emit*
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

