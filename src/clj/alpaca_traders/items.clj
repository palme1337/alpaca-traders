(ns alpaca-traders.items
  (:require 
    [alpaca-traders.rethink-driver :as driver]
    [rethinkdb.query :as r]))

;TODO: Server side cache would be good. This table doesn't really change

(defn get-items [] 
  (with-open [conn (driver/get-conn)] 
    (-> (r/db driver/DB_NAME)
        (r/table "items")
        (r/run conn)
        )))
