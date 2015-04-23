(ns cost-analyzer.core
  (:gen-class)
  (:require [cost-analyzer.auth :refer [credentials]]
            [cost-analyzer.spreadsheet-service :refer [find-worksheet find-spreadsheet sheet->matrix]]))

(defn -main [& args]
  (let [sheet (find-worksheet (credentials) "家計簿" "2015/03")]
    (println (mapv (fn [[k v]]
                     [k (reduce + 0 (map #(-> %
                                              (nth 3)
                                              Integer/parseInt)
                                         v))])
                   (group-by second (sheet->matrix sheet)))))
  (System/exit 0))
