(ns iyashi.murakumo
  "Pure cljc actor boundary generated from manifest migration scaffold."
  (:require [clojure.string :as str]))

(def actor-did
  "did:web:iyashi.etzhayyim.com")

(def common-gates
  [:council-charter-attestation
   :no-platform-held-key-baseline
   :no-probing-baseline
   :murakumo-only-inference-baseline
   :did-primary-baseline
   :append-only-gate-baseline
   :kotoba-only-substrate-baseline])

(defn collection
  [name]
  (str "com.etzhayyim.iyashi." name))

(def cell-specs {
  :primary_care_encounter {:legacy-cell "primary-care-encounter"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "primary_care_encounter")]
     :required-gates common-gates
     :trigger "manifest cell primary_care_encounter"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :chronic_care_followup {:legacy-cell "chronic-care-followup"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "chronic_care_followup")]
     :required-gates common-gates
     :trigger "manifest cell chronic_care_followup"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :vaccination_administration {:legacy-cell "vaccination-administration"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "vaccination_administration")]
     :required-gates common-gates
     :trigger "manifest cell vaccination_administration"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :acute_first_line {:legacy-cell "acute-first-line"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "acute_first_line")]
     :required-gates common-gates
     :trigger "manifest cell acute_first_line"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :provider_lifecycle {:legacy-cell "provider-lifecycle"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "provider_lifecycle")]
     :required-gates common-gates
     :trigger "manifest cell provider_lifecycle"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
  :clinic_facility {:legacy-cell "clinic-facility"
     :phase :event
     :murakumo-node "reuben"
     :collections [(collection "clinic_facility")]
     :required-gates common-gates
     :trigger "manifest cell clinic_facility"
     :ceiling "Manifest-driven migration scaffold; explicit execution stays in runtime methods"}
})

(defn safe-rkey
  [s]
  (let [clean (-> (str s)
                  (str/replace #"^did:web:" "")
                  (str/replace #"[^A-Za-z0-9._~-]" "-"))]
    (if (str/blank? clean) "unknown" clean)))

(defn gate-value
  [attestations gate]
  (or (get attestations gate)
      (get attestations (name gate))
      (when (set? attestations) (attestations gate))
      (when (set? attestations) (attestations (name gate)))))

(defn missing-gates
  [spec attestations]
  (->> (:required-gates spec)
       (remove #(boolean (gate-value attestations %)))
       vec))

(defn put-record-effect
  [collection rkey record]
  {:op :mst/put-record
   :actor actor-did
   :collection collection
   :rkey rkey
   :record record})

(defn records-for
  [spec {:keys [records record computed-at request-id]
         :as input}]
  (let [input-records (cond
                        (map? records) records
                        (some? record) {0 record}
                        :else {})
        base {:actorDid actor-did
              :computedAt computed-at
              :legacyCell (:legacy-cell spec)
              :phase (:phase spec)
              :requestId request-id
              :actorBoundary "cljc-migration-scaffold"
              :scaffold true
              :constitutionalStatus "attested-plan"}]
    (map-indexed
     (fn [idx coll]
       (let [record* (merge {:$type coll}
                            base
                            (or (get input-records coll)
                                (get input-records idx)
                                {}))
             rkey (safe-rkey (or (:rkey record*)
                                 (get record* "rkey")
                                 (:tid record*)
                                 request-id
                                 (str (:legacy-cell spec) "-" idx)))]
         {:collection coll
          :record record*
          :rkey rkey}))
     (:collections spec))))

(defn cell-plan
  [cell-key {:keys [attestations] :as input}]
  (let [spec (get cell-specs cell-key)]
    (when-not spec
      (throw (ex-info "unknown cell" {:cell cell-key})))
    (let [missing (missing-gates spec attestations)]
      (merge
       {:cell cell-key
        :legacy-cell (:legacy-cell spec)
        :actor actor-did
        :phase (:phase spec)
        :murakumo-node (:murakumo-node spec)
        :trigger (:trigger spec)
        :ceiling (:ceiling spec)
        :required-gates (:required-gates spec)
        :missing-gates missing}
       (if (seq missing)
         {:status :blocked
          :effects []}
         (let [planned-records (records-for spec input)]
           {:status :ready
            :records (vec planned-records)
            :effects (mapv (fn [{:keys [collection record rkey]}]
                             (put-record-effect collection rkey record))
                           planned-records)}))))))

(defn all-cell-plans
  [input]
  (into {}
        (map (fn [cell-key] [cell-key (cell-plan cell-key input)]))
        (keys cell-specs)))
