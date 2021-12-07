(ns com.amithgeorge.pos-outside-in.pos
  (:require [com.amithgeorge.pos-outside-in.catalogue :as catalogue]
            [com.amithgeorge.pos-outside-in.display :as display]))

(defn scan
  [catalogue display code]
  (display/price display (catalogue/price catalogue code)))