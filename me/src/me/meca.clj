(ns me.meca
  (:require [clojure.string :as string]
            [clojure.walk :as walk]))

(defn read-str [s]
  (-> s
      (string/replace #"\n" "")
      (string/split #"---")
      (->> (map #(let [[name content] (string/split % #"\|\|\|")]
                   {(keyword "meca" name) content}))
           (into {}))))

(defn read-file [f]
  (-> f slurp read-str))

(defn replace [smap coll]
  (walk/prewalk-replace smap coll))