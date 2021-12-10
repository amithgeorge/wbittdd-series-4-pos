(ns com.amithgeorge.pos-outside-in.pos
  (:require [com.amithgeorge.pos-outside-in.catalogue :as catalogue]
            [com.amithgeorge.pos-outside-in.display :as display]
            [clojure.string :as str]
            [com.amithgeorge.pos-outside-in.cart :as cart]
            [com.amithgeorge.pos-outside-in.persistence :as persistence]))

(defn scan
  ([catalogue display storage cart code]
   (if (str/blank? code)
     (display/invalid-code display)
     (if-let [product-price (catalogue/price catalogue code)]
       (do
         (persistence/save-cart! storage (cart/add-to cart code product-price))
         (display/price display product-price))
       (display/not-found display)))))

(defn total
  ([display cart]
   (if (cart/empty? cart)
     (display/cart-empty display)
     (display/total display (cart/total cart))))
  ([display inmemory-cart initial-cart]
   (if (nil? initial-cart)
     (if (cart/empty? inmemory-cart)
       (display/cart-empty display)
       (display/total display (cart/total inmemory-cart)))
     (if (cart/empty-cart? initial-cart)
       (display/cart-empty display)
       (display/total display (cart/total inmemory-cart))))))
