(ns com.amithgeorge.pos-outside-in.integrated.display-message-to-console-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [com.amithgeorge.pos-outside-in.display :as display]
            [com.amithgeorge.pos-outside-in.impl.console-display :as console-display]))

(deftest display-not-found-error-message
  (testing "prints not found message to stdout"
    (let [std-out-lines (str/split-lines
                         (with-out-str
                           (display/not-found (console-display/instance))))]
      (is (= ["Product not found."] std-out-lines)))))

(deftest display-invalid-code-error-message
  (testing "prints invalid code message to stdout"
    (let [std-out-lines (str/split-lines
                         (with-out-str
                           (display/invalid-code (console-display/instance))))]
      (is (= ["Scanning error. Invalid code."] std-out-lines)))))

(deftest display-formatted-price
  (testing "accept an amount, prints formatted price to stdout"
    (let [std-out-lines (str/split-lines
                         (with-out-str
                           (display/price (console-display/instance) {:amount 10.39M})))]
      (is (= ["USD 10.39"] std-out-lines)))))

(deftest display-formatted-total
  (testing "accept an amount, prints formatted total to stdout"
    (let [std-out-lines (str/split-lines
                         (with-out-str
                           (display/total (console-display/instance) {:amount 39.17M})))]
      (is (= ["Total: USD 39.17"] std-out-lines)))))

(deftest display-cart-empty
  (testing "prints cart is empty message"
    (let [std-out-lines (str/split-lines
                         (with-out-str
                           (display/cart-empty (console-display/instance))))]
      (is (= ["Cart empty. Please scan an item."] std-out-lines)))))