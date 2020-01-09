(ns me.site.index
  (:require [me.meca :as meca]
            [me.site.components.author :as a]
            [me.site.render :as r]))

(def page 
  [:div.page-outer-container
   [:div.page-inner-container
    [:div.page-title :meca/title]
    a/author
    [:div.page-content
     [:p.landing-page-article-greeting :meca/greeting]
     [:p :meca/favor]
     [:p [:a {:href "/reference/v1"} :meca/cta]]
     [:p :meca/p1]
     [:p :meca/p2]
     [:p :meca/p3]
     [:p :meca/p4]
     [:p :meca/p5]]]])

(defn generate []
  (spit "build/site/index.html"
        (r/->html {:title "Learn about the economy | Mortal Economics"
                   :description "Mortal Economics builds tools for getting the most out of important economic data. Are you an economic, finance, or other firm in need of custom economic data processing or other software? We can help you."
                   :content (meca/replace
                             (meca/read-file "copy/site/index.meca")
                             page)})))