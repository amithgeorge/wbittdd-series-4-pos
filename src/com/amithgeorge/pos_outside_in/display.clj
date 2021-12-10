(ns com.amithgeorge.pos-outside-in.display)

(defprotocol Display
  (price [this amount] "Displays the price after formatting it")
  (not-found [this] "Displays the item not found message")
  (invalid-code [this] "Displays the barcode is invalid message")
  (cart-empty [this] "Displays the cart is empty message")
  (total [this amount] "Displays the total after formatting it"))