(ns com.amithgeorge.pos-outside-in.catalogue)

(def AmountSchema
  [:map [:amount [:fn decimal?]]])

(defprotocol Catalogue
  (price [this code] "Returns the price for product identified by the given code"))