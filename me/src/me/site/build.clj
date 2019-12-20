(ns me.site.build
  (:require [me.site 
             [index :as index]
             [tos :as tos]]
            [me.site.reference.v1.bls.ces.generator :as bls-ces]
            [me.site.reference.v1.census.naics-2017.generator :as naics-2017]
            [clojure.java.io :as io]))

(defn build []
  (.mkdirs (io/file "build/site/"))
  (index/generate)
  (tos/generate)
  (naics-2017/generate)
  (bls-ces/generate))

#_(build)