(ns figforum.core
    (:require [rum.core :as rum]))

(enable-console-print!)

(println "This text is printed from src/figforum/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(def auth-db (atom [{:username "lopez"
                    :password "great"}
                   {:username "vas"
                    :password "haxor5"}]))



(defn err0r []
  (println "err0r"))

(defn message-sent-boomerang [ok result]
  (println ok result "** **")
  (.log js/console "hey the result!"))


; books, speeches, letters
; topic (autogenerated via tokens)
; question+responsese (votedriff)
; article->created+curated..ish [subtopics added on a ranked list]

(def nf-books (atom { 8882882 {:title "The way of awesomeness."
                               :isbn [10200000000002 210500000000010 200000000000000015]
                               :threads [2 3 4 5 6]
                               }}))
(def nf-speeches (atom {}))
(def nf-letters (atom {}))
(def nf-topics (atom {}))

(def nf-threads (atom { 12121 { :title "cruise on through to the other side"
                                :author "jimmorrison@nonforum.com"
                                :comments []
                                :number-of-ratings 2
                                :rating-total 98}

                               ; :ratings [{:oid 5175
                               ;            :rater "z@nf.com"
                               ;            :rating "+"
                               ;            :timestamp 564271}]}
;scrap this ratings idea on the client.. track in on the server.
                        }))


(def nf-ratings (atom { 33373 { :rid 33373
                                :author "jm@nf.com"
                                :oid "theThingyRated251215"
                                :rating "++"
                                :timestamp 121212121212}}))  ;;massive global rating object.  fill it however you choose from the server.  subscriptions.

(def nf-qd (atom { 12345777 {:contents "Hey this is a question about this site.  How does it all work, and what's it all about?"
                             :comments []
                             :author "x@nonforum.com"
                             :timestamp 80808
                             :number-of-ratings 32
                             :rating-total 9700

                             :ratings [{:oid 123456
                                        :rater "y@nonforum.com"
                                        :rating "++"
                                        :timestamp 6756421
                                        }] }


                   })); Questions and Discussions

(def nf-articles (atom {167641 {:subtopics [{:subtopic "deciduous" :ratings [{:rater "bob@nf.com" :rating "++" :timestamp 28282}]}
                                            {:subtopic "harmony" :ratings [{:rater "oion@nf.com" :rating "++" :timestamp 28282}]}]}}))

;in articles, each subtopic is an NF thread.


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
                          :selected-parent 77
                          :selected-child [33 53]
                          :username ""
                          :password ""
                          :token ""
                          :token-timestamp ""
                          :create-username ""
                          :create-password ""
                          :create-password2 ""
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
                    :comments [34]}
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
                    :comments [88 7777]}
                   {:id 69
                    :contents "the extraordinary world of bugs is glorious."
                    :author "fx@nonforum.com"
                    :comments [77]}
                   {:id 7777
                    :contents "Oh how I love the rain"
                    :author "rains@nonforum.com"
                    :comments []}]))

(swap! posts conj {:id 88
                   :contents "fortunate are the African penguins"
                   :author "vv@nonforum.com"
                   :comments []})

(def ratings (atom [{}]))

;create painless index map {:thing} from id
;(defn idx-by-id [id-key coll]
;  (into {}
;        (map (fn [{id id-key :as item}]
;               [id item]))
;        coll))

;(def posts-sleek (atom (idx-by-id :id @posts)))

(defn return-comment-ids [post-id]
  (let [cids (:comments (first (filter  #(= post-id (:id %)) @posts)))]
    cids))

(return-comment-ids 69)

(first (filter #(= 69 (:id %)) @posts))



(defn create-user [username password password2]
  (if (not (= password password2))
    (.log js/console "passwords do not match")
  ;else
    (if (not (empty? (filter #(= username (:username %)) @auth-db)))
      (.log js/console "username in use")
    ;else
      (do
        (swap! auth-db conj {:username username :password password})
        (.log js/console "n<>n user added to db" username)))))





(defn try-login [username password]
  (let [results (first (filter #(= username (:username %)) @auth-db))
        stored-pw (:password results)]
    (if (empty? results)
      (.log js/console "<user not found")
    ;else
      (if (= password stored-pw)
        (do
          (.log js/console "passwords match")
          (.log js/console "generating login token")
          (swap! input-state assoc-in [:inputs 0 :token] "hash-this--shiz")
          (swap! input-state assoc-in [:inputs 0 :username] username))
      ;else
        (.log js/console stored-pw)))))


(try-login "vas" "haxor5")

(filter #(= "vas" (:username %)) @auth-db)

(try-login "vas" "haxor5")

(create-user "hap" "5" "5")
(try-login "hap" "5")

(filter #(= "hap" (:username %)) @auth-db)

(def show-fresh
  {:did-mount (fn [state]
                (let [comp     (:rum/react-component state)
                      dom-node (js/ReactDOM.findDOMNode comp)]
                ;  (set! (.-backgroundColor (.-style dom-node)) "green")
                 ; (set! (.-transition (.-style dom-node)) "background-color 0.5s ease")
                  (set! (.-classList dom-node) "justMounted")
                  )
                state) })

 (rum/defc render-item < rum/reactive show-fresh [pid]

  (let [post-coll  (rum/react posts) ;atom
        input-coll (rum/react input-state)
        cids (return-comment-ids pid)]
    ;(prn cids)
    (if (empty? (return-comment-ids pid))
      (let [noc-post  (first (filter  #(= pid (:id %)) post-coll))]
        [:div.nocomments {:id pid :class "genpost"}
         [:div.padleft {:on-click (fn [e] (do
                                         (.log js/console "Freshly selected: " pid)
                                         (.stopPropagation e)
                                         (swap! input-state assoc-in [:inputs 0 :selected-parent] pid)
                                         (swap! input-state assoc-in [:inputs 0 :selected-child] (return-comment-ids pid))))}
          [:div.item-contents.genpost {:class (cond (= pid (get-in @input-state [:inputs 0 :selected-parent])) "selectedParent"
                                            (some #(= % pid) (get-in @input-state [:inputs 0 :selected-child])) "selectedChild")} (:contents noc-post)
            [:div.item-author   (:author noc-post)]]]])
       ;lest the post has comments and needs more renders in pocket.
       (let [com-post (first (filter  #(= pid (:id %)) post-coll))]
         [:div.hascomments {:id pid }
          [:div.padleft {:on-click (fn [e] (do
                                         (.log js/console "Freshly selected: " pid)
                                         (.stopPropagation e)
                                         (swap! input-state assoc-in [:inputs 0 :selected-parent] pid)
                                         (swap! input-state assoc-in [:inputs 0 :selected-child] (return-comment-ids pid))))}
           [:div.item-contents.genpost  {:class (cond (= pid (get-in @input-state [:inputs 0 :selected-parent])) "selectedParent"
                                              (some #(= % pid) (get-in @input-state [:inputs 0 :selected-child])) "selectedChild")} (:contents com-post)
             [:div.item-author (:author com-post)]]
           (map render-item cids)]]))))


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
   ;(fb-sdk 1417763311691300) ;nonforum app id
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

                                       (swap! tv-state update :tiles conj new-post-map))) ;thanks @Marc O'Morain
                       } "post new"]])

;; https://github.com/tonsky/grumpy/blob/master/src/grumpy/editor.cljc#L257
;; thank you, @tonsky
;; rum is awesome. 25 nov 2018


(def y (atom 999))

(rum/defc post-comment-input []
  [:form#postcommentinput
   [:textarea.fullwidth {:value (get-in @input-state [:inputs 0 :comment])
                         :place-holder "your comment"
                         :on-change (fn [e] (do
                                              (swap! input-state assoc-in [:inputs 0 :comment] (.-value (.-target e)))
                                              (.log js/console (get-in @input-state [:inputs 0 :comment]))))
                         }]
   [:input.fullwidth {:value (get-in @input-state [:inputs 0 :username])
                         :place-holder "username"
                         :on-change (fn [e] (do
                                              (swap! input-state assoc-in [:inputs 0 :username] (.-value (.-target e)))
                                              (.log js/console (get-in @input-state [:inputs 0 :username]))))
                         }]
   [:button.fullwidth {:type "button"
                       :class "replySelected"
                       :on-click (fn [e]
                                   (let [ parent-id (get-in @input-state [:inputs 0 :selected-parent])
                                          username (get-in @input-state [:inputs 0 :username])
                                          new-comment-map {:id (swap! y inc)
                                                          :contents (get-in @input-state [:inputs 0 :comment])
                                                          :author username
                                                          :comments []}]

                                      (let [first-hit (->> @posts
                                                          (keep-indexed #(when (= (:id %2) parent-id) %1))
                                                           first)]
                                        (.log js/console ">< " (get-in @posts [first-hit :comments]) (:id new-comment-map))
                                        (swap! posts conj new-comment-map) ;add new comment
                                        (swap! posts update-in [first-hit :comments] conj (:id new-comment-map))) ;add comment id to parent
                                     (prn posts)
                                     ))} "Reply to Plum-highlighted Comment"]])



(rum/defc create-account-fields []
  [:form#nfcreate
   [:input.fullwidth {:place-holder "username" :name "username"
                      :on-change (fn [e] (do
                                              (swap! input-state assoc-in [:inputs 0 :create-username] (.-value (.-target e)))
                                              (.log js/console (get-in @input-state [:inputs 0 :create-username]))))}]
   [:input.fullwidth {:place-holder "password" :type "password" :name "password"
                      :on-change (fn [e] (do
                                              (swap! input-state assoc-in [:inputs 0 :create-password] (.-value (.-target e)))
                                              (.log js/console (get-in @input-state [:inputs 0 :create-password]))))}]
   [:input.fullwidth {:place-holder "pw confirm" :type "password" :name "password2"
                      :on-change (fn [e] (do
                                              (swap! input-state assoc-in [:inputs 0 :create-password2] (.-value (.-target e)))
                                              (.log js/console (get-in @input-state [:inputs 0 :create-password2]))))}]
   [:button.fullwidth {:type "button"
                       :on-click (fn [e] (let [username  (get-in @input-state [:inputs 0 :create-username])
                                               password  (get-in @input-state [:inputs 0 :create-password])
                                               password2 (get-in @input-state [:inputs 0 :create-password2])]
                                           (do
                                             (.log js/console "create account button pressed")
                                             (create-user username password password2))))} "create account"]])



(rum/defc nf-login-input []
  [:form#nflogin
   [:input.fullwidth {:place-holder "username" :name "username"
                      :on-change (fn [e] (do
                                              (swap! input-state assoc-in [:inputs 0 :username] (.-value (.-target e)))
                                              (.log js/console (get-in @input-state [:inputs 0 :username]))))}]
   [:input.fullwidth {:place-holder "password" :type "password" :name "password"
                      :on-change (fn [e] (do
                                              (swap! input-state assoc-in [:inputs 0 :password] (.-value (.-target e)))
                                              (.log js/console (get-in @input-state [:inputs 0 :password]))))}]
   [:button.fullwidth {:type "button"
                       :on-click (fn [e] (let [username (get-in @input-state [:inputs 0 :username])
                                               password (get-in @input-state [:inputs 0 :password])]
                                           (do
                                             (.log js/console "login button pressed")
                                             (try-login username password))))} "login"]])

(rum/defc create-account-input []
  [:div#create-account-contain
   (create-account-fields)])

(rum/defc input-fields []
  [:div#inputs-contain
   (post-input)
   (post-comment-input)
   (nf-login-input)
   (create-account-input)])

(rum/defc start []
  [:div#maincontain
   (top-bar)
   (side-bar)
   (login-bar)
   (television)])

(rum/mount (render-item 69)
           (. js/document (getElementById "thread")))

(rum/mount (start)
           (. js/document (getElementById "start")))

(rum/mount (input-fields)
           (. js/document (getElementById "inputs")))
(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
