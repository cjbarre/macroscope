(ns me.site.template)

(def vega-embed-js
  [:script {:src "https://cdn.jsdelivr.net/npm/vega-embed@6.2.1"}])

(defn site-template
  [content]
  [:div#main-container
   [:script {:type "text/javascript" :src "https://cdn.jsdelivr.net/npm/vega@5.9.0"}]
   [:script {:type "text/javascript" :src "https://cdn.jsdelivr.net/npm/vega-lite@4.0.2"}]
   vega-embed-js
   [:link {:rel "stylesheet" :type "text/css" :href "/assets/app.css"}]
   [:link {:rel "stylesheet" :type "text/css" :href "/assets/app2.css"}]
   [:ul#header-container
    [:li.site-title "Mortal Economics"]
    [:li.site-title [:a {:href "/tos.html"} "ToS"]]
    [:li.site-title [:a {:href "#"} "FAQ"]]
    [:li.site-title [:a {:href "#"} "Blog"]]
    [:li.site-title
     [:a {:href "/reference/v1/bls/ces/" :target "_blank"} "Open The Reference"]]]
   content])