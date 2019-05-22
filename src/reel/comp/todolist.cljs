
(ns reel.comp.todolist
  (:require [respo.core :refer [defcomp <> div span button input list->]]
            [respo.comp.space :refer [=<]]
            [respo-ui.core :as ui]
            [reel.comp.task :refer [comp-task]]))

(defn on-click [state] (fn [e dispatch! mutate!] (dispatch! :task/add state) (mutate! "")))

(defn on-input [e dispatch! mutate!] (mutate! (:value e)))

(def style-container {:padding 8, :overflow :auto})

(defcomp
 comp-todolist
 (states tasks)
 (let [state (or (:data states) "")]
   (div
    {:style (merge ui/fullscreen style-container)}
    (div
     {}
     (input
      {:placeholder "Task to add...",
       :value state,
       :style ui/input,
       :on-input on-input,
       :on-keydown (fn [e d! m!] (if (= (:keycode e) 13) (do (d! :task/add state) (m! ""))))})
     (=< 8 nil)
     (button {:style ui/button, :on-click (on-click state)} (<> "Add")))
    (list-> {} (->> tasks (map (fn [task] [(:id task) (comp-task task)])))))))
