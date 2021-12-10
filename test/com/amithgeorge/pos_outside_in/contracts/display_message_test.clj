(ns com.amithgeorge.pos-outside-in.contracts.display-message-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos-outside-in.display :as display]
            [com.amithgeorge.pos-outside-in.impl.console-display :as console-display]))

(deftest display-price
  (testing "accept an amount, don't throw an exception"
    (let [display (console-display/instance)]
      (display/price display {:amount 13.9M})
      (is true))))

(deftest display-not-found-error-message
  (testing "accept no arguments, don't throw an exception"
    (let [display (console-display/instance)]
      (display/not-found display)
      (is true))))

(deftest display-invalid-code-error-message
  (testing "accept no arguments, don't throw an exception"
    (let [display (console-display/instance)]
      (display/invalid-code display)
      (is true))))

(deftest display-cart-empty-error-message
  (testing "accept no arguments, don't throw an exception"
    (let [display (console-display/instance)]
      (display/cart-empty display)
      (is true))))

(deftest display-total-message
  (testing "accept an amount, don't throw an exception"
    (let [display (console-display/instance)]
      (display/total display {:amount 23.97M})
      (is true))))
