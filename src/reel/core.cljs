
(ns reel.core (:require [clojure.string :as string]))

(defn reel-updater [updater reel op op-data op-id]
  (comment println "Name:" (name op))
  (if (string/starts-with? (str op) ":reel/")
    (case op
      :reel/toggle (update reel :display? not)
      :reel/recall
        (let [[idx a-store] op-data]
          (-> reel (assoc :pointer idx) (assoc :stopped? true) (assoc :store a-store)))
      :reel/run
        (-> reel (assoc :store op-data) (assoc :stopped? false) (assoc :pointer nil))
      :reel/merge
        (-> reel
            (assoc :store op-data)
            (assoc :initial-store op-data)
            (assoc :stopped? false)
            (assoc :pointer nil)
            (assoc :records []))
      :reel/reset
        (-> reel
            (assoc :store (:initial-store reel))
            (assoc :pointer nil)
            (assoc :records [])
            (assoc :stopped? false))
      (do (println "Unknown reel/ op:" op) reel))
    (let [data-pack [op op-data op-id]]
      (if (:stopped? reel)
        (-> reel (update :records (fn [records] (conj records data-pack))))
        (-> reel
            (assoc :store (updater (:store reel) op op-data op-id))
            (update :records (fn [records] (conj records data-pack))))))))

(defn play-records [store records updater]
  (if (empty? records)
    store
    (let [[op op-data op-id] (first records), next-store (updater store op op-data op-id)]
      (recur next-store (rest records) updater))))

(defn replay-store [reel updater idx]
  (let [records-slice (subvec (:records reel) 0 idx)]
    (play-records (:initial-store reel) records-slice updater)))