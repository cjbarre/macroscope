(ns me.site.components.a-z-index)

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
                                                           [:a.index-a.index-text-hover {:href (:target x)}
                                                            (:text x)]])
                                                  (:items %)))))
                      a-z-data)))])