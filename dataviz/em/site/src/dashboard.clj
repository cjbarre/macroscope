(ns site.dashboard
  (:require [clojure.string :as string]
            [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.string :as string]))

(defn parse-content-range [http-resp]
  (let [[start end length] (-> (get (:headers http-resp) "Content-Range")
                               (string/split #"-|/"))]
    {:start start :end end :length length}))

(defn data-lazy-seq [table query start-pos]
  (let [resp (http/get (format "http://localhost/api/v1/%s?%s" table query)
                       {:headers {"Range-Unit" "items"
                                  "Range" (format "%s-" start-pos)}})
        end-range (try (Integer/parseInt (:end (parse-content-range resp)))
                       (catch Exception e nil))]
    (when end-range
      (lazy-seq (concat (json/read-str (:body resp) :key-fn keyword)
                        (data-lazy-seq table query (inc end-range)))))))

(def manufacturing-01
  {:height 400
   :width 500
   :title "All employees, thousands"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES3000000001)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def manufacturing-02
  {:height 400
   :width 500
   :title "Average weekly hours of all employees"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES3000000002)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def manufacturing-03
  {:height 400
   :width 500
   :title "Average weekly overtime hours of all employees"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES3000000004)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def trucking-01
  {:height 400
   :width 500
   :title "All employees, thousands"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES4348400001)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def trucking-02
  {:height 400
   :width 500
   :title "Average weekly hours of all employees"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES4348400002)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def child-care-01
  {:height 400
   :width 500
   :title "All employees, thousands"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES6562440001)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def child-care-02
  {:height 400
   :width 500
   :title "Average weekly hours of all employees"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES6562440002)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def temp-01
  {:height 400
   :width 500
   :title "All employees, thousands"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES6056132001)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def temp-02
  {:height 400
   :width 500
   :title "Average weekly hours of all employees"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "ees_data" "series_id=in.(CES6056132002)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def unemployment-01
  {:height 400
   :width 500
   :title "Unemployment Rate"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS14000000)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def unemployment-02
  {:height 400
   :width 500
   :title "Real Unemployment Rate"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS13327709)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def job-leavers-01
  {:height 400
   :width 500
   :title "Percentage of Unemployed, Quit"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS13023706)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def job-losers-01
  {:height 400
   :width 500
   :title "Percentage of Unemployed, Not on Layoff"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS13026511)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def job-losers-02
  {:height 400
   :width 500
   :title "Percentage of Unemployed, on Layoff"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS13023654)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def time-spent-out-01
  {:height 400
   :width 500
   :title "Less than 5 weeks, Percentage of Unemployed"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS13008397)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def time-spent-out-02
  {:height 400
   :width 500
   :title "5-14 weeks, Percentage of Unemployed"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS13025701)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def time-spent-out-03
  {:height 400
   :width 500
   :title "15-26 weeks"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS13025702)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

(def time-spent-out-04
  {:height 400
   :width 500
   :title "27+ weeks"
   :data {:values (mapv #(assoc % :month (format "%s-%s" (:year %) (-> % :period rest string/join)))
                        (data-lazy-seq "hes_data" "series_id=in.(LNS13025703)&year=gte.2000" 0))}
   :mark {:type "line"}
   :encoding {:x {:field "month" :type "temporal"}
              :y {:field "value" :type "quantitative"}}})

;;;;;;;;;;
;; Page ;;
;;;;;;;;;;

[:div
 [:h2 "Macroscope Dashboard"]
 [:p "Find your bearings with this collection of key macro-economic indicators."]

 [:div
  [:h2 "Unemployment Rate"]
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite unemployment-01]
   [:vega-lite unemployment-02]]]

 [:div   
  [:h2 "Jobs"]
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite job-leavers-01]
   [:vega-lite job-losers-01]
   [:vega-lite job-losers-02]]]

 [:div
  [:h2 "Time Spent Unemployed"]
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite time-spent-out-01]
   [:vega-lite time-spent-out-02]]
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite time-spent-out-03]
   [:vega-lite time-spent-out-04]]]

 [:div
  [:h2 "Manufacturing"]
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite manufacturing-01]
   [:vega-lite manufacturing-02]
   [:vega-lite manufacturing-03]]]

 [:div
  [:h2 "Trucking"]
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite trucking-01]
   [:vega-lite trucking-02]]]

 [:div
  [:h2 "Child Care"]
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite child-care-01]
   [:vega-lite child-care-02]]]

 [:div
  [:h2 "Temp Workers"]
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite temp-01]
   [:vega-lite temp-02]]]]
   




