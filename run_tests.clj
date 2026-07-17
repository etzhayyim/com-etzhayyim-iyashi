#!/usr/bin/env bb
(require '[babashka.classpath :as cp]
         '[babashka.fs :as fs]
         '[clojure.test :as t])
(let [root (fs/parent (fs/absolutize *file*))]
  (cp/add-classpath (str root "/src"))
  (cp/add-classpath (str root "/test")))
(def suites '[iyashi.murakumo-test
              iyashi.methods.test-charter-gates
              iyashi.methods.test-manifest-invariants])
(apply require suites)
(let [{:keys [fail error]} (apply t/run-tests suites)]
  (System/exit (if (zero? (+ fail error)) 0 1)))
