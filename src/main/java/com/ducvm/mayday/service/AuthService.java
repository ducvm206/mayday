package com.ducvm.mayday.service;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.entity.*;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.enums.Sex;
import com.ducvm.mayday.exceptions.InvalidFieldException;
import com.ducvm.mayday.exceptions.UserExistedException;
import com.ducvm.mayday.repository.*;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.security.TokenBlacklistService;
import com.ducvm.mayday.util.Constant;
import com.ducvm.mayday.util.SmsSender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

@Service
public class AuthService {
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
	 * Role repository.
	 */
	@Autowired
	private RoleRepository roleRepository;
	/**
	 * Branch repository.
	 */
	@Autowired
	private BranchRepository branchRepository;
	/**
	 * Clinic repository.
	 */
	@Autowired
	private RoomRepository roomRepository;
	/**
	 * Department repository.
	 */
	@Autowired
	private DepartmentRepository departmentRepository;
	/**
	 * Employee repository.
	 */
	@Autowired
	private EmployeeRepository employeeRepository;
	/**
	 * Person repository.
	 */
	@Autowired
	private PersonRepository personRepository;
	/**
	 * Password encoder.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	/**
	 * JWT service.
	 */
	@Autowired
	private JwtService jwtService;
	/**
	 * Authentication manager.
	 */
	@Autowired
	private AuthenticationManager authenticationManager;
	/**
	 * Token blacklist service.
	 */
	@Autowired
	private TokenBlacklistService tokenBlacklistService;
	/**
	 * OTP sending service.
	 */
	@Autowired
	private OtpService otpService;
	/**
	 * SMS sending module.
	 */
	@Autowired
	private SmsSender smsSender;
	/**
	 * Entity manager.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Signup a new patient.
	 */
	@Transactional
	public SignupResult signup(SignupRequest req) {
		String phone = req.getPhone().trim();

		if (accountRepository.existsByUsername(phone)) {
			throw new UserExistedException("Phone already registered");
		}

		Role role = roleRepository.findByName(Roles.PATIENT.getName())
				.orElseThrow(() -> new IllegalStateException("Patient role not found"));

		Sex sex = Sex.fromApiValue(req.getGender()).orElse(null);

		Patient patient = new Patient();
		patient.setFullName(req.getFullName().trim());
		patient.setPhone(phone);
		patient.setSex(sex == null ? null : sex.getValue());
		patient = patientRepository.save(patient);

		Account account = new Account();
		account.setPersonId(patient.getId());
		account.setRole(role);
		account.setUsername(phone);
		account.setEncodedPassword(passwordEncoder.encode(req.getPassword()));
		account.setActive(true);

		try {
			account = accountRepository.saveAndFlush(account);
		} catch (DataIntegrityViolationException e) {
			// concurrent signup that passed the exists check
			String msg = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
			if (msg != null && msg.contains("uq_tai_khoan_ten_dang_nhap")) {
				throw new UserExistedException("Phone already registered");
			}
			throw e;
		}

		entityManager.refresh(account);

		return new SignupResult(patient.getId(), patient.getFullName(), phone,
				sex == null ? null : sex.getApiValue(),
				role.getId(), account.getActive(), account.getCreatedAt());
	}

	/**
	 * Login to a patient account.
	 */
	@Transactional(readOnly = true)
	public LoginResult login(LoginRequest req) {
		Account account = requireActiveAccount(req.getPhone().trim());

		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(account.getUsername(), req.getPassword()));
		String token = jwtService.generateToken((UserDetails) auth.getPrincipal());

		Person person = personRepository.findById(account.getPersonId())
				.orElseThrow(() -> new UsernameNotFoundException("Person not found"));

		return new LoginResult(token, person.getId(), person.getFullName(),
				account.getUsername(), account.getRole().getId());
	}

	/**
	 * Logout: blacklist the token's JWT ID until the token expires.
	 *
	 * @param token raw JWT, without the "Bearer " prefix
	 * @throws JwtException if the token is malformed, tampered with or expired
	 */
	public void logout(String token) {
		tokenBlacklistService.blacklist(
				jwtService.extractJti(token),
				jwtService.extractExpiration(token));
	}

	/**
	 * Process forgot password request.
	 * @param phone
	 */
	public void forgotPassword(String phone) {
		requireActiveAccount(phone);
		String code = otpService.generate(phone);
		long minutes = Constant.OTP_EXPIRY_SECONDS / 60;
		smsSender.send(phone, "Mã xác thực của bạn là " + code
				+ ". Mã có hiệu lực " + minutes + " phút.");
	}

	/**
	 * Change password of patient.
	 * @param token
	 * @param req
	 */
	@Transactional
	public void changePassword(String token, ChangePasswordRequest req) {

		// Token must be valid (JwtException if not) and not logged out
		String phone = jwtService.extractUsername(token);
		if (tokenBlacklistService.isBlacklisted(jwtService.extractJti(token))) {
			throw new JwtException("Token has been revoked");
		}

		Account account = requireActiveAccount(phone);

		if (!passwordEncoder.matches(req.getOldPassword(), account.getEncodedPassword())) {
			throw new InvalidFieldException("old_password", "Old password is incorrect");
		}

		if (req.getNewPassword().equals(req.getOldPassword())) {
			throw new InvalidFieldException("new_password", "New password must differ from the old password");
		}

		account.setEncodedPassword(passwordEncoder.encode(req.getNewPassword()));
		accountRepository.save(account);
	}

	@Transactional
	public CreateStaffResult createStaff(String token, CreateStaffRequest req) {

		// Caller: valid, not revoked, active, Admin (role read from DB)
		if (tokenBlacklistService.isBlacklisted(jwtService.extractJti(token))) {
			throw new JwtException("Token has been revoked");
		}
		Account caller = requireActiveAccount(jwtService.extractUsername(token));
		if (!Roles.ADMIN.getName().equals(caller.getRole().getName())) {
			throw new AccessDeniedException("Access denied");
		}

		String phone = req.getPhone().trim();
		String fullName = req.getFullName().trim();

		// References -> InvalidFieldException (1004)
		Role role = roleRepository.findById(req.getRoleId())
				.orElseThrow(() -> new InvalidFieldException("role_id", "Role not found"));
		if (Roles.PATIENT.getName().equals(role.getName())) {   // add Admin here too if needed
			throw new InvalidFieldException("role_id", "Role is not a staff role");
		}

		Branch branch = branchRepository.findById(req.getBranchId())
				.orElseThrow(() -> new InvalidFieldException("branch_id", "Branch not found"));
		if (!Boolean.TRUE.equals(branch.getStatus())) {
			throw new InvalidFieldException("branch_id", "Branch is inactive");
		}

		Department department = departmentRepository.findById(req.getDepartmentId())
				.orElseThrow(() -> new InvalidFieldException("department_id", "Department not found"));
		if (!department.getBranchId().equals(branch.getId())) {
			throw new InvalidFieldException("department_id", "Department does not belong to the branch");
		}

		Room room = null;
		if (req.getRoomId() != null) {
			room = roomRepository.findById(req.getRoomId())
					.orElseThrow(() -> new InvalidFieldException("room_id", "Room not found"));
			if (!room.getBranchId().equals(branch.getId())) {
				throw new InvalidFieldException("room_id", "Room does not belong to the branch");
			}
		}

		// Phone (login name) must be free
		if (accountRepository.existsByUsername(phone)) {
			throw new UserExistedException("Phone already registered");
		}

		// Create person + employee
		Sex sex = Sex.fromApiValue(req.getGender()).orElse(null);

		Employee employee = new Employee();
		employee.setFullName(fullName);
		employee.setPhone(phone);
		employee.setSex(sex == null ? null : sex.getValue());
		employee.setBranch(branch);
		employee = employeeRepository.save(employee);

		// Create account
		Account account = new Account();
		account.setPersonId(employee.getId());
		account.setUsername(phone);
		account.setRole(role);
		account.setEncodedPassword(passwordEncoder.encode(req.getPassword()));
		account.setActive(true);


		try {
			accountRepository.saveAndFlush(account);
		} catch (DataIntegrityViolationException e) {
			String msg = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
			if (msg != null && msg.contains("uq_tai_khoan_ten_dang_nhap")) {
				throw new UserExistedException("Phone already registered");
			}
			throw e;
		}

		return new CreateStaffResult(employee.getId(), employee.getFullName(),
				role.getId(), branch.getId(), department.getId());
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
