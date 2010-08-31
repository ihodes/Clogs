(ns clogs.processor
  (require [clogs.render :as r]
           [clogs.dates :as d]
           [clojure.java.io :as io]
           [clojure.string :as s]))

;; postdir is the directory that the post is in. e.g e-and-clojure/
;; and inside that dir a post.md file, as well as whatever resources
;; used in the post (media etc)
(def *base-url* "http://copperthoughts.com/")
(def *post-base* "resources/")

(defn full-post-dir
  "Returns the full post path."
  [postdir]
  (str *post-base* postdir))

(defn extract-post-meta
  "Returns the map found on the first line of the post.md file.
   'postdir is a directory that contains a post.md file.

   e.g. p/test/"
  [postdir]
  (with-open [f (io/reader (str (full-post-dir postdir) "post.md"))]
    (read-string (first (take 1 (line-seq f))))))

(defn extract-post-content
  "Returns, as a string of text, the content of a post.md,
   given its directory"
  [postdir]
  (with-open [f (io/reader (str (full-post-dir postdir) "post.md"))]
    (s/join \newline (drop 1 (line-seq f)))))

(defn process-post-meta
  "Takes the post meta and adds to it as necessary.

   Currently, only adds the url, and date if needed, or else
   formats the date correctly.

   You must add the postdir, as well, but as an arg."
  [m postdir]
  (assoc m :date (d/fulldate-string
                  (d/date-from-string (m :date)))
         :postdir postdir
         :url (str *base-url* postdir)))

(defn assoc-content
  "Assocs the content (html of the markdown) of the post associated
   with 'm, metadata."
  [m]
  (let [content (r/markdown (extract-post-content (:postdir m)))]
    (assoc m :content content)))

(defn assoc-escaped-content
  "Assocs the escaped content (escaped html of the markdown)
   of the post associated  with 'm, metadata."
  [m]
  (let [content (r/escape-html (r/markdown (extract-post-content (:postdir m))))]
    (assoc m :escapedcontent content)))

(defn replace-post-meta
  "Replaces the metadata map of the post.md in postdir
   with 'm."
  [postdir m]
  (let [content (extract-post-content postdir)
        newpost (str m \newline content)]
    (spit (str(full-post-dir  postdir) "post.md") newpost)))
    
    
  
