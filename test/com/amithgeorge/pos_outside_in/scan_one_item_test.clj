(ns com.amithgeorge.pos-outside-in.scan-one-item-test
  (:require [clojure.test :refer [deftest testing is]]
            [clj-fakes.core :as f]
            [malli.core :as m]
            [com.amithgeorge.pos-outside-in.pos :as sut]
            [com.amithgeorge.pos-outside-in.catalogue :as catalogue]
            [com.amithgeorge.pos-outside-in.display :as display]
            [com.amithgeorge.pos-outside-in.cart :as cart]))

(defn- hashmap->specs
  [m]
  (->> m
       (reduce (fn [acc [k v]] (concat acc [[k] v]))
               [])
       vec))

(defn- stub-catalogue
  [items]
  ;; spec to accept any provide key and return its value. return nil for any other input
  (let [specs (conj (hashmap->specs items) [f/any] nil)]
    (f/reify-fake
     catalogue/Catalogue
     (price :fake specs))))

(deftest item-found
  (testing "Given a barcode for an existing item"
    (f/with-fakes
      (let [irrelevant-price {:amount 10.0M}
            irrelevant-code "some product barcode"
            catalogue (stub-catalogue {irrelevant-code irrelevant-price})
            display (f/reify-fake
                     display/Display
                     (price :recorded-fake))
            cart (f/reify-fake
                  cart/Cart
                  (add :recorded-fake [[(f/arg #(m/validate cart/CartItemSchema %1))] nil]))
            add-to-cart (f/recorded-fake [[(f/arg #(m/validate cart/CartItemSchema %1))] nil])]
        (sut/scan catalogue display add-to-cart cart irrelevant-code)

        (testing "It should display its price"
          (is (f/method-was-called-once display/price display [irrelevant-price])))

        (testing "It should add item to cart"
          (is (f/was-called-once add-to-cart [{:code irrelevant-code :price irrelevant-price}]))
          (is (f/method-was-called-once cart/add cart [{:code irrelevant-code :price irrelevant-price}])))))))

(deftest item-not-found
  (testing "Given a barcode for an item not in catalogue"
    (f/with-fakes
      (let [code-product-not-in-catalogue "this shouldn't be found"
            catalogue (stub-catalogue {})
            display (f/reify-fake
                     display/Display
                     (not-found :recorded-fake))
            add-to-cart (f/recorded-fake)]
        (sut/scan catalogue display add-to-cart code-product-not-in-catalogue)
        (testing "It should display not found"
          (is (f/method-was-called-once display/not-found display [])))
        (testing "It should not add item to cart"
          (is (f/was-not-called add-to-cart)))))))

(deftest empty-code
  (testing "Given an empty barcode"
    (f/with-fakes
      (let [display (f/reify-fake
                     display/Display
                     (invalid-code :recorded-fake))
            add-to-cart (f/recorded-fake)]
        (sut/scan nil display add-to-cart "")
        (testing "It should display scanning error"
          (is (f/method-was-called-once display/invalid-code display [])))
        (testing "It should not add item to cart"
          (is (f/was-not-called add-to-cart)))))))

