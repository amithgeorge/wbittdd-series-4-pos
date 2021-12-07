(ns com.amithgeorge.pos-outside-in.catalogue)

(defprotocol Catalogue
  (price [this code] "Returns the price for product identified by the given code"))