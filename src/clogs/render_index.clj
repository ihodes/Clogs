(ns clogs.render-index
  (:import java.util.Date)
  (:require [clojure.contrib.shell-out :as shell]))

(println (re-seq #".*" (shell/sh "ls" "/Users/ihodes/clogs")))
