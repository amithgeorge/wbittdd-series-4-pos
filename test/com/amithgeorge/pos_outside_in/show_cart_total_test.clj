(ns com.amithgeorge.pos-outside-in.show-cart-total-test
  (:refer-clojure :exclude [empty?])
  (:require [clj-fakes.core :as f]
            [clojure.test :refer [deftest is testing]]
            [com.amithgeorge.pos-outside-in.cart :as cart]
            [com.amithgeorge.pos-outside-in.display :as display]
            [com.amithgeorge.pos-outside-in.pos :as sut]))

(deftest empty-cart
  (testing "Total of an empty cart, it should display empty cart message"
    (f/with-fakes
      (let [display (f/reify-fake
                     display/Display
                     (cart-empty :recorded-fake))
            initial-cart {:items []}
            inmemory-cart (f/reify-fake
                           cart/Cart
                           (empty? :optional-fake [[] true]))]
        (sut/total display inmemory-cart initial-cart)
        (is (f/method-was-called-once display/cart-empty display []))))))

(deftest cart-with-items
  (testing "Cart has items, it should display total"
    (f/with-fakes
      (let [total {:amount 89.1M}
            display (f/reify-fake
                     display/Display
                     (total :recorded-fake))
            cart-with-items {:items [{:code "irrelevant item 1 code" :price {:amount 13.9M}}
                                     {:code "irrelevant item 2 code" :price {:amount 75.2M}}]}
            inmemory-cart (f/reify-fake
                           cart/Cart
                           (empty? :optional-fake [[] false])
                           (total :optional-fake [[] total]))]
        (sut/total display inmemory-cart cart-with-items)
        (is (f/method-was-called-once display/total display [total]))))))