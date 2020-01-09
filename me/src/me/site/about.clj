(ns me.site.about
  (:require [me.meca :as meca]
            [me.site.components.author :as a]
            [me.site.render :as r]))

(def page
  [:div.page-outer-container
   [:div.page-inner-container
    [:div.page-title :meca/title]
    a/author
    [:div.page-content
     [:p :meca/p1]
     [:p :meca/p2]
     #_[:p :meca/p3]
     #_[:p :meca/p4]
     [:h3 :meca/services-shout]
     [:p :meca/services-cta]
     [:p :meca/p6]]]])

(defn generate []
  (spit "build/site/about.html"
        (r/->html {:title "Who we are | Mortal Economics"
                   :description "Need custom economic software services or consultation?"
                   :content (meca/replace
                             (meca/read-file "copy/site/about.meca")
                             page)})))