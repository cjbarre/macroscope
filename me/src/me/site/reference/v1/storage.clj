(ns me.site.reference.v1.storage
  (:require [next.jdbc :as jdbc]))

(def db {:dbtype "postgresql"
         :dbname "macroscope"
         :user "macroscope_admin"
         :password "30e433a6-0732-4ae7-8e50-a0014f4aaa26"})

(def ds (jdbc/get-datasource db))