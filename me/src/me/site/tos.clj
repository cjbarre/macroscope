(ns me.site.tos
  (:require [me.meca :as meca]
            [me.site.components.author :as a]))

(defn tos-section 
  [heading title text]
  [:div 
   [:h2 heading]
   [:h3 title]
   [:p text]])

(def page
  [:div.page-outer-container
   [:div.page-inner-container
    [:div.page-title :meca/title]
    a/author
    [:div.page-content
     [:p.landing-page-article-greeting :meca/greeting]
     [:p :meca/favor]
     [:h1 "Section 1"]
     [:h2 :meca/s1-title]
     (tos-section "1.1" 
                  :meca/s1-1-title 
                  :meca/s1-1-text)
     (tos-section "1.2"
                  :meca/s1-2-title
                  :meca/s1-2-text)
     (tos-section "1.3"
                  :meca/s1-3-title
                  :meca/s1-3-text)]]])

(defn generate []
  (spit "build/raw/tos.clj"
        (format "(ns raw.tos) \n \n %s"
                (meca/replace
                 (meca/read-file "copy/tos.meca")
                 page))))

(generate)
