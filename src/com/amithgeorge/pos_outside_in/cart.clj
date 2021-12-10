(ns com.amithgeorge.pos-outside-in.cart
  (:refer-clojure :exclude [empty?])
  (:require [com.amithgeorge.pos-outside-in.catalogue :as catalogue]))

(def CartItemSchema
  [:map
   [:code :string]
   [:price catalogue/AmountSchema]])

(def CartSchema
  [:map [:items [:sequential CartItemSchema]]])

(defn- add-amounts
  [amount-1 amount-2]
  (merge-with + amount-1 amount-2))

(defn add-to
  [cart code price]
  (update cart :items conj {:code code :price price}))

(defn empty-cart?
  [cart]
  (clojure.core/empty? (:items cart)))

(defn total-cart
  {:malli/schema [:=> [:cat CartSchema] catalogue/AmountSchema]}
  [cart]
  (if (empty-cart? cart)
    (throw (IllegalStateException. "Cannot total an empty cart. Check for empty using cart/empty?"))
    (reduce add-amounts (map :price (:items cart)))))

(defn new
  {:malli/schema [:=> :cat CartSchema]}
  []
  {:items []})