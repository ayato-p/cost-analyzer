(ns cost-analyzer.auth
  (:import (com.google.api.client.auth.oauth2 Credential)
           (com.google.api.client.googleapis.auth.oauth2 GoogleAuthorizationCodeRequestUrl
                                                         GoogleAuthorizationCodeTokenRequest
                                                         GoogleCredential$Builder
                                                         GoogleTokenResponse)
           (com.google.api.client.http HttpTransport)
           (com.google.api.client.http.javanet NetHttpTransport)
           (com.google.api.client.json.jackson JacksonFactory)
           (com.google.gdata.util ServiceException))
  (:require [clojure.java.browse :as browse]))

(def config (baum/read-config "~/.cost-analyzer.edn"))

(defn credentials []
  (let [{:keys [client-id client-secret scopes redirect-uri]} config
        transport (NetHttpTransport.)
        json-factory (JacksonFactory.)
        authorization-url (.build (GoogleAuthorizationCodeRequestUrl. client-id redirect-uri scopes))]
    (println "自動的にブラウザが開くので、 Google の認証を通してください。")
    (browse/browse-url authorization-url)
    (print "表示されたコードを貼り付けてください。-> ")
    (flush)

    (let [code (read-line)
          response (.execute (GoogleAuthorizationCodeTokenRequest. transport json-factory client-id
                                                                   client-secret code redirect-uri))
          credential (.build
                      (doto (GoogleCredential$Builder.)
                        (.setClientSecrets client-id client-secret)
                        (.setJsonFactory json-factory)
                        (.setTransport transport)))]
      (doto credential
        (.setAccessToken (.getAccessToken response))
        (.setRefreshToken (.getRefreshToken response))))))
