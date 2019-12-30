(ns me.site.components.temporal-chart)

(def period-time-unit {:M {:time-unit "yearmonth"
                           :x-label-format "%m/%y"}
                       :Q {:time-unit "yearquarter"
                           :x-label-format "%q/%y"}
                       :A {:time-unit "year"
                           :x-label-format "%Y"}})

(defn temporal-chart [{:keys [data title x-title y-title width height period-type] 
                       :or {height 300 
                            width "container"
                            period-type "M"}}]
  (let [{:keys [time-unit x-label-format]} (-> period-type keyword period-time-unit)]
    {:data data
     :height height
     :width width
     :background "#F8F6F6"
     :title title
     :mark {:type "area" :tooltip true :color "#8d9db6"}
     :encoding {:x {:field "period"
                    :type "temporal"
                    :timeUnit time-unit
                    :axis {:title x-title
                           :titleFontSize 15
                           :titlePadding 10
                           :format x-label-format
                           :labelPadding 10
                           :labelFontSize 14
                           :labelFlush false}}
                :y {:field "value"
                    :type "quantitative"
                    :axis {:title y-title
                           :titleFontSize 15
                           :titlePadding 5
                           :labelPadding 10
                           :labelFontSize 14}}}}))