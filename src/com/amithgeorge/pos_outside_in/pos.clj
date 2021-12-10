(ns com.amithgeorge.pos-outside-in.pos
  (:require [com.amithgeorge.pos-outside-in.catalogue :as catalogue]
            [com.amithgeorge.pos-outside-in.display :as display]
            [clojure.string :as str]
            [com.amithgeorge.pos-outside-in.cart :as cart]))

(defn scan
  ([catalogue display add-to-cart-fn code]
   (if (str/blank? code)
     (display/invalid-code display)
     (if-let [product-price (catalogue/price catalogue code)]
       (do
         (add-to-cart-fn {:code code, :price product-price})
         (display/price display product-price))
       (display/not-found display))))
  ([catalogue display add-to-cart-fn cart code]
   (if (str/blank? code)
     (display/invalid-code display)
     (if-let [product-price (catalogue/price catalogue code)]
       (do
         (add-to-cart-fn {:code code, :price product-price})
         (cart/add cart {:code code, :price product-price})
         (display/price display product-price))
       (display/not-found display)))))