(ns blaat.tmpl
  (:use [blaat.i18n]
        [blaat.url]
        [hiccup.core]
        [hiccup.page]))




(defn navbar [&{:keys [logged-in-user? user-name]
              :or {logged-in-user false}}]
   [:div {:class "navbar navbar-inverse navbar-fixed-top" :role "navigation"}
     [:div {:class "container"}
       [:div {:class "navbar-header"}
         [:button {:type "button" :class "navbar-toggle" :data-toggle "collapse" :data-target ".navbar-collapse"}
           [:span {:class "sr-only"} "Toggle navigation"]
           [:span {:class "icon-bar"}]
           [:span {:class "icon-bar"}]
           [:span {:class "icon-bar"}]]
         [:a {:class "navbar-brand" :href "/"} "Blaat"]]
       [:div {:class "collapse navbar-collapse"}
         [:ul {:class "nav navbar-nav"}

           [:li {:class "active"}
                [:a {:href "/"} "Home"]]]

         [:ul.nav.navbar-nav.navbar-right
           (when-not logged-in-user?
             [:li [:a {:href "/register"} (_t "Join")]])

           (if logged-in-user?
              [:li.dropdown
                 [:a.dropdown-toggle {:href "#" :data-toggle "dropdown"} user-name [:b.caret]]
                 [:ul.dropdown-menu
                   [:li [:a {:href (url "/account")} (_t "Account")]]
                   [:li.divider]
                   [:li [:a {:href (url "/logout")} (_t "Sign out")]]

                  ]
               ]
             ;;else
               [:li [:a {:href (url "/login")} (_t "Sign in")]])
          ]

        ]]])

(defn main [&{:keys [title content logged-in-user? user-name script success error]
              :or {title "No title" content "" logged-in-user false}}]
  (html
    (html5
     [:head
        [:meta {:charset "utf-8"}]
        [:title title]
        [:link {:href (static-url "/css/bootstrap.min.css") :rel "stylesheet"}]
        [:link {:href (static-url "/css/bootstrap-theme.min.css") :rel "stylesheet"}]
        [:link {:href (static-url "/css/blaat.css") :rel "stylesheet"}]
        [:link {:href (static-url "/images/favicon.png") :rel "shortcut icon"}]]

     [:body

       (navbar :logged-in-user? logged-in-user? :user-name user-name)

       [:div.container
         [:div.starter-template

          (for [msg success]
            [:div.alert.alert-success msg])

          (for [msg error]
            [:div.alert.alert-danger msg])

           content]]

       [:script {:src (static-url "/js/jquery-1.10.2.min.js")}]
       [:script {:src (static-url "/js/bootstrap.min.js")}]

       (when (seq script)
         [:script script])]

      )))


(comment

  (main :title "Piet")

  )
