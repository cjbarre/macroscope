(ns me.reference.v1.index
  (:require [me.reference.v1.glue :as glue]))

(defn ees-alphabet-index [index]
  [:div#body-container
   [:h2 "Establishment Employment Situation"]
   [:h3 "Explore by industry"]
   (vec (concat [:ul {:style "padding:0;margin-top:2em"}]
                (mapv #(vector :li
                               {:style "display:inline;margin-left:1em;font-size:1.5em;"}
                               [:a
                                {:href (format "#%s" (:letter %))}
                                (:letter %)])
                      index)))
   (vec (concat [:div]
                (mapv #(vector :div
                               [:h3 [:a {:name (:letter %)} (:letter %)]]
                               (vec (concat [:ul {:style "list-style:none;padding-left:0;"}]
                                            (mapv (fn [x] [:li {:style "font-size:1.2em;"}
                                                           [:a {:href (format "index/%s-index.html" (glue/slug (:industry_name x)))}
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