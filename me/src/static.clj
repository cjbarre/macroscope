(ns me.static
  (:require [next.jdbc :as jdbc]
            [clojure.core.reducers :as r]
            [clojure.data.json :as json]
            [oz.core :as oz]
            [me.page :as p]))

;;;;;;;;;;;;;;;;;;;;;;;
;;; Data Generation ;;;
;;;;;;;;;;;;;;;;;;;;;;;

(def db {:dbtype "postgresql"
         :dbname "macroscope"
         :user "macroscope_admin"
         :password "30e433a6-0732-4ae7-8e50-a0014f4aaa26"})

(def ds (jdbc/get-datasource db))

(defn generate-static-ees-data []
  (time (into [] 
              (r/fold conj
                      (r/map (fn [x]
                               (spit (format "build/site/charts/ees/data/%s.json" (:ees_data/series_id x))
                                     (-> x :data str)))
                             (jdbc/execute! ds [(slurp "sql/ees-data.sql")]))))))

#_(generate-static-ees-data)

;;;;;;;;;;;;;;;;;;;;;;;
;;; Page Generation ;;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn generate-static-ees-pages []
  (time (into [] 
              (r/fold conj 
                      (r/map (fn [x] (spit (format "build/raw/charts/ees/%s.clj"
                                                   (-> (:ees_series/series_id x)
                                                       (str "-" (:ees_series/series_title x))
                                                       (clojure.string/replace #"\W+" "-")
                                                       (clojure.string/lower-case)))
                                           (format "(ns raw.charts.ees.%s) \n\n %s"
                                                   (:ees_series/series_id x)
                                                   (p/page (:ees_series/series_id x) (:ees_series/series_title x)))))
                             (jdbc/execute! ds [(slurp "sql/ees-series.sql")]))))))

#_(generate-static-ees-pages) 

;;;;;;;;;;
;;; Oz ;;;
;;;;;;;;;;

(def vega-embed-js
  [:script {:src "https://cdn.jsdelivr.net/npm/vega-embed@5.0.0"}])

(defn site-template
  [content]
  [:div
   vega-embed-js
   [:h1 "Economics for Mortals"]
   content])

(defn generate-ees-static-site []
  (oz/build!
         [{:from "build/raw/"
           :to "build/site/"
           :template-fn site-template}]
         :live? false
         :lazy? false
         :view? false))


;;;;;;;;;;;;
;;; Main ;;;
;;;;;;;;;;;;

(defn generate-site []
  #_(generate-static-ees-data)
  (generate-static-ees-pages)
  (generate-ees-static-site))

#_(generate-site)
   

(comment

  (oz/build!
   [{:from "build/raw/"
     :to "build/site/"
     :template-fn site-template}])

  (oz/start-server!)


  (-> (map #(-> %
                :ees_series/series_title
                (clojure.string/replace #"\W+" "-")
                (str "-" (:ees_series/series_id %))
                (clojure.string/lower-case))
           (jdbc/execute! ds [(slurp "sql/ees-series.sql")]))
      (into #{})
      count))