package com.ducvm.mayday.service;

import com.ducvm.mayday.dataset.GetProfileResult;
import com.ducvm.mayday.dataset.UpdateProfileRequest;
import com.ducvm.mayday.entity.Account;
import com.ducvm.mayday.entity.Patient;
import com.ducvm.mayday.repository.AccountRepository;
import com.ducvm.mayday.repository.PatientRepository;
import com.ducvm.mayday.repository.PersonRepository;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.security.TokenBlacklistService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProfileService {
	/**
	 * Account repository.
	 */
	@Autowired
	private AccountRepository accountRepository;
	/**
	 * Patient repository.
	 */
	@Autowired
	private PatientRepository patientRepository;
	/**
	 * JWT service.
	 */
	@Autowired
	private JwtService jwtService;

	public GetProfileResult getProfile(String token) {

		// Extract phone number from JWT
		String phone = jwtService.extractUsername(token);

		Account account = requireActiveAccount(phone);

		Patient patient = patientRepository.findById(account.getPersonId())
				.orElseThrow(() ->
						new EntityNotFoundException("Patient not found"));

		return new GetProfileResult(
				account.getId(),
				patient.getFullName(),
				account.getUsername(),
				account.getRole().getId(),
				patient.getSex(),
				patient.getDateOfBirth().toString(),
				patient.getOccupation(),
				patient.getEthnicity(),
				patient.getAddress(),
				patient.getWard(),
				patient.getCity(),
				patient.getMedicalInsuranceNumber(),
				patient.getMedicalInsuranceExpiry() != null
						? patient.getMedicalInsuranceExpiry().toString()
						: null,
				null,
				patient.getFamilyPhoneNumber(),
				null
		);
	}

	@Transactional
	public GetProfileResult updateProfile(
			String token,
			UpdateProfileRequest req) {

		// Extract phone number from JWT
		String phone = jwtService.extractUsername(token);

		// Verify account exists and is active
		Account account = requireActiveAccount(phone);

		// Find corresponding patient
		Patient patient = patientRepository.findById(account.getPersonId())
				.orElseThrow(() ->
						new EntityNotFoundException("Patient not found"));

		// Update Person fields
		if (req.fullName() != null) {
			patient.setFullName(req.fullName().trim());
		}

		if (req.gender() != null) {
			patient.setSex(req.gender());
		}

		if (req.dateOfBirth() != null) {
			patient.setDateOfBirth(LocalDate.parse(req.dateOfBirth()));
		}

		if (req.hamletAddress() != null) {
			patient.setAddress(req.hamletAddress());
		}

		if (req.communeAddress() != null) {
			patient.setWard(req.communeAddress());
		}

		if (req.cityAddress() != null) {
			patient.setCity(req.cityAddress());
		}

		// Update Patient fields
		if (req.career() != null) {
			patient.setOccupation(req.career());
		}

		if (req.ethnic() != null) {
			patient.setEthnicity(req.ethnic());
		}

		if (req.healthInsuranceNumber() != null) {
			patient.setMedicalInsuranceNumber(
					req.healthInsuranceNumber()
			);
		}

		if (req.healthInsuranceValidate() != null) {
			patient.setMedicalInsuranceExpiry(
					LocalDate.parse(req.healthInsuranceValidate())
			);
		}

		if (req.nextOfKinPhone() != null) {
			patient.setFamilyPhoneNumber(req.nextOfKinPhone());
		}

		return getProfile(token);
	}

	/**
	 * Check if account exists and is active
	 * @param phone
	 * @return
	 */
	private Account requireActiveAccount(String phone) {
		Account account = accountRepository.findByUsername(phone)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));
		if (!Boolean.TRUE.equals(account.getActive())) {
			throw new DisabledException("Account is disabled");
		}
		return account;
	}
}
