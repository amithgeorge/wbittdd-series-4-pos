(ns com.amithgeorge.pos-outside-in.pos
  (:require [com.amithgeorge.pos-outside-in.catalogue :as catalogue]
            [com.amithgeorge.pos-outside-in.display :as display]
            [clojure.string :as str]
            [com.amithgeorge.pos-outside-in.cart :as cart]
            [com.amithgeorge.pos-outside-in.persistence :as persistence]))

(defn scan
  ([catalogue display cart code]
   (if (str/blank? code)
     (display/invalid-code display)
     (if-let [product-price (catalogue/price catalogue code)]
       (do
         (cart/add cart {:code code, :price product-price})
         (display/price display product-price))
       (display/not-found display))))
  ([catalogue display storage cart code]
   (if (str/blank? code)
     (display/invalid-code display)
     (if-let [product-price (catalogue/price catalogue code)]
       (do
         (cart/add cart {:code code, :price product-price})
         (persistence/save-cart! storage {})
         (display/price display product-price))
       (display/not-found display)))))

(defn total
  [display cart]
  (if (cart/empty? cart)
    (display/cart-empty display)
    (display/total display (cart/total cart))))
