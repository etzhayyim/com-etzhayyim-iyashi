# com.etzhayyim.iyashi.* — iyashi (癒) Lexicons

**Owner actor**: `did:web:iyashi.etzhayyim.com` (this repository)
**ADR**: ADR-2605263000 (R0 scaffold); ADR-2605281950 (L7 phlebotomyAttestation, R2+)
**Status**: R0 schema skeletons. Full schemas (additionalProperties=false + required fields + encryptedPayloadCid mandatory on L1+L3) at R1+.

## 7 Lexicons (6 R0 + 1 R2)

| # | Lexicon | Consumer cell | Purpose |
|---|---|---|---|
| L1 | `clinicalEncounterAttestation` | primary_care_encounter + acute_first_line | Per-encounter; **encryptedPayloadCid REQUIRED** (G2 structural) |
| L2 | `providerAttestation` | provider_lifecycle | Credentialing; medical-license-cite-CID + Council ≥3 attestation + L5 vocation-flow link to chigiri.stewardLaborAttestation |
| L3 | `chronicCareContinuityRecord` | chronic_care_followup | Longitudinal; **encryptedPayloadCid REQUIRED**; cross-link with hagukumi.continuitySessionAttestation |
| L4 | `vaccinationAttestation` | vaccination_administration | Per-administration; vaccine ID + lot # (yakushi cross-link if iyashi-administered yakushi-produced) + adverse-event-flag |
| L5 | `clinicFacilityAttestation` | clinic_facility | Per-clinic-site standards; annual audit; building + equipment + sterile-zone certification |
| L6 | `silenIyashiReview` | (Council attestation scope) | Wellbecoming + quality + multi-generational ratio + Charter Rider §1.13 compliance quarterly Council review |
| L7 | `phlebotomyAttestation` | internal_phlebotomy (iyashi R2+ per [ADR-2605281950](../../../../../90-docs/adr/2605281950-mitate-r2-general-lab-orders-and-iyashi-phlebotomy.md)) | Per-event log for in-house venipuncture serving a `mitate.diagnosticOrder` with `orderRoutingTarget=iyashi-internal`; **phlebotomistClass const "community-witnessed-competent" + employmentRelation const "vocation-flow" + lLevel const "L5"** structural (ADR-2605281950 GE) |

## Schema Discipline (R1+)

All 6 Lexicons at R1 will enforce:

- `additionalProperties: false` at top-level record schema;
- `required` list covering every constitutionally-relevant field;
- L1 `clinicalEncounterAttestation` + L3 `chronicCareContinuityRecord`
  MUST carry `encryptedPayloadCid` (G2 structural — rejects plaintext
  content via additionalProperties=false);
- L2 `providerAttestation.lLevel` MUST be `"L5"`, `.employmentRelation`
  MUST be `"vocation-flow"` (G14 structural; cross-actor enforcement
  with chigiri.stewardLaborAttestation);
- L4 `vaccinationAttestation` adverse-event-flag is mandatory per
  administration record (regulatory transparency).

## R0 Status

Schemas at R0 are skeleton-level: the field set is enumerated but
strict validation + encryptedPayloadCid enforcement is not yet active
(schemas evolve through R1 Council attestation review).

## Related Files

- `manifest.edn`
- `README.md`
- `CLAUDE.md`
- `/90-docs/adr/2605263000-iyashi-clinical-care-provider-tier-b-actor-r0.md`
- `/90-docs/adr/2605181100-mst-encrypted-records-signal-keywrap.md` — privacy envelope
- `/00-contracts/lexicons/com/etzhayyim/hagukumi/` — sibling Lexicon pattern reference
