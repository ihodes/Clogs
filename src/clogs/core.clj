(ns clogs.core
  (:require [clogs.processor :as p]
            [clogs.render :as r]
            [clogs.postmaster :as pm]))

(def *index* "resources/index.html")

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