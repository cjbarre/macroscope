(ns me.reference.v1.bls.ces.template)

(defn main-chart [data series-title]
  {:data data
   :height 250
   :width 500
   :mark {:type "area"}
   :title {:text series-title
           :fontSize 20}
   :encoding {:x {:field "period" 
                  :type "temporal" 
                  :timeUnit "utcyearmonth"}
              :y {:field "value" :type "quantitative"}}})

(defn details-chart [data]
  {:data data
   :height 100
   :width 500
   :mark "area"
   :selection {:brush {:type "interval" :encodings ["x"]}}
   :encoding {:x {:field "period" :type "temporal" :timeUnit "utcyearmonth"}
              :y {:field "value" :type "quantitative"}}})

(defn rate-of-change-12-mo [data]
  {:data data
   :height 250
   :width 500
   :mark "area"
   :encoding {:x {:field "period" :type "temporal" :timeUnit "utcyearmonth"}
              :y {:field "value" :type "quantitative"}}})

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
  (let [main-data {:url (format "data/%s.json" series-id)}
        roc-12-mo-data {:url (format "data/%s-roc12.json" series-id)}]
    [:div#body-container
     [:div#series-title series-title]
     [:div#series-source "Source: Bureau of Labor Statistics"]
     [:div#series-id (format "Series ID: %s" series-id)]
     [:div.section
      [:div.section-header
       [:div.section-title "Official Data"]]
      [:div.section-body
       [:div main-series-text]
       [:vega-lite (main-chart main-data series-title)]]]

     [:div.section
      [:div.section-header
       [:div.section-title "Rate of Change (ROC12)"]]
      [:div.section-body
       [:vega-lite (rate-of-change-12-mo roc-12-mo-data)]
       rate-of-change-12-text]]]))

