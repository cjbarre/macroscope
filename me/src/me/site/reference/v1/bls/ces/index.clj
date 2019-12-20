(ns me.site.reference.v1.bls.ces.index
  (:require [me.site.reference.v1.bls.glue :as glue]))

(defn ees-alphabet-index [index]
  [:div.page-outer-container
   [:div#series-title "Current Establishment Situation"]
   [:div#series-source "Bureau of Labor Statistics"]
   [:div.index-a2z-container
    (vec (concat [:ul]
                 (mapv #(vector :li
                                [:a.index-a.index-text-hover
                                 {:href (format "#%s" (:letter %))}
                                 (:letter %)])
                       index)))]
   (vec (concat [:div.index-a2z-contents]
                (mapv #(vector :div.index-a2z-section
                               [:div [:a.index-a {:name (:letter %)} (:letter %)]]
                               (vec (concat [:ul]
                                            (mapv (fn [x] [:li
                                                           [:a.index-a.index-text-hover {:href (format "index/%s-index.html" (glue/slug (:industry_name x)))}
                                                            (:industry_name x)]])
                                                  (:industries %)))))
                      index)))])



(defn ees-industry-index [industry-index]
  [:div.page-outer-container
   [:div#series-title (:industry_name industry-index) ]
   [:div#series-source "Bureau of Labor Statistics" ]
   [:div.index-cta "Establishment Survey"]
   [:div.landing-page-article {:style "background-color: #F0F0F0;padding-top:15px;margin-top:50px;"}
    (vec (concat [:ul {:style "list-style:none;"}]
                 (mapv #(vector :li {:style "border-left: 2px gray solid;margin-bottom:25px;padding-left: 15px;"}
                                [:a {:style "color:black;"
                                     :href (format "../%s.html"
                                                   (glue/slug (str (:series_id %) "-" (:series_title %))))}
                                 (clojure.string/replace (:series_title %) 
                                                         (re-pattern (clojure.string/lower-case 
                                                                      (str (:industry_name industry-index) ","))) " ")])
                       (:series industry-index))))]])