(ns me.site.components.classic-page
  (:require [me.site.components.author :as a]))

(defn classic-page [content]
  [:div.page-outer-container
   [:div.page-inner-container
    [:div.page-title :meca/title]
    a/author
    [:div.page-content
     content]]])