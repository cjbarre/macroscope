(ns me.static
  (:require [next.jdbc :as jdbc]
            [clojure.core.reducers :as r]
            [clojure.data.json :as json]
            [oz.core :as oz]))

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

(defn page [series-id series-title]
  [:div
   [:vega-lite {:height 400
                :width 500
                :title series-title
                :data {:url (format "data/%s.json" series-id)}
                :mark {:type "line"
                       :interpolate "monotone"}
                :encoding {:x {:field "period" :type "temporal" :timeUnit "utcyearmonth"}
                           :y {:field "value" :type "quantitative"}}}]])

(defn generate-static-ees-pages []
  (time (into [] 
              (r/fold conj 
                      (r/map (fn [x] (spit (format "build/raw/charts/ees/%s-%s.clj"
                                                   (clojure.string/lower-case 
                                                    (clojure.string/replace (:ees_series/series_title x) #"\W+" "-"))
                                                   (:ees_series/series_id x))
                                           (format "(ns raw.charts.ees.%s) \n\n %s"
                                                   (:ees_series/series_id x)
                                                   (page (:ees_series/series_id x) (:ees_series/series_title x)))))
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
  (generate-static-ees-data)
  (generate-static-ees-pages)
  (generate-ees-static-site))

#_(generate-site)
