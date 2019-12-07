(ns me.reference.v1.generator
  (:require [next.jdbc :as jdbc]
            [clojure.core.reducers :as r]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [oz.core :as oz]
            [me.reference.v1.template :as p]
            [me.reference.v1.index :as i]
            [me.reference.v1.glue :as glue]
            [clojure.java.shell :as sh]))

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
                             (jdbc/execute! ds [(slurp "sql/ees-data.sql")])))))
  (time (into []
              (r/fold conj
                      (r/map (fn [x]
                               (spit (format "build/site/reference/v1/bls/ces/data/%s-roc12.json" (:ees_data/series_id x))
                                     (-> x :data str)))
                             (jdbc/execute! ds [(slurp "sql/ees-roc12-data.sql")]))))))

#_(generate-static-ees-data)




;;;;;;;;;;;;;;;;;;;;;;;
;;; Page Generation ;;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn generate-static-ees-pages []
  (.mkdirs (io/file "build/raw/reference/v1/bls/ces/"))
  (time (into [] 
              (r/fold conj 
                      (r/map (fn [x] (spit (format "build/raw/reference/v1/bls/ces/%s.clj"
                                                   (-> (:ees_series/series_id x)
                                                       (str "-" (:ees_series/series_title x))
                                                       (clojure.string/replace #"\W+" "-")
                                                       (clojure.string/lower-case)))
                                           (format "(ns raw.reference.v1.bls.ces.%s) \n\n %s"
                                                   (:ees_series/series_id x)
                                                   (p/page (:ees_series/series_id x) (:ees_series/series_title x)))))
                             (jdbc/execute! ds [(slurp "sql/ees-series.sql")]))))))

#_(generate-static-ees-pages)

;;;;;;;;;;;;;;;;;;;;;;;;
;;; Index Generation ;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defn generate-ees-indicies []
  (.mkdirs (io/file "build/raw/reference/v1/bls/ces/index"))
  (let [index (map #(update % :industries (fn [x] (json/read-str (str x) :key-fn keyword)))
                   (jdbc/execute! ds [(slurp "sql/ees-industries-index.sql")]))]
    (spit "build/raw/reference/v1/bls/ces/index.clj"
          (format "(ns raw.reference.v1.bls.ces.index) \n \n %s"
                  (i/ees-alphabet-index index)))
    (mapv #(spit (format "build/raw/reference/v1/bls/ces/index/%s-index.clj"
                         (glue/slug (:industry_name %)))
                 (format "(ns raw.reference.v1.bls.ces.index.%s) \n \n %s"
                         (glue/slug (:industry_name %))
                         (i/ees-industry-index %)))
          (mapcat :industries index))))

;;;;;;;;;;
;;; Oz ;;;
;;;;;;;;;;

(def vega-embed-js
  [:script {:src "https://cdn.jsdelivr.net/npm/vega-embed@5.0.0"}])

(defn site-template
  [content]
  [:div#main-container
   vega-embed-js
   [:link {:rel "stylesheet" :type "text/css" :href "/assets/app.css"}]
   [:div#header-container
    [:div#site-title "Mortal Economics"]]
   content])

(defn generate-ees-static-site []
  (oz/build!
         [{:from "build/raw/"
           :to "build/site/"
           :template-fn #'site-template}
          {:from "assets/"
           :to "build/site/assets/"
           :as-assets? true}]
         :live? false
         :lazy? false
         :view? false))


;;;;;;;;;;;;
;;; Main ;;;
;;;;;;;;;;;;

(defn generate-site []
  (generate-static-ees-data)
  (generate-static-ees-pages)
  (generate-ees-indicies)
  (generate-ees-static-site))

#_(generate-site)
   

(comment

  (oz/build!
   [{:from "build/raw/"
     :to "build/site/"
     :template-fn #'site-template}
    {:from "assets/"
     :to "build/site/assets/"
     :as-assets? true}])
  
  
  

  (oz/start-server!)


  (-> (map #(-> %
                :ees_series/series_title
                (clojure.string/replace #"\W+" "-")
                (str "-" (:ees_series/series_id %))
                (clojure.string/lower-case))
           (jdbc/execute! ds [(slurp "sql/ees-series.sql")]))
      (into #{})
      count))