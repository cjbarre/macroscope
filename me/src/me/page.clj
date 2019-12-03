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

#_(defn page [series-id series-title]
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

(def main-series-text
  [:div
   [:p.section-p "Here is a ten year window of offical data."]
   [:p.section-p "This view of the data sets the context, but it's difficult to gain insight without
                  further inspection."]])

(def rate-of-change-12-text
  [:div
   [:p.section-p "By comparing the ROC12 to the official data, consider whether the 
                  current trend is slowing down or picking up speed."]
   [:p.section-p "If the trend is moving towards zero, in either direction, it is slowing down.
                  If it's moving away from zero, in either direction, it's speeding up."]
   [:p.section-p "Positive means growing, negative means shrinking."]])

(defn page [series-id series-title]
  [:div#main-container
   [:div#header-container
    [:div#site-title "Mortal Economics"]
    [:div#series-title series-title]
    [:div#series-source "Source: Bureau of Labor Statistics"]
    [:div#series-id (format "Series ID: %s" series-id)]]
   [:div#body-container
    [:div.section
     [:div.section-header
      [:div.section-title "Official Data"]]
     [:div.section-body
      [:div main-series-text]
      [:img.chart {:src (format "charts/%s.svg" series-id)}]]]

    [:div.section
     [:div.section-header
      [:div.section-title "Rate of Change (ROC12)"]]
     [:div.section-body
      [:img.chart {:src "/assets/test2.svg"}]
      rate-of-change-12-text]]]
   [:div#footer-container]])

