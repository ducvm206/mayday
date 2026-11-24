package com.ducvm.mayday.dataset;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record GetMedicalRecordResult(
		Long id,

		@JsonProperty("patient_id")
		Long patientId,

		@JsonProperty("record_type")
		String recordType,

		@JsonProperty("first_visit_time")
		Instant firstVisitTime,

		@JsonProperty("outpatient_treatment_start_date")
		LocalDate outpatientTreatmentStartDate,

		@JsonProperty("outpatient_treatment_end_date")
		LocalDate outpatientTreatmentEndDate,

		@JsonProperty("referral_diagnosis")
		String referralDiagnosis,

		@JsonProperty("initial_clinic_diagnosis")
		String initialClinicDiagnosis,

		@JsonProperty("insurance_treatment_outcome")
		String insuranceTreatmentOutcome,

		@JsonProperty("onset_duration_weeks")
		BigDecimal onsetDurationWeeks,

		String status,

		@JsonProperty("family_history_status")
		String familyHistoryStatus,

		@JsonProperty("family_history_details")
		String familyHistoryDetails,

		@JsonProperty("previously_treated")
		Boolean previouslyTreated
) {}

