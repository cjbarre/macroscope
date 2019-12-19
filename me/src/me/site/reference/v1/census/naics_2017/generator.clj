(ns me.site.reference.v1.census.naics-2017.generator
  (:require [me.site.reference.v1.storage :as s]
            [me.site.components.author :as a]
            [clojure.java.io :as io]
            [clojure.core.reducers :as r]
            [next.jdbc :as jdbc]
            [clojure.data.json :as json]
            [clojure.string :as string]))

(defn page [data]
  [:div.page-outer-container
   [:div.page-inner-container
    [:div.page-title (:naics_2017_descriptions/title data)]
    a/author
    [:div.page-content
     (vec (concat [:div]
                  (-> data
                      :description
                      (string/split #"\n")
                      (->> (filter #(not= "" %))
                           (map #(vector :p %))))))
     (when (:cross_references data)
       (vec (concat [:ul]
                    (-> data
                        :cross_references
                        str
                        json/read-str
                        (->> (map #(vector :li (string/replace % #"[0-9]{3,6}" 
                                                               (format "<a href='%s.html'>%s</a>" 
                                                                       (re-find #"[0-9]{3,6}" %)
                                                                       (re-find #"[0-9]{3,6}" %))))))))))
     (when (:index_items data)
       [:div
        [:p [:b "Examples"]]
        (vec (concat [:ul]
                     (-> data
                         :index_items
                         str
                         json/read-str
                         (->> (map #(vector :li %))))))])]]])

(defn generate-pages []
  (.mkdirs (io/file "build/raw/reference/v1/census/naics-2017/"))
  (time (into []
              (r/fold conj
                      (r/map (fn [x] (spit (format "build/raw/reference/v1/census/naics-2017/%s.clj"
                                                   (:naics_code x))
                                           (format "(ns raw.reference.v1.census.naics-2017.%s) \n\n %s"
                                                   (:naics_code x)
                                                   (page x))))
                             (jdbc/execute! s/ds [(slurp "sql/reference/v1/census/naics-2017/pages.sql")]))))))

#_(generate-pages)

(comment )