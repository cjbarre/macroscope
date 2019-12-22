(ns me.site.reference.v1.bls.ln
  (:require [me.site.components.a-z-index :as az]
            [me.site.reference.v1.storage :as s]
            [me.site.render :as render]
            [clojure.java.io :as io]
            [next.jdbc :as jdbc]
            [clojure.data.json :as json]))

(defn generate-indices []
  (let [a-z-data (map #(update % :items
                               (fn [x] (->> (json/read-str (str x) :key-fn keyword)
                                            (map (fn [item] {:target (format "./%s.html" (:value item))
                                                             :text (:text item)})))))
                      (jdbc/execute! s/ds [(slurp "sql/reference/v1/bls/ln/index.sql")]))]
    (spit "build/site/reference/v1/bls/ln/index.html"
          (render/->html
           {:title "The Reference > Bureau of Labor Statistics > Current Population Survey (Labor Statistics)"
            :description "Browse labor statistics from the Current Population Survey by industry"
            :content (az/a-z-index {:title "Current Population Survey (Labor Statistics)"
                                    :source "Bureau of Labor Statistics"
                                    :content a-z-data})}))))

(defn generate []
  (.mkdirs (io/file "build/site/reference/v1/bls/ln/"))
  (generate-indices))