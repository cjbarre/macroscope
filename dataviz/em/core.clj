(ns em.core
  (:require [oz.core :as oz]))

#_(def bootstrap-css
  [:link {:href "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          :rel "stylesheet"
          :integrity "sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          :crossorigin "anonymous"}])

#_(def jquery-js
  [:script {:src "https://code.jquery.com/jquery-3.3.1.slim.min.js"
            :integrity "sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            :crossorigin "anonymous"}])

#_(def bootstrap-js
  [:script {:src "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js"
            :integrity "sha384-xrRywqdh3PHs8keKZN+8zzc5TX0GRTLCcmivcbNJWm2rs5C8PRhcEn3czEjhAO9o"
            :crossorigin "anonymous"}])

(def nav
  [:nav.navbar.navbar-expand-lg.navbar-dark.bg-dark
   [:span.navbar-brand.mb-0 "Economics for Mortals"]])

(defn site-template
  [content]
  [:div
   [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/vega-embed/5.0.0/vega-embed.js"}]
   [:h1 "Economics for Mortals"]
   content])

(oz/build!
 [{:from "site/src/"
   :to "site/build/"
   :template-fn site-template}])

(comment
  (oz/start-server!)
  
  )