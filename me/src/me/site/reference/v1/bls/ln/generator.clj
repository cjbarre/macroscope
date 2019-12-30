(ns me.site.reference.v1.bls.ln
  (:require [me.site.components.a-z-index :as az]
            [me.site.components.list-index :as li]
            [me.site.components.temporal-chart :as tc]
            [me.site.components.reference-page :as rp]
            [me.site.reference.v1.storage :as s]
            [me.site.reference.v1.bls.glue :as glue]
            [clojure.core.reducers :as r]
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
                                                                       (assoc :target 
                                                                              (format "../%s.html"
                                                                                      (glue/slug (str (:value item)
                                                                                                      "-"
                                                                                                      (:series_title item)))))
                                                                       (update :text clojure.string/replace (re-pattern (format "%s," (:title %))) "")))))))
                             (s/query :reference.v1.bls.ln.industry-index))]
    (mapv #(spit (format "build/site/reference/v1/bls/ln/index/%s-index.html"
                         (glue/slug (:title %)))
                 (render/->html
                  {:title (format "The Reference > Bureau of Labor Statistics > Current Population Survey (Labor Statistics) > %s"
                                  (:title %))
                   :content (li/list-index {:title (:title %)
                                            :source "Current Population Survey (Labor Statistics)"
                                            :content (:items %)})}))
          list-index-data)
    nil))

(defn generate-pages []
  (.mkdirs (io/file "build/site/reference/v1/bls/ln/"))
  (time (doall (pmap (fn [x] (spit (format "build/site/reference/v1/bls/ln/%s.html"
                                           (glue/slug (str (:hes_series/series_id x)
                                                           "-"
                                                           (:hes_series/series_title x))))
                                   (render/->html
                                    {:title (format "The Reference > Bureau of Labor Statistics > Current Population Survey > %s > %s"
                                                    (:hes_indy/indy_text x)
                                                    (:hes_series/series_title x))
                                     :content (rp/reference-page (:hes_series/series_id x)
                                                                 (:hes_series/series_title x)
                                                                 (:hes_industry/naics_code x)
                                                                 (:hes_series/periodicity_code x))})))
                     (jdbc/execute! s/ds [(slurp "sql/reference/v1/bls/ln/series.sql")]))))

  nil)

(defn generate-data []
  (.mkdirs (io/file "build/site/reference/v1/bls/ln/data"))
  (time (into []
              (r/fold conj
                      (r/map (fn [x]
                               (spit (format "build/site/reference/v1/bls/ln/data/%s.json" (:hes_data/series_id x))
                                     (-> x :data str)))
                             (jdbc/execute! s/ds [(slurp "sql/reference/v1/bls/ln/main-data.sql")])))))
  (time (into []
              (r/fold conj
                      (r/map (fn [x]
                               (spit (format "build/site/reference/v1/bls/ln/data/%s-roc12.json" (:hes_data/series_id x))
                                     (-> x :data str)))
                             (jdbc/execute! s/ds [(slurp "sql/reference/v1/bls/ln/roc12-data.sql")])))))

  nil)

(defn generate []
  (.mkdirs (io/file "build/site/reference/v1/bls/ln/"))
  (.mkdirs (io/file "build/site/reference/v1/bls/ln/index/"))
  (generate-index)
  (generate-industry-indices))