(ns me.site.components.reference-page
  (:require [me.meca :as meca]
            [me.site.components 
             [author :as a]
             [temporal-chart :as tc]]
            [me.vega-embed :as v]
            [clojure.string :as string]))

(defn reference-page [series-id series-title naics-code period-type]
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
        (v/vega-embed [:vega-lite (tc/temporal-chart {:data main-data
                                                      :title (first (string/split series-title #","))
                                                      :period-type period-type})])
        [:h3 :meca/s3-title]
        [:p :meca/s3-p1]
        [:p :meca/s3-p2]
        [:p :meca/s3-p3]
        (v/vega-embed [:vega-lite (tc/temporal-chart {:data roc-12-mo-data
                                                      :title "Rate of Change"
                                                      :y-title "% change"
                                                      :period-type period-type})])]]])))