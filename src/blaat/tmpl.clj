(ns blaat.tmpl
  (:use [blaat.i18n]
        [blaat.url]
        [hiccup.core]
        [hiccup.page]))


(defn create-account-form []
  [:form {:action (url "/account/create") :method "post" :role "form"}
   [:label {:for "email"} (_t "Email adress")]
   [:input {:type "email" :class "form-control" :id "email" :placeholder (_t "Enter email")}]
   [:label {:for "password"} (_t "Password")]
   [:input {:type "password" :class "form-control" :id "password" :placeholder (_t "Enter password")}]
   [:label {:for "password2"} (_t "Password (repeat)")]
   [:input {:type "password" :class "form-control" :id "password2" :placeholder (_t "Enter password (repeat)")}]
   [:button {:type "submit" :class "btn btn-cta"}] (_t "Create account")])


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
         [:a {:class "navbar-brand" :href "#"} "Project name"]]
       [:div {:class "collapse navbar-collapse"}
         [:ul {:class "nav navbar-nav"}
           [:li {:class "active"}
                [:a {:href "#"} "Home"]]
           [:li [:a {:href "#"} "About"]]
           [:li [:a {:href "#"} "Contact"]]]

           [:ul.nav.navbar-nav.navbar-right
             (if logged-in-user?
                [:li.dropdown
                   [:a.dropdown-toggle {:href "#" :data-toggle "dropdown"} user-name [:b.caret]]
                   [:ul.dropdown-menu
                     [:li [:a {:href (url "/account")} (_t "Account")]]
                     [:li.divider]
                     [:li [:a {:href (url "/logout")} (_t "Logout")]]

                    ]
                 ]
               ;;else
                 [:li [:a {:href (url "/login")} (_t "Login")]])
            ]

        ]]])

(defn main [&{:keys [title content logged-in-user? user-name script]
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
           content]]

       [:script {:src "https://code.jquery.com/jquery-1.10.2.min.js"}]
       [:script {:src (static-url "/js/bootstrap.min.js")}]

       (when (seq script)
         [:script script])]

      )))


(comment

  (main :title "Piet")

  )
