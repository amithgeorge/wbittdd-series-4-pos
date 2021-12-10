(ns com.amithgeorge.pos-outside-in.persistence)

(defprotocol Persistence
  (save-cart! [this cart] "Persist the cart to some storage"))