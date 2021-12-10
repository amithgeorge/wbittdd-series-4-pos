(ns com.amithgeorge.pos-outside-in.cart
  (:refer-clojure :exclude [empty?])
  (:require [com.amithgeorge.pos-outside-in.catalogue :as catalogue]))

(def CartItemSchema
  [:map
   [:code :string]
   [:price catalogue/AmountSchema]])

(def CartSchema
  [:map [:items [:sequential CartItemSchema]]])

(defprotocol Cart
  (add [this item] "Adds item to cart")
  (empty? [this] "Returns true if cart is empty")
  (total [this] "Return the sum of prices of all items in cart")
  (state [this] "Temp return the state of cart"))

(defn add-to
  [cart code price]
  (update cart :items conj {:code code :price price}))