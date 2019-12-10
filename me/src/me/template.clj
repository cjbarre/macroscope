(ns me.template)

(def vega-embed-js
  [:script {:src "https://cdn.jsdelivr.net/npm/vega-embed@5.0.0"}])

(defn site-template
  [content]
  [:div#main-container
   vega-embed-js
   [:link {:rel "stylesheet" :type "text/css" :href "/assets/app.css"}]
   [:div#header-container
    [:div.site-title "Mortal Economics"]
    [:div.site-title "|"]
    [:div.site-title [:a {:href "#"} "FAQ"]]
    [:div.site-title "|"]
    [:div.site-title [:a {:href "#"} "Terms"]]
    [:div.site-title "|"]
    [:div.site-title
     [:a {:href "/reference/v1" :target "_blank"} "Open The Reference"]]]
   content])