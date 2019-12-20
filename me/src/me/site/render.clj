(ns me.site.render
  (:require [hiccup.core :as h]
            [me.site.template :as t]))

(defn ->html [spec]
  (h/html (t/site-template spec)))