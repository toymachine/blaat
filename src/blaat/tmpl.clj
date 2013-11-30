(ns blaat.tmpl
  (:use [hiccup.core]
        [hiccup.page]))

(defn static-url [s]
  (str "/static/" s))

(defn navbar []
  (html
     [:div {:class "navbar navbar-inverse navbar-fixed-top" :role "navigation"}
       [:div {:class "container"}
         [:div {:class "navbar-header"}
           [:button {:type "button" :class "navbar-toggle" :data-toggle "collapse" :data-target ".navbar-collapse"}
             [:span {:class "sr-only"} "Toggle navigation"]
             [:span {:class "icon-bar"}]
             [:span {:class "icon-bar"}]
             [:span {:class "icon-bar"}]]
           [:a {:class "navbar-brand" :href "#"} "Project name"]]
         [:div {:class "collapse navbar-collapse"}
           [:ul {:class "nav navbar-nav"}
             [:li {:class "active"}
                  [:a {:href "#"} "Home"]]
             [:li [:a {:href "#"} "About"]]
             [:li [:a {:href "#"} "Contact"]]]]]]))

(defn main [&{:keys [title content]
              :or {title "No title" content ""}}]
  (html
    (html5
     [:head
        [:meta {:charset "utf-8"}]
        [:title title]
        [:link {:href (static-url "css/bootstrap.css") :rel "stylesheet"}]
        [:link {:href (static-url "css/blaat.css") :rel "stylesheet"}]
        [:link {:href (static-url "images/favicon.png") :rel "shortcut icon"}]]

     [:body
       (navbar)

       [:div {:class "container"}
         [:div {:class "starter-template"}
           content]]

       [:script {:src "https://code.jquery.com/jquery-1.10.2.min.js"}]
       [:script {:src (static-url "js/bootstrap.min.js")}]]

      )))


(comment

  (main :title "Piet")

  )
