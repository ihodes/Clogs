(ns clogs.publisher
  (:use clogs.postmaster
	[clojure.contrib.io :only (delete-file-recursively)])
  (:require [net.cgrand.enlive-html :as html]
            [clogs.processor :as p]
            [clogs.render :as r]))

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
    (pb-add-to-postbox meta)
    (spit (str (p/full-post-dir postdir) "index.html")
          (r/concat-strings
           (r/base-render {:title (post :title)
                           :body (r/just-post-snippet post)})))))

(defn- build-index
  "Creates the index with the last 'n posts."
  ([] (build-index *posts-to-show*))
  ([n] (r/concat-strings
	(r/base-render
	 {:title nil
	  :body (r/index-snippet
		 (map p/assoc-content (reverse (take-last n (pb-posts)))))}))))

(defn- build-rss
  "Creates the rss feed with the last 'n posts."
  ([] (build-rss *posts-to-show*))
  ([n] (r/concat-strings
	(r/rss-render
	 (map p/assoc-escaped-content (reverse (take-last n (pb-posts))))))))

(defn- build-archives
  "Creates the archives from all the published posts."
  [] (r/concat-strings
      (r/base-render
       {:title "The Archives"
	:body (r/archive-snippet (reverse (pb-posts)))})))

(defn- publish-build
  "Spits the output of the given build to the given file, 'f."
  [build f]
  (spit f build))

(defn publish-post
  "Publish post to index, archives and rss, as well as creates the permalink."
  [postdir]
  (pre-publish-post postdir)
  (publish-build (build-archives) *archives*)
  (publish-build (build-index) *index*)
  (publish-build (build-rss) *feed*))

(defn complete-delete-post
  "Entirely deletes a post in 'postdir."
  [postdir]
  (pb-delete-post postdir)
  (delete-file-recursively postdir)
  (publish-build (build-archives) *archives*)
  (publish-build (build-index) *index*)
  (publish-build (build-rss) *feed*))

(defn update-post
  "Updates a post with :postdir 'postdir with the new post.md in 'postdir."
  [postdir]
  (let [meta (p/process-post-meta (p/extract-post-meta postdir) postdir)
        rawcontent (p/extract-post-content postdir)
        post (assoc meta
               :content (r/markdown rawcontent))]
    (p/replace-post-meta postdir meta)
    (pb-edit-post postdir meta)
    (spit (str (p/full-post-dir postdir) "index.html")
          (r/concat-strings
           (r/base-render {:title (post :title)
                           :body (r/just-post-snippet post)})))
    (publish-post postdir)))