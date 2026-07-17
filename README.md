# iyashi (癒) — Non-profit Religious-Corp Clinical Care Provider Substrate

**DID**: `did:web:iyashi.etzhayyim.com`
**Namespace**: `com.etzhayyim.iyashi.*`
**ADR**: ADR-2605263000 (R0 scaffold)
**Status**: R0 scaffold (2026-05-26) — 6 cells path-reserved (import-time RuntimeError per G2 privacy invariant) + 6 Lexicon skeletons
**L4 Care Tier siblings**: yakushi (pharma) / mitate (diagnosis) / hagukumi (daily-living)
**Cross-actor data**: mitate / hagukumi / yakushi / toritate / chigiri / manabi

## Overview

Religious-corp clinical care provider substrate. Community-clinic-model
primary care + chronic followup + vaccination + acute first-line +
maternity/pediatric clinical + provider lifecycle + facility
standards. Completes the L4 Care Tier triad alongside yakushi (mfg) +
mitate (diagnosis) + hagukumi (daily-living).

## Identity (CRITICAL — IMMUTABLE)

- **Encrypted PHI envelope MANDATORY** (G2 per ADR-2605181100) —
  `clinicalEncounterAttestation` + `chronicCareContinuityRecord`
  require `encryptedPayloadCid`; plaintext content fields rejected at
  schema layer (R1 `additionalProperties: false`). Clinical PHI is the
  most sensitive observation class in religious-corp; structural
  privacy enforcement, not policy.
- **No video recording** (G3) — firmware-level enforcement; live
  telepresence permitted, frame-write-to-disk PROHIBITED.
- **No commercial EHR** (G11) — Epic / Cerner / Athena / Allscripts /
  NextGen / eClinicalWorks / Greenway / Practice Fusion PROHIBITED
  per Charter Rider §2(e) anti-gatekeeping + §2(c) covert-ops vendor
  concern (vendor closed query-tracking exposes patient + provider
  posture).
- **No insurance billing** (G13) — funded via Public Fund grant +
  sliding-scale donation only (chigiri.taxReceipt handles donor-side
  receipts per ADR-2605262700). State insurance integration OUT OF
  SCOPE at all phases (requires 宗教法人法 登記 which is
  constitutionally excluded per Preamble §0.4).
- **No payroll for providers** (G14) — providers are vocation-flow L5
  stewards per Liberation Ladder L0..L6 (volunteer ≠ employee per
  chigiri G13 + toritate G12 cross-actor enforcement).
- **Murakumo-only inference** (G12) — clinical decision support via
  judah LiteLLM only; vendor clinical-AI (Microsoft Nuance DAX /
  Abridge / Suki AI / Augmedix) PROHIBITED.
- **Human-in-loop ALWAYS** (G8) — AI-assist requires synchronous
  provider sign-off; no AI-only clinical decision.
- **NOT a state-licensed medical entity** — religious-corp itself is
  NOT licensed; individual providers carry state license per
  jurisdiction; iyashi is the procedural + attestation substrate
  (UPL analog to chigiri G14).

## 6 Pregel Cells (R0 path-reserved)

All cells path-reserved under `kotoba-lang/kotodama-cells/iyashi_*/`.
Cell modules created at R1 ratification, import-time
`RuntimeError("iyashi R0 scaffold: activate via Council ADR + R1 ratification + encrypted-record framework Council-attested production-ready")`.

| Cell | Node | Phase | I/O |
|---|---|---|---|
| `iyashi_primary_care_encounter` | levi | session | patientDid + consentCid → clinicalEncounterAttestation (encrypted) |
| `iyashi_chronic_care_followup` | levi (hagukumi-paired) | longitudinal | chronicConditionState → chronicCareContinuityRecord (encrypted) |
| `iyashi_vaccination_administration` | levi | event | patientDid + vaccineId + consentCid → vaccinationAttestation |
| `iyashi_acute_first_line` | levi (mitate-paired) | session | symptom triage → encounter OR mitate emergency referral |
| `iyashi_provider_lifecycle` | reuben | event | candidate provider DID → providerAttestation (Council ≥3 + medical-license-cite-CID) |
| `iyashi_clinic_facility` | reuben | annual (event) | facility audit → clinicFacilityAttestation |

## 6 Lexicons under `com.etzhayyim.iyashi.*`

| Lexicon | Description |
|---|---|
| `clinicalEncounterAttestation` | Per-encounter; **encryptedPayloadCid REQUIRED** (G2) |
| `providerAttestation` | Credentialing; medical-license-cite-CID + Council ≥3 + L5 vocation-flow link |
| `chronicCareContinuityRecord` | Longitudinal; encryptedPayloadCid REQUIRED; cross-link with hagukumi |
| `vaccinationAttestation` | Per-administration; vaccine ID + lot # (yakushi cross-link) + adverse-event-flag |
| `clinicFacilityAttestation` | Per-clinic-site standards; annual audit |
| `silenIyashiReview` | Council Wellbecoming + quality + multi-gen ratio quarterly review |

See `lex/README.md` for canonical schemas.

## Constitutional Gates (G1–G14) — IMMUTABLE R0–R3

See ADR-2605263000 §5. Key:

- **G2** Encrypted envelope MANDATORY (most sensitive PHI in religious-corp)
- **G3** No video recording (firmware-level)
- **G5** Provider Council vetting + medical-license-cite-CID
- **G8** Human-in-loop ALWAYS for AI-assist
- **G10** Emergency escalation to mitate via shared keyword lexicon
- **G11** NO commercial EHR
- **G12** Murakumo-only inference
- **G13** NO insurance billing
- **G14** NO payroll for providers (vocation-flow L5)

## Non-Goals (N1–N12) — EXCLUDED from R0–R3

See ADR-2605263000 §6.

## Roadmap

| Phase | Timeline | Scope | L-gate |
|---|---|---|---|
| **R0** | 2026-05-26 | Scaffold (this commit) | — |
| **R1** | post-Council + ≥1 licensed-MD + encrypted-record production | 2 core cells + ≤20 patients pilot, single clinic | future ADR |
| **R2** | post-R1 + 30-day public + 5 site attestations | +4 cells + ≤200 patient ceiling 5 clinics | **L4 eligibility** |
| **R3** | post-R2 + Council Lv7+ + multi-jurisdictional | All 6 cells + 50 clinics ≤25,000 patient capacity | **L4→L5 required** |

## Cross-actor Relationships

| Actor | Direction | Purpose |
|---|---|---|
| `mitate` | ↔ | Diagnosis ↔ clinical encounter; emergency keyword shared (G10) |
| `hagukumi` | ↔ | Daily-living care continuity (chronic_care_followup pair) |
| `yakushi` | ← (medication) | Sukoyaka cold-chain delivery for vaccination + prescription |
| `toritate` | → (read) | Donation + Public Fund grant accounting |
| `chigiri.stewardLaborAttestation` | → (read) | Provider L5 vocation-flow classification (G14) |
| `chigiri.consentRecord` | → (read) | Per-encounter consent (G4) |
| `manabi` | ↔ | Provider continuing-medical-education curriculum + tracking |
| `kokoro` (future) | ↔ | Mental health (postnatal mood / chronic mental health continuity) |

## R0 Status

**Scaffold only.** No cells exist yet (W1). Lexicon schemas are
skeleton only — required-field validation + encryptedPayloadCid
enforcement lands at R1 Council attestation review.

## Related Files

- `manifest.edn`
- `CLAUDE.md`
- `lex/ (canonical EDN; wire/ contains external JSON)` (6 Lexicons + README)
- `/90-docs/adr/2605263000-iyashi-clinical-care-provider-tier-b-actor-r0.md`
- `/90-docs/adr/2605181100-mst-encrypted-records-signal-keywrap.md` — privacy envelope
- `/90-docs/adr/2605261000-labor-liberation-transition-mechanism.md` — L4 gate
- `/90-docs/adr/2605261030-hagukumi-care-tier-b-actor-r0.md` — sibling R0 pattern
- `/CHARTER-RIDER.md` §2(e) + §2(c) — G11 commercial EHR prohibition source
- `/CLAUDE.md` — Religious-corp status table
