(ns com.amithgeorge.pos-outside-in.contracts.compute-cart-total-test
  (:require [clojure.test :refer [deftest testing is]]
            [malli.core :as m]
            [com.amithgeorge.pos-outside-in.cart :as cart]
            [com.amithgeorge.pos-outside-in.catalogue :as catalogue]))

(defn inmemory-cart-instance
  [initial-items]
  (let [state (atom {:items (or initial-items [])})]
    (assert (m/validate cart/CartSchema @state))
    (reify cart/Cart
      (total [_]
        (let [{:keys [items]} @state]
          (if (clojure.core/empty? items)
            (throw (IllegalStateException. "Cannot total an empty cart. Check for empty using cart/empty?"))
            {:amount (->> items
                          (map #(get-in %1 [:price :amount]))
                          (reduce + 0.0M))}))))))

(deftest empty-cart
  (testing "for empty cart, total throws exception"
    (let [cart (inmemory-cart-instance [])]
      (is (thrown? IllegalStateException (cart/total cart))))))

(deftest items-with-price-zero
  (testing "items in cart, all with zero price, return 0"
    (let [cart (inmemory-cart-instance [{:code "::irrelevant code 1::" :price {:amount 0.0M}}])
          result (cart/total cart)]
      (is (m/validate catalogue/AmountSchema result))
      (is (= {:amount 0M} result)))))

(deftest items-with-non-zero-price
  (testing "items in cart having non zero price, return sum of all item prices"
    (let [cart (inmemory-cart-instance [{:code "::irrelevant code 1::" :price {:amount 12.3M}}
                                        {:code "::irrelevant code 2::" :price {:amount 97.5M}}])
          result (cart/total cart)]
      (is (m/validate catalogue/AmountSchema result))
      (is (= {:amount 109.8M} result)))))