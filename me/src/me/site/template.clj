(ns me.site.template
  (:require [clojure.string :as string]))

(def vega-embed-js
  [:script {:src "https://cdn.jsdelivr.net/npm/vega-embed@6.2.1"}])

(defn site-template
  [{:keys [content title description author keywords tags] :as params}]
  [:html
   [:head
    [:meta {:charset "UTF-8"}]
    [:title (or title "Mortal Economics")]
    [:meta {:name "description" :content (or description "An economic research platform for the 21st century")}]
    [:meta {:name "author" :content (or author "Cameron Barré")}]
    (when keywords
      [:meta {:name "keywords" :content (string/join "," (into (set keywords) tags))}])
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "stylesheet" :type "text/css" :href "/assets/app.css"}]
    [:link {:rel "stylesheet" :type "text/css" :href "/assets/app2.css"}]
    [:script {:type "text/javascript" :src "https://cdn.jsdelivr.net/npm/vega@5.9.0"}]
    [:script {:type "text/javascript" :src "https://cdn.jsdelivr.net/npm/vega-lite@4.0.2"}]
    vega-embed-js]
   [:body
    [:div#main-container
     [:ul#header-container
      [:li.site-title [:a {:href "/"} "Mortal Economics"]]
      [:li.site-title [:a {:href "/about.html"} "About"]]
      #_[:li.site-title [:a {:href "#"} "FAQ"]]
      #_[:li.site-title [:a {:href "#"} "Blog"]]
      [:li.site-title
       [:a {:href "/reference/v1/"} "Open The Reference"]]]
     content
     [:div.footer-container
      "test"]]]])