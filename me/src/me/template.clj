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
    #_[:div.site-title "|"]
    [:li.site-title [:a {:href "#"} "FAQ"]]
    #_[:div.site-title "|"]
    [:li.site-title [:a {:href "#"} "Terms"]]
    #_[:div.site-title "|"]
    [:li.site-title
     [:a {:href "/reference/v1/bls/ces/" :target "_blank"} "Open The Reference"]]]
   content])