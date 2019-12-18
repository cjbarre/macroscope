(ns me.site.reference.v1.bls.glue)

(defn slug [s]
  (-> s
      (clojure.string/replace #"\W+" "-")
      (clojure.string/lower-case)))