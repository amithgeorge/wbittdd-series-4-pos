(ns com.amithgeorge.pos-outside-in.scan-one-item-test
  (:require [clojure.test :refer [deftest testing is]]
            [clj-fakes.core :as f]
            [com.amithgeorge.pos-outside-in.pos :as sut]
            [com.amithgeorge.pos-outside-in.catalogue :as catalogue]
            [com.amithgeorge.pos-outside-in.display :as display]))

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
  (testing "Given a barcode for an existing item, it should display its price"
    (f/with-fakes
      (let [irrelevant-price {:amount 10.0M}
            irrelevant-code "some product barcode"
            catalogue (stub-catalogue {irrelevant-code irrelevant-price})
            display (f/reify-fake
                     display/Display
                     (price :recorded-fake))]
        (sut/scan catalogue display irrelevant-code)
        (is (f/method-was-called-once display/price display [irrelevant-price]))))))

(deftest item-not-found
  (testing "Given a barcode for an item not in catalogue, it should display not found"
    (f/with-fakes
      (let [code-product-not-in-catalogue "this shouldn't be found"
            catalogue (stub-catalogue {})
            display (f/reify-fake
                     display/Display
                     (not-found :recorded-fake))]
        (sut/scan catalogue display code-product-not-in-catalogue)
        (is (f/method-was-called-once display/not-found display []))))))

(deftest empty-code
  (testing "Given an empty barcode, it should display scanning error"
    (f/with-fakes
      (let [display (f/reify-fake
                     display/Display
                     (invalid-code :recorded-fake))]
        (sut/scan nil display "")
        (is (f/method-was-called-once display/invalid-code display []))))))

