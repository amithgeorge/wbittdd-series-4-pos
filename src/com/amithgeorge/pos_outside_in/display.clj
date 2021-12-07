(ns com.amithgeorge.pos-outside-in.display)

(defprotocol Display
  (price [this amount] "Displays the price after formatting it")
  (not-found [this] "Displays the item not found message")
  (invalid-code [this] "Displays the barcode is invalid message"))