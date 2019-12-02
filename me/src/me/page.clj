(ns me.page)

(defn main-chart [data series-title]
  {:data data
   :mark {:type "area"}
   :title {:text series-title
           :fontSize 20}
   :encoding {:x {:field "period" 
                  :type "temporal" 
                  :timeUnit "utcyearmonth"
                  :scale {:domain {:selection "brush"}}}
              :y {:field "value" :type "quantitative"}}})

(defn details-chart [data]
  {:data data
   :mark "area"
   :selection {:brush {:type "interval" :encodings ["x"]}}
   :encoding {:x {:field "period" :type "temporal" :timeUnit "utcyearmonth"}
              :y {:field "value" :type "quantitative"}}})

(defn rate-of-change-12-mo [data]
  {:data data
   :mark "area"
   :encoding {:x {:field "period" :type "temporal" :timeUnit "utcyearmonth"}
              :y {:field "value" :type "quantitative"}}})

(defn page [series-id series-title]
  (let [main-data {:url (format "data/%s.json" series-id)
                   :name "data"
                   :format {:type "json"
                            :property "primary_data"}}
        roc-12-mo-data (assoc-in main-data [:format :property] "rate_of_change_12_mo")]
    [:div.page-container.grid
     [:div.page-header
      [:div.series-title series-title]]
     [:section.page-body
      [:div.data-section
       [:div.data-section-header 
        "Data Values (10 Years)"]
       [:div.data-section-content 
        [:img.chart {:src "/assets/test.svg"}]]]
      [:div.data-section
       [:div.data-section-header
        "Rate of Change YTD (12 Month Lookback)"]
       [:div.data-section-content
        [:img.chart {:src "/assets/test2.svg"}]]]]]))

