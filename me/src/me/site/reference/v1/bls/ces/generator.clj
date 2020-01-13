(ns me.site.reference.v1.bls.ces.generator
  (:require [next.jdbc :as jdbc]
            [clojure.core.reducers :as r]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [me.site.render :as render]
            [me.site.reference.v1.bls.ces.template :as p]
            [me.site.reference.v1.bls.ces.index :as i]
            [me.site.reference.v1.bls.glue :as glue]))


;;;;;;;;;;;;;;;;;;;;;;;
;;; Data Generation ;;;
;;;;;;;;;;;;;;;;;;;;;;;

(def db {:dbtype "postgresql"
         :dbname "macroscope"
         :user "macroscope_admin"
         :password "30e433a6-0732-4ae7-8e50-a0014f4aaa26"})

(def ds (jdbc/get-datasource db))

(defn generate-static-ees-data []
  (.mkdirs (io/file "build/site/reference/v1/bls/ces/data"))
  (time (into [] 
              (r/fold conj
                      (r/map (fn [x]
                               (spit (format "build/site/reference/v1/bls/ces/data/%s.json" (:ees_data/series_id x))
                                     (-> x :data str)))
                             (jdbc/execute! ds [(slurp "sql/reference/v1/bls/ces/ees-data.sql")])))))
  (time (into []
              (r/fold conj
                      (r/map (fn [x]
                               (spit (format "build/site/reference/v1/bls/ces/data/%s-roc12.json" (:ees_data/series_id x))
                                     (-> x :data str)))
                             (jdbc/execute! ds [(slurp "sql/reference/v1/bls/ces/ees-roc12-data.sql")]))))))

#_(generate-static-ees-data)



;;;;;;;;;;;;;;;;;;;;;;;
;;; Page Generation ;;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn generate-static-ees-pages []
  (.mkdirs (io/file "build/site/reference/v1/bls/ces/"))
  (time (doall (pmap (fn [x] (spit (format "build/site/reference/v1/bls/ces/%s.html"
                                           (glue/slug (str (:ees_series/series_id x)
                                                           "-"
                                                           (:ees_series/series_title x))))
                                   (render/->html
                                    {:title (format "%s > %s [CES]"
                                                    (:ees_industry/industry_name x)
                                                    (:ees_series/series_title x))
                                     :description (format "View \"%s\" in \"%s\" from the U.S. Bureau of Labor Statistics, Current Establishment Statistics series"
                                                          (:ees_series/series_title x)
                                                          (:ees_industry/industry_name x))
                                     :content (p/page (:ees_series/series_id x)
                                                      (:ees_series/series_title x)
                                                      (:ees_industry/naics_code x))})))
                     (jdbc/execute! ds [(slurp "sql/reference/v1/bls/ces/ees-series.sql")]))))
  nil)


#_(generate-static-ees-pages)
;;;;;;;;;;;;;;;;;;;;;;;;
;;; Index Generation ;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defn generate-ees-indicies []
  (.mkdirs (io/file "build/site/reference/v1/bls/ces/index"))
  (let [index (map #(update % :industries (fn [x] (json/read-str (str x) :key-fn keyword)))
                   (jdbc/execute! ds [(slurp "sql/reference/v1/bls/ces/ees-industries-index.sql")]))]
    (spit "build/site/reference/v1/bls/ces/index.html"
          (render/->html
           {:title "Discover the CES series from the BLS | Mortal Economics"
            :description "Browse the Current Establishment Statistics from the U.S. Bureau of Labor Statistics in our visual reference."
            :content (i/ees-alphabet-index index)}))
    
    (mapv #(spit (format "build/site/reference/v1/bls/ces/index/%s-index.html"
                         (glue/slug (:industry_name %)))
                 (render/->html
                  {:title (format "View \"%s\" data [CES] | Mortal Economics"
                                  (:industry_name %))
                   :description (format "View data on \"%s\" from the Current Establishment Statistics from the U.S. BLS"
                                        (:industry_name %))
                   :content (i/ees-industry-index %)}))
          (mapcat :industries index))))

;;;;;;;;;;
;;; Oz ;;;
;;;;;;;;;;

;;;;;;;;;;;;
;;; Main ;;;
;;;;;;;;;;;;

(defn generate []
  (generate-static-ees-data)
  (generate-static-ees-pages)
  (generate-ees-indicies))

