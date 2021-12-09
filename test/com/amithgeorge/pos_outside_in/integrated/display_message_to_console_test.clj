(ns com.amithgeorge.pos-outside-in.integrated.display-message-to-console-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos-outside-in.display :as display]
            [com.amithgeorge.pos-outside-in.impl.console-display :as console-display]
            [clojure.string :as str]))

(deftest display-not-found-error-message
  (testing "accept no arguments, prints not found message to stdout"
    (let [std-out-lines (str/split-lines
                         (with-out-str
                           (display/not-found (console-display/instance))))]
      (is (= ["Product not found."] std-out-lines)))))

(deftest display-invalid-code-error-message
  (testing "accept no arguments, prints invalid code message to stdout"
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