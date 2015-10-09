(ns alpaca-traders.new-posting
  (:require [reagent.core :as r :refer [atom]]
   [alpaca-traders.money-group :as money-group :refer [to-coppers to-money-group rebalance placeholders]]))

(defonce default-money-group {
                           :platinum 0
                           :gold 0
                           :silver 0
                           :copper 0
                           }
  )

(def input-state (r/atom {:price default-money-group
                                :price-per-unit default-money-group
                                :quantity 1
                                }))


(defn currency-input [input-type param]
  [:div [:label (param placeholders)]
   [:input {
             :type "number"
             :min "0"
             :placeholder (param placeholders)
             :on-change #(swap! input-state assoc-in [input-type param] (int (.-target.value %)))
             :on-blur #(swap! input-state assoc input-type (rebalance (input-type @input-state)))
             :value (get-in @input-state [input-type param])
             }]
   ])

(defn input-group [input-group-type]
  [:div [currency-input input-group-type :platinum]
   [currency-input input-group-type :gold]
   [currency-input input-group-type :silver]
   [currency-input input-group-type :copper]
   ])

(defn resolve-ppu []
  (let [total-price (:price @input-state),
        quantity (:quantity @input-state)]
    (swap! input-state assoc :price-per-unit (to-money-group (/ (to-coppers total-price) quantity)))
    )
  )

(defn resolve-total-price []
  (let [ppu (:price-per-unit @input-state)
        quantity (:quantity @input-state)]
    (swap! input-state assoc :price (to-money-group (* (to-coppers ppu) quantity)))
    )
  )

(defn create []
  [:div [:h2 "Price"]
   [input-group :price]
   [:label "I want to sell"]
   [:input {
             :type "number"
             :min "1"
             :on-change #(swap! input-state assoc :quantity (int (.-target.value %)))
             :value (:quantity @input-state)}]


   [:h2 "Price Per Unit"]
   [input-group :price-per-unit]
   ])