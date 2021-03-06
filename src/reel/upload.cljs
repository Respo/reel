
(ns reel.upload
  (:require ["child_process" :as cp] [reel.config :as config] [cumulo-util.file :refer [sh!]])
  (:require-macros [clojure.core.strint :refer [<<]]))

(defn main! []
  (sh! (<< "rsync -avr --progress dist/* ~(:cdn-folder config/site)"))
  (sh!
   (<<
    "rsync -avr --progress dist/{index.html,manifest.json} ~(:upload-folder config/site)")))

(defn reload! [] )
