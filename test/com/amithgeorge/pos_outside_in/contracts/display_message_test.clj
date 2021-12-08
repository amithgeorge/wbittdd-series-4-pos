(ns com.amithgeorge.pos-outside-in.contracts.display-message-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos-outside-in.display :as display]))

(defn- create-fake-display
  [method]
  (case method
    :price (reify display/Display
             (price [_ price]
               nil))
    :not-found (reify display/Display
                 (not-found [_]
                   nil))
    :invalid-code (reify display/Display
                    (invalid-code [_]
                      nil))))

(deftest display-price
  (testing "accept an amount, don't throw an exception"
    (let [display (create-fake-display :price)]
      (display/price display {:amount 13.9M})
      (is true))))

(deftest display-not-found-error-message
  (testing "accept no arguments, don't throw an exception"
    (let [display (create-fake-display :not-found)]
      (display/not-found display)
      (is true))))

(deftest display-invalid-code-error-message
  (testing "accept no arguments, don't throw an exception"
    (let [display (create-fake-display :invalid-code)]
      (display/invalid-code display)
      (is true))))
