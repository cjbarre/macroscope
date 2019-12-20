(ns me.site.reference.v1.census.naics-2017.generator
  (:require [me.site.reference.v1.storage :as s]
            [me.site.components 
             [author :as a]
             [a-z-index :as azi]]
            [me.site.render :as render]
            [clojure.java.io :as io]
            [clojure.core.reducers :as r]
            [next.jdbc :as jdbc]
            [clojure.data.json :as json]
            [clojure.string :as string]))

(defn page [data]
  [:div.page-outer-container
   [:div.page-inner-container
    [:div.page-title (:title data)]
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
  (time (into []
              (r/fold conj
                      (r/map (fn [x] (spit (format "build/site/reference/v1/census/naics-2017/%s.html"
                                                   (:naics_code x))
                                           (render/->html {:title (format "The Reference > NAICS > (%s) %s "
                                                                          (:naics_code x)
                                                                          (:title x))
                                                           :content (page x)})))
                             (jdbc/execute! s/ds [(slurp "sql/reference/v1/census/naics-2017/pages.sql")]))))))

;;; Index Generation ;;;

(defn generate-indices []
  (let [a-z-data (map #(update % :items
                               (fn [x] (->> (json/read-str (str x) :key-fn keyword)
                                            (map (fn [item] {:target (format "./%s.html" (:value item))
                                                             :text (format "<b>%s</b> %s"
                                                                           (:value item)
                                                                           (:text item))})))))
                      (jdbc/execute! s/ds [(slurp "sql/reference/v1/census/naics-2017/a-z-index.sql")]))]
    (spit "build/site/reference/v1/census/naics-2017/index.html"
          (render/->html
           {:title "The Reference > Census Bureau > NAICS 2017 > Index"
            :content (azi/a-z-index a-z-data)}))))

(defn generate []
  (.mkdirs (io/file "build/site/reference/v1/census/naics-2017/"))
  (generate-pages)
  (generate-indices))