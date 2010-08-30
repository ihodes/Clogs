(ns clogs.dates
  (:require [clj-time.core :as time]
            [clj-time.format :as time-fmt]
            [clj-time.coerce :as time-coerce]))

(declare dig-2)

;; basic date formate to parse
(def clg-time-fmt (time-fmt/formatter "dd-MM-yyyy"))

(defn clgdate-fmt
  [date]
  (let [y (time/year (time-fmt/parse clg-time-fmt date))
        m (time/month (time-fmt/parse clg-time-fmt date))
        d (time/day (time-fmt/parse clg-time-fmt date))]
    (str (dig-2 (str d)) "." (dig-2 (str m)) "." y)))

(defn rssdate-fmt [date] (clgdate-fmt date))

(defn datetime-fmt
  [date]
  (let [y (time/year (time-fmt/parse clg-time-fmt date))
        m (time/month (time-fmt/parse clg-time-fmt date))
        d (time/day (time-fmt/parse clg-time-fmt date))]
    (str y "-" m "-" d)))

;; Utilities

(defn dig-2
  "Pads s with a 0 if it's just length 1"
  [s]
  (if (= (count s) 1) (str "0" s) s))