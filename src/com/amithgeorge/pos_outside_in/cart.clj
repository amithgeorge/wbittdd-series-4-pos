(ns com.amithgeorge.pos-outside-in.cart
  (:require [com.amithgeorge.pos-outside-in.catalogue :as catalogue]))

(def CartItemSchema
  [:map [:code :string]
   [:price catalogue/AmountSchema]])

(defprotocol Cart
  (add [this item] "Adds item to cart"))