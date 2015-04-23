(ns cost-analyzer.spreadsheet-service
  (:import (com.google.gdata.client.spreadsheet SpreadsheetService
                                                SpreadsheetQuery
                                                WorksheetQuery
                                                FeedURLFactory)
           (com.google.gdata.data.spreadsheet SpreadsheetFeed
                                              WorksheetFeed
                                              ListFeed)
           (com.google.gdata.util.ServiceException)
           (java.net URL)))


(defmacro plain-text-title [x]
  `(.. ~x getTitle getPlainText))

(def service (SpreadsheetService. "cost-analyzer"))

(defn find-spreadsheet [credential spreadsheet-name]
  (let [url (URL. "https://spreadsheets.google.com/feeds/spreadsheets/private/full")
        query (SpreadsheetQuery. url)]
    (.setOAuth2Credentials service credential)
    (.setTitleQuery query spreadsheet-name)
    (let [feed (.query service query SpreadsheetFeed)]
      (first (.getEntries feed)))))

(defn find-worksheet [credential spreadsheet-name worksheet-name]
  (let [spreadsheet (find-spreadsheet credential spreadsheet-name)
        url (.getWorksheetFeedUrl spreadsheet)
        query (WorksheetQuery. url)]
    (.setTitleQuery query worksheet-name)
    (let [feed (.query service query WorksheetFeed)]
      (first (.getEntries feed)))))

(defn only-household-account-sheets [list-feed]
  (filter #(re-find #"\d{4}\/\d{2}" (plain-text-title %))
          (.getEntries list-feed)))

(defn sheet->matrix [sheet]
  (let [list-feed-url (.getListFeedUrl sheet)
        list-feed (.getFeed service list-feed-url ListFeed)
        filtered-list-feed (only-household-account-sheets list-feed)]
    (mapv (fn [list-entry] (mapv #(.getValue (.getCustomElements list-entry) %)
                                 (.getTags (.getCustomElements list-entry))))
          filtered-list-feed)))
