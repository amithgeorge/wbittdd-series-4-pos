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
      (println (format "USD %s" (:amount price))))
    (total [_ amount]
      (println (format "Total: USD %s" (:amount amount))))
    (cart-empty [_]
      (println (format "Cart empty. Please scan an item.")))))