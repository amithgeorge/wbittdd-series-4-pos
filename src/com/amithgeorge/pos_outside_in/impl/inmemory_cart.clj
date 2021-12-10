(ns com.amithgeorge.pos-outside-in.impl.inmemory-cart
  (:require [malli.core :as m]
            [com.amithgeorge.pos-outside-in.cart :as cart]))

(defn instance
  [initial-items]
  (let [state (atom {:items (or initial-items [])})]
    (assert (m/validate cart/CartSchema @state))
    (reify cart/Cart
      (empty? [_]
        (clojure.core/empty? (:items @state)))
      (add [_ item]
        (assert (m/validate cart/CartItemSchema item))
        (swap! state update :items (fn [items] (conj items item))))
      (total [_]
        (let [{:keys [items]} @state]
          (if (clojure.core/empty? items)
            (throw (IllegalStateException. "Cannot total an empty cart. Check for empty using cart/empty?"))
            {:amount (->> items
                          (map #(get-in %1 [:price :amount]))
                          (reduce + 0.0M))}))))))
