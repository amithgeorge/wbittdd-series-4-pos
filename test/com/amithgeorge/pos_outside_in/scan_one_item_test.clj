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
  (let [specs (hashmap->specs items)]
    (f/reify-fake
     catalogue/Catalogue
     (price :fake specs))))

(defn- mock-display
  []
  (f/reify-fake
   display/Display
   (price :recorded-fake)))

(deftest item-found
  (testing "Given a barcode for an existing item, it should display its price"
    (f/with-fakes
      (let [irrelevant-price {:amount 10.0M}
            irrelevant-code "some product barcode"
            catalogue (stub-catalogue {irrelevant-code irrelevant-price})
            display (mock-display)]
        (sut/scan catalogue display irrelevant-code)
        (is (f/method-was-called-once display/price display [irrelevant-price]))))))
