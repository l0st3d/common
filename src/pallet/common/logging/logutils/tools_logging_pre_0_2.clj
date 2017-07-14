;;;; WARNING This file is loaded and not required

;; pre tools.logging 0.2.0
(deftype NullLogger
    []
  clojure.tools.logging.Log
  (impl-enabled? [log level] false)
  (impl-write! [log level throwable message]))

(def null-log (delay (NullLogger.)))

(deftype NullLoggerFactory
    []
  clojure.tools.logging.LogFactory
  (impl-name [factory] "null logger")
  (impl-get-log [factory log-ns] @null-log))

(def null-logger-factory (delay (NullLoggerFactory.)))

;;; A stdout logger
;;; Logs everyting to stdout.  Can be useful to test logging.
(deftype StdoutLogger
    []
  clojure.tools.logging.Log
  (impl-enabled? [log level] true)
  (impl-write! [log level throwable message]
    (println (name level) message)
    (when throwable
      (stacktrace/print-stack-trace
       (stacktrace/root-cause throwable)))))

(def stdout-log (delay (StdoutLogger.)))

(deftype StdoutLoggerFactory
    []
  clojure.tools.logging.LogFactory
  (impl-name [factory] "stdout logger")
  (impl-get-log [factory log-ns] @stdout-log))

(def stdout-logger-factory (delay (StdoutLoggerFactory.)))

(defmacro with-logger-factory
  [factory# & body#]
  `(binding [logging/*log-factory* ~factory#] ~@body#))
