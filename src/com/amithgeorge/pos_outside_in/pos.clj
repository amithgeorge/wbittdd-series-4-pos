(ns com.amithgeorge.pos-outside-in.pos
  (:require [com.amithgeorge.pos-outside-in.catalogue :as catalogue]
            [com.amithgeorge.pos-outside-in.display :as display]
            [clojure.string :as str]))

(defn scan
  [catalogue display code]
  (if (str/blank? code)
    (display/invalid-code display)
    (if-let [product-price (catalogue/price catalogue code)]
      (display/price display product-price)
      (display/not-found display))))