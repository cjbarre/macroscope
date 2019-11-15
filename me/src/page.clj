(ns me.page)

(defn main-chart [series-title]
  {:height 500
   :width 1200
   :mark {:type "area"}
   :title {:text series-title
           :fontSize 20}
   :encoding {:x {:field "period" 
                  :type "temporal" 
                  :timeUnit "utcyearmonth"
                  :scale {:domain {:selection "brush"}}}
              :y {:field "value" :type "quantitative"}}})

(def details-chart 
  {:height 100
   :width 1200
   :mark "area"
   :selection {:brush {:type "interval" :encodings ["x"]}}
   :encoding {:x {:field "period" :type "temporal" :timeUnit "utcyearmonth"}
              :y {:field "value" :type "quantitative"}}})

(defn page [series-id series-title]
  [:div {:style "margin:0 auto;width:1200;"}
   [:vega-lite {:data {:url (format "data/%s.json" series-id)}
                :vconcat [(main-chart series-title)
                          details-chart]}]])

