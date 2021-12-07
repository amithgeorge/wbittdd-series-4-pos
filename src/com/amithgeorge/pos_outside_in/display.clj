(ns com.amithgeorge.pos-outside-in.display)

(defprotocol Display
  (price [this amount] "Displays the price after formatting it"))