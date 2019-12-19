(ns me.site.index
  (:require [me.meca :as meca]
            [me.site.components.author :as a]))

(def page 
  [:div.page-outer-container
   [:div.page-inner-container
    [:div.page-title :meca/title]
    a/author
    [:div.page-content
     [:p.landing-page-article-greeting :meca/greeting]
     [:p :meca/favor]
     [:p :meca/body-p-1]
     [:p :meca/body-p-2]
     [:p :meca/body-p-3]
     [:p [:a.landing-page-product-1
          {:href "/reference/v1/bls/ces/"
           :target "_blank"}
          :meca/product-1]]
     [:p :meca/product-1-desc]
     [:p :meca/product-1-desc-2]
     [:p :meca/outro]]]])

(spit "build/raw/index.clj"
      (format "(ns raw.index) \n \n %s"
       (meca/replace
        (meca/read-file "copy/index.meca")
        page)))
