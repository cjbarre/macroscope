(ns me.site.reference.v1.storage
  (:require [next.jdbc :as jdbc]
            [clojure.string :as string]
            [next.jdbc.result-set :as rs]))

(def db {:dbtype "postgresql"
         :dbname "macroscope"
         :user "macroscope_admin"
         :password "30e433a6-0732-4ae7-8e50-a0014f4aaa26"})

(def ds (jdbc/get-datasource db))

(defn query 
  "file-path is a keyword."
  [file-path] 
  (jdbc/execute! ds 
                 [(slurp (format "sql/%s.sql" 
                                 (-> file-path name str (string/replace #"\." "/"))))]
                 {:builder-fn rs/as-unqualified-lower-maps}))