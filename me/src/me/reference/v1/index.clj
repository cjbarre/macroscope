(ns me.reference.v1.index
  (:require [me.reference.v1.glue :as glue]))

(defn ees-alphabet-index [index]
  [:div#body-container
   [:div#series-title "Establishment Survey"]
   [:div#series-source "Bureau of Labor Statistics"]
   [:div.index-cta "browse by industry"]
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
  [:div
   [:h2 (:industry_name industry-index)]
   [:h3 "Explore industry-specific data series"]
   (vec (concat [:ul {:style "list-style:none;padding-left:0;"}]
                (mapv #(vector :li [:a {:href (format "../%s.html"
                                                      (glue/slug (str (:series_id %) "-" (:series_title %))))}
                                    (:series_title %)])
                      (:series industry-index))))])