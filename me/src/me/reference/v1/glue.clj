(ns me.reference.v1.glue)

(defn slug [s]
  (-> s
      (clojure.string/replace #"\W+" "-")
      (clojure.string/lower-case)))