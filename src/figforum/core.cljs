(ns figforum.core
    (:require [rum.core :as rum]))

(enable-console-print!)

(println "This text is printed from src/figforum/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))


(defn err0r []
  (println "err0r"))

(defn message-sent-boomerang [ok result]
  (println ok result "** **")
  (.log js/console "hey the result!"))

(defonce app-state (atom {:text "Hello world!"}))
(def     tv-state (atom
                    {:tiles
                        [ {:title "Fusion Power Imminent"
                           :contents "Horne Technologies has developed a working Plasma Containment Prototype for furthering Fusion"
                           :priority 1
                           :posted-by "v@nonforum.com"
                           :timestamp 808080808
                           :parent nil}
                          {:title "Let's Put Sun Panels on the Roof"
                           :contents "Put a powerplant on your home and be free of your electric bill"
                           :priority 2
                           :posted-by "v@nonforum.com"
                           :timestamp 808080808
                           :parent nil}
                          {:title "Tonsky/rum is excellent for cljs"
                           :contents "the best way to be the best"
                           :priority 3
                           :posted-by "v@nonforum.com"
                           :timestamp 808080808
                           :parent nil}
                          {:title "Postpostpost"
                           :contents "this is the post!"
                           :link "http://hysterical.com"
                           :priority 4
                           :posted-by "v@nonforum.com"
                           :timestamp 808080808
                           :parent nil}]}))

(def input-state (atom {:inputs
                       [ {:title ""
                          :contents ""
                          :comment "ur coment"
                          }]}))

(defn js-reload []
  (.log js/console "javascript reloaded ^_^;"))


(def posts (atom [ {:id 77
                    :contents "Seventy seven is the nicest number below one hundred"
                    :author "nonforum@nonforum.com"
                    :comments [33 53]}
                   {:id 33
                    :contents "Thirty three is awesome."
                    :author "monforum@nonforum.com"
                    :comments [34 35]}
                   {:id 34
                    :contents "fusion is coming soon to a powergrid near you."
                    :author "non@nonforum.com"
                    :comments [37]}
                   {:id 37
                    :contents "hello there to the galaxy"
                    :author "x@nonforum.com"
                    :comments []}
                   {:id 53
                    :contents "relax , don't do it."
                    :author "fool@nonforum.com"
                    :comments []}]))



(:comments (first (filter  #(= 33 (:id %)) @posts)))

(defn return-comment-ids [post-id]
  (let [cids (:comments (first (filter  #(= post-id (:id %)) @posts)))]
    cids))

(defn render-post [pid]
  (let [cids (return-comment-ids pid)]
    (prn cids)
    (if (empty? (return-comment-ids pid))
      (let [noc-post (first (filter #(= pid (:id %)) @posts))]
        (println (:contents noc-post))
        (println (:author noc-post)))
       (let [com-post (first (filter #(= pid (:id %)) @posts))]
         (println (:contents com-post))
         (println (:author com-post))
         (println "comments >")
         (map render-post cids)))))

(render-post 77)
;renders good nested structure for the comments

(defn render-post [pid]
  (let [post (first (filter #(= pid (:id %)) @posts))
        contents (:contents post)
        author (:author post)
        comments (:comments post)]

    (str comments " " author " " contents)))

(render-post 33)

(return-comment-ids 33)
(mapcat return-comment-ids (return-comment-ids 33))

(return-comment-ids 77)

(return-comment-ids 34)


 (filter  #(= 33 (:id %)) @posts)


(rum/defc comment-parent [id]
  (filter (fn [sid]
            (= sid id)) @posts))

(rum/defc fb-sdk [app-id]
  [:script {:type "text/javascript"}
   (str "window.fbAsyncInit = function() {
    FB.init({
      appId      : '" app-id "',
      cookie     : true,
      xfbml      : true,
      version    : '3.2'
    });

    FB.AppEvents.logPageView();

  };

  (function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = 'https://connect.facebook.net/en_US/sdk.js';
     fjs.parentNode.insertBefore(js, fjs);
   }(document, 'script', 'facebook-jssdk'));")])

(rum/defc link [address]
  [:a {:href address} address])

(rum/defc hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Edit this and watch it change!"]
   [:h4 "Nonforum lives again!"]])

(rum/defc top-bar []
  [:div#topbar
   [:ol.topbar
    [:li [:a {:href "/"} "nonforum"]]
    [:li (link "top")]
    [:li (link "latest")]
    [:li (link "submit")]
    [:li (link "feed")]]])

(rum/defc side-bar []
  [:div#sidebar
   [:ol.sidebar
    [:li (link "profile")]
    [:li (link  "settings & pls omg no moar hax")]
    [:li (link "feedback & hax")]
    [:li (link "logout")]]])

(rum/defc login-bar []
  [:div#loginbar
   [:ol.loginbar
    [:li.fbfb [:a {:href "/facebook"} "Continue with Facebook as ___"]]
    [:li.gogo [:a {:href "/gogole"} "Google Login"]]
    [:li.twtw [:a {:href "/twitter"} "Twitter Login"]]
    [:li.nfnf [:a {:href "/nflogin"} "Nonforum Login"]]]
   (fb-sdk 1417763311691300) ;nonforum app id
   ])

(rum/defc tv-cell [td]
  [:li [:div.tile {:id (str "tile" (:priority td))}
        [:div.heading (:title td)]
        [:div.contents (:contents td)]
        [:div.priority (:priority td)]]])

(rum/defc television  < rum/reactive []
  [:div#tv
   [:ol.tv
    (map tv-cell (:tiles (rum/react tv-state)))]])

(rum/defc post-input []
  [:form#postinput
   [:input.fullwidth {:place-holder "title"
                      :on-change (fn [e] (do
                                    (swap! input-state assoc-in [:inputs 0 :title] (.-value (.-target e)))
                                    (.log js/console (get-in @input-state [:inputs 0 :title]))))}]
   [:input.fullwidth {:place-holder "contents"
                      :on-change (fn [e] (do
                                   (swap! input-state assoc-in [:inputs 0 :contents] (.-value (.-target e)))
                                   (.log js/console (get-in @input-state [:inputs 0 :contents]))))}]
   [:button.fullwidth {:type "button"
                       :on-click (fn [e]
                                     ;(.preventDefault e)
                                     ;(.stopPropagation e)
                                     (.log js/console "sending..")
                                     (.log js/console (.getElementById js/document "aft"))
                                   ;submit to server here!

                                    (let [new-post-map {:title (get-in @input-state [:inputs 0 :title])
                                                                                 :contents (get-in @input-state [:inputs 0 :contents])
                                                                                 :priority 10
                                                                                 :posted-by "x@nonforum.com"
                                                                                 :timestamp 80008
                                                                                 :parent nil}]
                                    ;   (POST "/send-message"
                                     ;   {:body {:title (:title new-post-map)
                                      ;            :posted-by "x@nonforum.com"
                                       ;           :timestamp 80008
                                        ;          :parent nil
                                   ;               :priority 11
                                    ;              :contents (:contents new-post-map)
                                     ;             :handler message-sent-boomerang
                                      ;            :error-handler err0r }
                                       ;  :headers {"x-csrf-token" (.-value (.getElementById js/document "aft"))}
                                       ;;  })
                                       ;(.log js/console (.-value (.getElementById js/document "aft"))) ;new-post-map)

                                       (swap! tv-state update :tiles conj new-post-map))) ;thanks @Marc O'Morain
                       } "post new"]])

;; https://github.com/tonsky/grumpy/blob/master/src/grumpy/editor.cljc#L257
;; thank you, @tonsky
;; rum is awesome. 25 nov 2018

(rum/defc post-comment-input []
  [:form#postcommentinput
   [:textarea.fullwidth {:value (get-in @input-state [:inputs 0 :comment])
                         :place-holder "your comment"
                         :on-change (fn [e] (do
                                              (swap! input-state assoc-in [:inputs 0 :comment] (.-value (.-target e)))
                                              (.log js/console (get-in @input-state [:inputs 0 :comment]))))
                         }]
   [:button.fullwidth {:type "submit"} "post comment"]])














(rum/defc nf-login-input []
  [:form#nflogin
   [:input.fullwidth {:place-holder "username"}]
   [:input.fullwidth {:place-holder "password" :type "password"}]
   [:button.fullwidth {:type "submit"} "login"]])





(def ps
  ;posts
  (atom { :posts [{:parent-id 777
                   :this-id 555
                   :child-id [444 333 222 8888]}
                  {:parent-id 334
                   :this-id 5353
                   :child-id 225}]}))

(def posts [{:title "What up Wisconsin!"
             :content ""
             :comments [{:cid 758373
                        :content "How you doing?"
                        :comments [{:cid 87583
                                   :content "Excellent!"
                                   :comments [{:cid 8787
                                              :content "Excelsior!"}]}]}]}])

(defn hax [m]
  (for [[title contents comments] m]
    (do
      (.log js/console (str title " " contents " " comments " "))
    (list title contents (hax comments)))))

(def m {"P" {"C" {} }
       "CD" {"DD" {}}
       "CE" {"EF" {"EG" {"EX" {}}}} })
                 ;       {:cid 7537
                 ;       :content "Extremsiour"
                 ;       :comments {}}})
         ;   {:title "Superdanger"
         ;    :content "Ohyeahunoit"
         ;    :comments {:cid 753753
         ;               :content ""
         ;               :comments {:cid 88787
         ;                          :content "Hurrah!"
         ;                          :comments {}}}}])


(defn nest [m]
  (for [[k v] m]
    (do
      (.log js/console (str k v))
      (list  [:.padleft k (nest v)]))))

(rum/defc nestit []
  [:div (nest m)])







(rum/defc input-fields []
  [:div#inputs-contain
   (post-input)
   (post-comment-input)
   (nf-login-input)])

(rum/defc start []
  [:div#maincontain
   (nestit)
   (top-bar)
   (side-bar)
   (login-bar)
   (television)
   (hello-world)])

(rum/mount (start)
           (. js/document (getElementById "start")))

(rum/mount (input-fields)
           (. js/document (getElementById "inputs")))
(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
