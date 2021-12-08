(ns com.amithgeorge.pos-outside-in.contracts.find-price-in-catalogue-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos-outside-in.catalogue :as catalogue]))

(defn- create-catalogue
  [& {:keys [include exclude]}]
  (reify catalogue/Catalogue
    (price
      [_ code]
      (cond
        (= code (:code exclude)) nil
        (= code (:code include)) (:price include)))))

(deftest code-present-in-catalogue
  (testing "Given a code present in the catalgoue, return its price"
    (let [item-to-include {:code "::code which exists::"
                           :price {:amount 13.9M}}
          catalogue (create-catalogue :include item-to-include)
          actual-price (catalogue/price catalogue (:code item-to-include))]
      (is (= (:price item-to-include) actual-price)))))

(deftest code-not-present-in-catalogue
  (testing "Given a code not present in the catalgoue, return its price"
    (let [item-to-exclude {:code "::code which does not exists::"}
          catalogue (create-catalogue :exclude item-to-exclude)
          actual-price (catalogue/price catalogue (:code item-to-exclude))]
      (is (nil? actual-price)))))