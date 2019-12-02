(ns me.static
  (:require [next.jdbc :as jdbc]
            [clojure.core.reducers :as r]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
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
  (.mkdirs (io/file "build/site/charts/ees/data"))
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

(defn slug [s]
  (-> s
      (clojure.string/replace #"\W+" "-")
      (clojure.string/lower-case)))

(defn generate-static-ees-pages []
  (.mkdirs (io/file "build/raw/charts/ees"))
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

;;;;;;;;;;;;;;;;;;;;;;;;
;;; Index Generation ;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defn ees-alphabet-index [index]
  [:div
   [:h2 "Establishment Employment Situation"]
   [:h3 "Explore by industry"]
   (vec (concat [:ul {:style "padding:0;margin-top:2em"}]
                (mapv #(vector :li 
                               {:style "display:inline;margin-left:1em;font-size:1.5em;"} 
                               [:a 
                                {:href (format "#%s" (:letter %))} 
                                (:letter %)]) 
                      index)))
   (vec (concat [:div]
                (mapv #(vector :div
                               [:h3 [:a {:name (:letter %)} (:letter %)]]
                               (vec (concat [:ul {:style "list-style:none;padding-left:0;"}] 
                                            (mapv (fn [x] [:li {:style "font-size:1.2em;"} 
                                                           [:a {:href (format "index/%s-index.html" (slug (:industry_name x)))}
                                                            (:industry_name x)]])
                                                  (:industries %)))))
                      index)))])

(defn ees-industry-index [industry-index]
  [:div
   [:h2 (:industry_name industry-index)]
   [:h3 "Explore industry-specific data series"]
   (vec (concat [:ul {:style "list-style:none;padding-left:0;"}]
                (mapv #(vector :li [:a {:href (format "../%s.html" 
                                                      (slug (str (:series_id %) "-" (:series_title %))))}
                                    (:series_title %)])
                      (:series industry-index))))])

(defn generate-ees-indicies []
  (.mkdirs (io/file "build/raw/charts/ees/index"))
  (let [index (map #(update % :industries (fn [x] (json/read-str (str x) :key-fn keyword)))
                   (jdbc/execute! ds [(slurp "sql/ees-industries-index.sql")]))]
    (spit "build/raw/charts/ees/index.clj"
          (format "(ns raw.charts.ees.index) \n \n %s"
                  (ees-alphabet-index index)))
    (mapv #(spit (format "build/raw/charts/ees/index/%s-index.clj"
                         (slug (:industry_name %)))
                 (format "(ns raw.charts.ees.index.%s) \n \n %s"
                         (slug (:industry_name %))
                         (ees-industry-index %)))
          (mapcat :industries index))))

;;;;;;;;;;
;;; Oz ;;;
;;;;;;;;;;

(def vega-embed-js
  [:script {:src "https://cdn.jsdelivr.net/npm/vega-embed@5.0.0"}])

(defn site-template
  [content]
  [:div.grid
   [:link {:rel "stylesheet" :type "text/css" :href "/assets/app.css"}]
   vega-embed-js
   [:div.grid.header
    [:span.brand "Economics for Mortals"]]
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
  (generate-ees-indicies)
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