(ns me.site.reference.v1.bls.ces.template
  (:require [me.meca :as meca]
            [me.vega-embed :as v]
            [me.site.components.author :as a]
            [clojure.string :as string]))

(defn main-chart [data series-title]
  {:data data
   :height 300
   :width "container"
   :background "#F8F6F6"
   :title (first (string/split series-title #","))
   :mark {:type "area" :tooltip true :color "#8d9db6"}
   :encoding {:x {:field "period"
                  :type "temporal"
                  :timeUnit "yearmonth"
                  :axis {:title nil
                         :titleFontSize 15
                         :titlePadding 10
                         :format "%m/%y"
                         :labelPadding 10
                         :labelFontSize 14
                         :labelFlush false}}
              :y {:field "value" 
                  :type "quantitative"
                  :axis {:title (first (string/split series-title #","))
                         :titleFontSize 15
                         :titlePadding 5
                         :labelPadding 10
                         :labelFontSize 14}}}})

(defn rate-of-change-12-mo [data]
  {:data data
   :height 300
   :width "container"
   :background "#F8F6F6"
   :title "Rate of Change"
   :mark {:type "area" :tooltip true :color "#667292"}
   :encoding {:x {:field "period" 
                  :type "temporal" 
                  :timeUnit "yearmonth"
                  :axis {:title nil
                         :titleFontSize 15
                         :titlePadding 10
                         :format "%m/%y"
                         :labelPadding 10
                         :labelFontSize 14
                         :labelFlush false}}
              :y {:field "value" 
                  :type "quantitative"
                  :axis {:title "% change"
                         :titleFontSize 15
                         :titlePadding 5
                         :labelPadding 10
                         :labelFontSize 14}}}})

(defn page [series-id series-title naics-code]
  (let [main-data {:url (format "data/%s.json" series-id)}
        roc-12-mo-data {:url (format "data/%s-roc12.json" series-id)}]
    (meca/replace
     (meca/read-file "copy/site/reference/v1/bls/ces/page.meca")
     [:div.page-outer-container.test
      [:div.page-inner-container
       [:div.page-content
        [:div.page-title series-title]
        a/author
        [:h3 :meca/s1-title]
        [:p [:a {:href (format "../../census/naics-2017/%s.html" naics-code)}
             :meca/s1-p1]]
        [:h3 :meca/s2-title]
        [:p :meca/s2-p1]
        (v/vega-embed [:vega-lite (main-chart main-data series-title)])
        [:h3 :meca/s3-title]
        [:p :meca/s3-p1]
        [:p :meca/s3-p2]
        [:p :meca/s3-p3]
        (v/vega-embed [:vega-lite (rate-of-change-12-mo roc-12-mo-data)])]]])))