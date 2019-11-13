(ns me.core
  (:require [oz.core :as oz]
            [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [em.web-resources :as wr]
            [clojure.core.reducers :as r]))

;;;;;;;;;;;;;;;;;;;;;;;
;;; Data Extraction ;;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn parse-content-range [http-resp]
  (let [[start end length] (-> (get (:headers http-resp) "Content-Range")
                               (string/split #"-|/"))]
    {:start start :end end :length length}))

(defn data-lazy-seq [table query start-pos & {:keys [resp-format] :or {resp-format "application/json"}}]
  (let [resp (http/get (format "http://localhost/api/v1/%s?%s" table query)
                       {:headers {"Range-Unit" "items"
                                  "Range" (format "%s-" start-pos)
                                  "Accept" resp-format}})
        end-range (try (Integer/parseInt (:end (parse-content-range resp)))
                       (catch Exception e nil))]
    (when end-range
      (lazy-seq 
       (cond (= "application/json" resp-format) (concat
                                                 (json/read-str (:body resp) :key-fn keyword)
                                                 (data-lazy-seq table query (inc end-range) :resp-format resp-format))
             :else (cons (:body resp)
                         (data-lazy-seq table query (inc end-range) :resp-format resp-format)))))))



;;;;;;;;;;;;;;;;;
;;; Generator ;;;
;;;;;;;;;;;;;;;;;

(defn ees-series [] (data-lazy-seq "ees_series" "" 0))

#_(spit "site/build/charts/data/CES0000000001.json" 
      (json/write-str (data-lazy-seq "ees_data_wrap"
                                     "series_id=eq.CES0000000001"
                                     0) :key-fn name))

(defn page [series-id series-title]
  [:div
   [:vega-lite {:height 400
                :width 500
                :title series-title
                :data {:url (format "/charts/data/%s.json" series-id)}
                :mark {:type "line"
                       :interpolate "monotone"}
                :encoding {:x {:field "effective_month" :type "temporal" :timeUnit "utcyearmonth"}
                           :y {:field "value" :type "quantitative"}}}]])

#_(time (into [] (r/fold conj (r/map (fn [x] (spit (format "site/build/charts/data/%s.json" (:series_id x))
                                              (json/write-str (data-lazy-seq "ees_data_wrap"
                                                                             (format "series_id=eq.%s" (:series_id x))
                                                                             0)
                                                              :key-fn name)))
                                (into [] (ees-series))))))

;; Generate Pages

#_(time (into [] (r/fold conj (r/map (fn [x] (spit (format "site/src/charts/%s.clj"
                                                           (:series_id x) ".clj")
                                                   (format "(ns site.charts.%s) \n\n %s"
                                                           (:series_id x)
                                                           (page (:series_id x) (:series_title x)))))
                                     (into [] (ees-series))))))

;;;;;;;;;;
;;; Oz ;;;
;;;;;;;;;;

(defn site-template
  [content]
  [:div
   wr/vega-embed-js
   [:h1 "Economics for Mortals"]
   content])

#_(oz/build!
   [{:from "site/src/"
     :to "site/build/"
     :template-fn site-template}]
   :live? false)

(comment
  (oz/start-server!)
  )

