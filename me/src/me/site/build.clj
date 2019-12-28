(ns me.site.build
  (:require [me.site 
             [index :as index]
             [tos :as tos]
             [about :as about]]
            [me.site.reference.v1.index :as reference-index]
            [me.site.reference.v1.bls.ces.generator :as bls-ces]
            [me.site.reference.v1.census.naics-2017.generator :as naics-2017]
            [clojure.java.io :as io]))

(defn build []
  (.mkdirs (io/file "build/site/"))
  (index/generate)
  (about/generate)
  (tos/generate)
  (naics-2017/generate)
  (reference-index/generate)
  (bls-ces/generate))

#_(build)
