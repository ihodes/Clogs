(ns clogs.dates
  (:require [clj-time.core :as time]
            [clj-time.format :as time-fmt])
  (:import [java.util GregorianCalendar]))

(declare dig-2)

;; basic date formate to parse
(def full-date-fmt (time-fmt/formatters :date-hour-minute))
(def short-date-fmt (time-fmt/formatter "YYYY-MM-dd"))

(defn date-from-string
  "Attempts to parse the date from the 's.

  If it isn't in either the full-date-format or short-date-format,
  then it returns the current date and UTC time."
  [s]
  (try (time-fmt/parse full-date-fmt s)
       (catch Exception e
         (try (time-fmt/parse short-date-fmt s)
              (catch Exception e
                (time/now))))))

(defn fulldate-string
  "Returns a string of the date in the
   YYYY-mm-ddThh:mm format."
  [date]
  (time-fmt/unparse full-date-fmt date))

(defn rssdate-string
  "Returns a string of the date in the
   format I want to have on my RSS feed."
  [date]
  (fulldate-string date))

(defn clgdate-string
  "Returns a string of the date in the
   format I want to display on my blog."
  [date]
  (str (dig-2 (time/day date)) "."
       (dig-2 (time/month date)) "."
       (time/year date)))

;; Utilities

(defn dig-2
  "Pads s with a 0 if it's just length 1"
  [s]
  (if (= (count (str s)) 1)
    (str "0" s)
    (str s)))