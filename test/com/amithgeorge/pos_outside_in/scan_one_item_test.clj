(ns com.amithgeorge.pos-outside-in.scan-one-item-test
  (:require [clj-fakes.core :as f]
            [clojure.test :refer [deftest is testing]]
            [com.amithgeorge.pos-outside-in.cart :as cart]
            [com.amithgeorge.pos-outside-in.catalogue :as catalogue]
            [com.amithgeorge.pos-outside-in.display :as display]
            [com.amithgeorge.pos-outside-in.pos :as sut]
            [malli.core :as m]
            [com.amithgeorge.pos-outside-in.persistence :as persistence]))

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
            initial-cart {:items []}
            expected-cart {:items [{:code irrelevant-code :price irrelevant-price}]}
            inmemory-cart (f/reify-fake
                           cart/Cart
                           (add :recorded-fake [[(f/arg #(m/validate cart/CartItemSchema %1))] nil])
                           (state :optional-fake [f/any expected-cart]))
            storage (f/reify-fake
                     persistence/Persistence
                     (save-cart! :recorded-fake [[(f/arg #(m/validate cart/CartSchema %1))] nil]))]
        (sut/scan catalogue display storage inmemory-cart initial-cart irrelevant-code)

        (testing "It should display its price"
          (is (f/method-was-called-once display/price display [irrelevant-price])))

        (testing "It should add item to cart"
          (is (f/method-was-not-called cart/add inmemory-cart))
          (is (f/method-was-called-once persistence/save-cart! storage [expected-cart])))))))

(deftest item-not-found
  (testing "Given a barcode for an item not in catalogue"
    (f/with-fakes
      (let [code-product-not-in-catalogue "this shouldn't be found"
            catalogue (stub-catalogue {})
            display (f/reify-fake
                     display/Display
                     (not-found :recorded-fake))
            storage (f/reify-fake
                     persistence/Persistence
                     (save-cart! :recorded-fake))]
        (sut/scan catalogue display storage nil code-product-not-in-catalogue)
        (testing "It should display not found"
          (is (f/method-was-called-once display/not-found display [])))
        (testing "It should not add item to cart"
          (is (f/method-was-not-called persistence/save-cart! storage)))))))

(deftest empty-code
  (testing "Given an empty barcode"
    (f/with-fakes
      (let [display (f/reify-fake
                     display/Display
                     (invalid-code :recorded-fake))
            storage (f/reify-fake
                     persistence/Persistence
                     (save-cart! :recorded-fake))]
        (sut/scan nil display storage nil "")
        (testing "It should display scanning error"
          (is (f/method-was-called-once display/invalid-code display [])))
        (testing "It should not add item to cart"
          (is (f/method-was-not-called persistence/save-cart! storage)))))))

