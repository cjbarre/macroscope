(ns stress-test.core
  (:require [clj-http.client :as http]
            [clojure.string :as string]))

(defn parse-content-range [http-resp]
  (let [[start end length] (-> (get (:headers http-resp) "Content-Range")
                               (string/split #"-|/"))]
    {:start start :end end :length length}))

(defn data-lazy-seq [table max-page cur-page start-pos]
  (when (<= cur-page max-page)
    (let [resp (http/get (format "http://macroscope.cameronbarre.com/api/v1/%s" table)
                         {:headers {"Range-Unit" "items"
                                    "Range" (format "%s-" start-pos)
                                    "Accept" "text/csv"}})
          end-range (Integer/parseInt (:end (parse-content-range resp)))]
      (lazy-seq (cons (:body resp) (data-lazy-seq table max-page (inc cur-page) (inc end-range)))))))

(comment
  (first (data-lazy-seq "ees_data" 1 1 0))

  (count (data-lazy-seq "ees_data" 2 1 0))
  
  (time (def t (doall (data-lazy-seq "ees_data" 1000 1 0))))
  
  (future (doall (data-lazy-seq "ees_data" 1000 1 0)))
  
  (future (doall (data-lazy-seq "ees_data" 1000 1 0)))
  
  )