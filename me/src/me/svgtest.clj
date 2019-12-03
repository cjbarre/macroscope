(ns me.svgtest
  (:require [clojure.data.json :as json]))


(def plot {:width 500
           :height 200
           :data {:url "http://localhost/charts/ees/data/CES0500000001.json"
                  :format {:type "json"
                           :property "primary_data"}}
           :mark "area"
           :encoding {:x {:field "period" 
                          :type "temporal" 
                          :timeUnit "utcyearmonth" 
                          :axis {:title nil
                                 :labelFontSize 15
                                 :labelAngle -45
                                 :format "%Y"
                                 ;:labelOverlap "greedy"
                                 :labelPadding 10
                                 ;:labelFlush false
                                 }}
                      :y {:field "value" 
                          :type "quantitative" 
                          :axis {:title nil
                                 :labelFontSize 15
                                 :labelPadding 10}}}})

(spit "test.json" (json/write-str plot :key-fn name))

(spit "test2.json" (json/write-str (-> plot 
                                       (assoc-in [:data :format :property] "rate_of_change_12_mo")
                                       (assoc-in [:encoding :x :axis :format] "%m-%y")) :key-fn name))