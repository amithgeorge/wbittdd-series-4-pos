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

(defn new-cart
  {:malli/schema [:=> :cat CartSchema]}
  []
  {:items []})

(defn empty?
  [cart]
  (clojure.core/empty? (:items cart)))

(defn add
  [cart code price]
  (update cart :items conj {:code code :price price}))

(defn total
  {:malli/schema [:=> [:cat CartSchema] catalogue/AmountSchema]}
  [cart]
  (if (empty? cart)
    (throw (IllegalStateException. "Cannot total an empty cart. Check for empty using cart/empty?"))
    (reduce add-amounts (map :price (:items cart)))))

