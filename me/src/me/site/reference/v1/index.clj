(ns me.site.reference.v1.index
  (:require [me.site.components.classic-page :as p]
            [me.meca :as meca]
            [me.site.render :as r]))

(def page 
  (p/classic-page 
   [:div 
    [:h2 "Browse by data source"]
    [:h3 "United States"]
    [:h4 "Bureau of Labor Statistics"]
    [:a {:href "./bls/ces/"} "Current Employment Statistics"]
    [:h4 "Census Bureau"]
    [:a {:href "./census/naics-2017/"} "NAICS 2017"]]))

(defn generate []
  (spit "build/site/reference/v1/index.html"
        (r/->html {:title "The Reference"
                   :content (meca/replace
                             (meca/read-file "copy/site/reference/v1/index.meca")
                             page)})))

