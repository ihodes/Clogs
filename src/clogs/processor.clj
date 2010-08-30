(ns clogs.processor
  (require [clogs.render :as r]
           [clogs.dates :as d]
           [clojure.java.io :as io]
           [clojure.string :as s]))

;; postdir is the directory that the post is in. e.g e-and-clojure/
;; and inside that dir a post.md file, as well as whatever resources
;; used in the post (media etc)

(defn extract-post-meta
  "Returns the map found on the first line of the post.md file.
   'postdir is a directory that contains a post.md file.

   e.g. resources/p/test/"
  [postdir]
  (with-open [f (io/reader (str postdir "post.md"))]
    (assoc (read-string (first (take 1 (line-seq f))))
      :url postdir)))

(defn extract-post-content
  "Returns, as a string of text, the content of a post.md,
   given its directory"
  [postdir]
  (with-open [f (io/reader (str postdir "post.md"))]
    (s/join \newline (drop 1 (line-seq f)))))

(defn content-to-html
  "Creates a post.html file from the post.md in the
   specified 'postdir."
  [postdir]
  (spit (str postdir "post.html")
        (r/markdown (extract-post-content postdir))))

(defn process-post-meta
  "Takes the post meta and adds to it as necessary.

   Currently, only adds date if needed, or else
   formats it correctly."
  [m]
  (assoc m :date (d/fulldate-string
                  (d/date-from-string (m :date)))))

(defn assoc-content
  "Assocs the content (html of the markdown) of the post associated
   with 'm, metadata."
  [m]
  (let [content (r/markdown (extract-post-content (:url m)))]
    (assoc m :content content)))

(defn assoc-escaped-content
  "Assocs the escaped content (escaped html of the markdown)
   of the post associated  with 'm, metadata."
  [m]
  (let [content (r/escape-html (extract-post-content (:url m)))]
    (assoc m :content content)))

(defn replace-post-meta
  "Replaces the metadata map of the post.md in postdir
   with 'm."
  [postdir m]
  (let [content (extract-post-content postdir)
        newpost (str m \newline content)]
    (spit (str postdir "post.md") newpost)))
    
    
  
