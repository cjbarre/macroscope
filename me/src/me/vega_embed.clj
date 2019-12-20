(ns me.vega-embed
  (:require [clojure.data.json :as json]))

(defn vega-embed
  "Embed a specific visualization."
  ([[mode spec]]
   (let [id (str "viz-" (java.util.UUID/randomUUID))
         code (format "vegaEmbed('#%s', %s, %s);" 
                      id
                      (json/write-str spec :key-fn name) 
                      (json/write-str {:mode mode} :key-fn name))]
     [:div
      [:div {:id id :style "width:100%;padding-right:0;"}]
      [:script {:type "text/javascript"} code]])))