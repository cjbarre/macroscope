(ns me.site.reference.v1.bls.ln
  (:require [me.site.components.a-z-index :as az]
            [me.site.components.list-index :as li]
            [me.site.reference.v1.storage :as s]
            [me.site.reference.v1.bls.glue :as glue]
            [me.site.render :as render]
            [clojure.java.io :as io]
            [next.jdbc :as jdbc]
            [clojure.data.json :as json]))

(defn generate-index []
  (let [a-z-data (map #(update % :items
                               (fn [x] (->> (json/read-str (str x) :key-fn keyword)
                                            (map (fn [item] {:target (format "index/%s-index.html"
                                                                             (glue/slug (:text item)))
                                                             :text (:text item)})))))
                      (jdbc/execute! s/ds [(slurp "sql/reference/v1/bls/ln/index.sql")]))]
    (spit "build/site/reference/v1/bls/ln/index.html"
          (render/->html
           {:title "The Reference > Bureau of Labor Statistics > Current Population Survey (Labor Statistics)"
            :description "Browse labor statistics from the Current Population Survey by industry"
            :content (az/a-z-index {:title "Current Population Survey (Labor Statistics)"
                                    :source "Bureau of Labor Statistics"
                                    :content a-z-data})}))))

(defn generate-industry-indices []
  (let [list-index-data (map #(update % :items
                                      (fn [x] (->> (json/read-str (str x) :key-fn keyword)
                                                   (map (fn [item] (-> item
                                                                       (assoc :target (format "./%s.html" (:value item)))
                                                                       (update :text clojure.string/replace (re-pattern (format "%s,"(:hes_indy/title %))) "")))))))
                             (jdbc/execute! s/ds [(slurp "sql/reference/v1/bls/ln/industry-index.sql")]))]
    (mapv #(spit (format "build/site/reference/v1/bls/ln/index/%s-index.html"
                         (glue/slug (:hes_indy/title %)))
                 (render/->html
                  {:title (format "The Reference > Bureau of Labor Statistics > Current Population Survey (Labor Statistics) > %s"
                                  (:hes_indy/title %))
                   :content (li/list-index {:title (:hes_indy/title %)
                                            :source "Current Population Survey (Labor Statistics)"
                                            :content (:items %)})}))
          list-index-data)))


(defn generate []
  (.mkdirs (io/file "build/site/reference/v1/bls/ln/"))
  (.mkdirs (io/file "build/site/reference/v1/bls/ln/index/"))
  (generate-index)
  (generate-industry-indices))

#_(generate)