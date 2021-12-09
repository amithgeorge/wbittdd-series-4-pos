(ns com.amithgeorge.pos-outside-in.impl.console-display
  (:require [com.amithgeorge.pos-outside-in.display :as display]))

(defn instance
  []
  (reify display/Display
    (not-found [_]
      (println "Product not found."))
    (invalid-code [_]
      (println "Scanning error. Invalid code."))
    (price [_ price]
      (println (format "USD %s" (:amount price))))))