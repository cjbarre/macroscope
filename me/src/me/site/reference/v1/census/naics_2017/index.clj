(ns me.site.reference.v1.census.naics-2017.index
  (:require [me.site.reference.v1.bls.glue :as glue]))

(defn a-z-index [a-z-data]
  [:div.page-outer-container
   [:div#series-title "NAICS 2017"]
   [:div#series-source "Census Bureau"]
   [:div.index-a2z-container
    (vec (concat [:ul]
                 (mapv #(vector :li
                                [:a.index-a.index-text-hover
                                 {:href (format "#%s" (:letter %))}
                                 (:letter %)])
                       a-z-data)))]
   (vec (concat [:div.index-a2z-contents]
                (mapv #(vector :div.index-a2z-section
                               [:div [:a.index-a {:name (:letter %)} (:letter %)]]
                               (vec (concat [:ul]
                                            (mapv (fn [x] [:li
                                                           [:a.index-a.index-text-hover {:href (format "./%s.html" (glue/slug (:value x)))}
                                                            (format "<b>%s</b> %s" (:value x) (:text x))]])
                                                  (:items %)))))
                      a-z-data)))])