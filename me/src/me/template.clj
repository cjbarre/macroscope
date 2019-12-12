(ns me.template)

(def vega-embed-js
  [:script {:src "https://cdn.jsdelivr.net/npm/vega-embed@5.0.0"}])

(defn site-template
  [content]
  [:div#main-container
   vega-embed-js
   [:link {:rel "stylesheet" :type "text/css" :href "/assets/app.css"}]
   [:ul#header-container
    [:li.site-title "Mortal Economics"]
    [:li.site-title [:a {:href "#"} "ToS"]]
    [:li.site-title [:a {:href "#"} "FAQ"]]
    [:li.site-title [:a {:href "#"} "Blog"]]
    [:li.site-title
     [:a {:href "/reference/v1/bls/ces/" :target "_blank"} "Open The Reference"]]]
   content])