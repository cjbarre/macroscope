(ns me.build
  (:require [oz.core :as oz]
            [me.site.template :as t]))

(defn start-dynamic-build! []
  (oz/build!
   [{:from "build/raw/"
     :to "build/site/"
     :template-fn #'t/site-template}
    {:from "assets/"
     :to "build/site/assets/"
     :as-assets? true}]))

(defn start-production-build! []
  (oz/build!
   [{:from "build/raw/"
     :to "build/site/"
     :template-fn #'t/site-template}
    {:from "assets/"
     :to "build/site/assets/"
     :as-assets? true}]
   :live? false
   :lazy? false
   :view? false))

#_(start-dynamic-build!)