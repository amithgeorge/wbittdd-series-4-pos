(ns com.amithgeorge.pos-outside-in.cart-test
  (:refer-clojure :exclude [empty?])
  (:require [clojure.test :refer [deftest is testing]]
            [com.amithgeorge.pos-outside-in.cart :as cart]))

(deftest total-empty-cart
  (testing "Given an empty cart, total should throw an exception"
    (let [empty-cart (cart/new)]
      (is (thrown? IllegalStateException (cart/total-cart empty-cart))))))

(deftest total-cart-items
  (testing "Given a cart with 1 item, total should return its price"
    (let [expected-amount {:amount 10.3M}
          cart {:items [{:code "irrelevant code" :price {:amount 10.3M}}]}]
      (is (= expected-amount (cart/total-cart cart)))))

  (testing "Given a cart with multiple items, total should return the sum of each item price"
    (let [expected-amount {:amount 102.69M}
          cart {:items [{:code "irrelevant code 1" :price {:amount 10.3M}}
                        {:code "irrelevant code 2" :price {:amount 24.5M}}
                        {:code "irrelevant code 3" :price {:amount 67.89M}}]}]
      (is (= expected-amount (cart/total-cart cart))))))