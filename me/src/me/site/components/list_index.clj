(ns me.site.components.list-index)

(defn list-index [index]
  [:div.page-outer-container
   [:div#series-title (:title index)]
   [:div#series-source (:source index)]
   [:div.landing-page-article {:style "background-color: #F0F0F0;padding-top:15px;margin-top:50px;"}
    (vec (concat [:ul {:style "list-style:none;"}]
                 (mapv #(vector :li {:style "border-left: 2px gray solid;margin-bottom:25px;padding-left: 15px;"}
                                [:a {:style "color:black;"
                                     :href (:target %)}
                                 (:text %)])
                       (:content index))))]])