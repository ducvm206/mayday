package com.ducvm.mayday.service;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.entity.*;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.repository.*;
import com.ducvm.mayday.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BranchRepository branchRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private RoomRepository roomRepository;

	public List<GetUsersResult> getUsers(GetUsersRequest request) {

		int page = request.page() != null ? request.page() : 1;
		int limit = request.limit() != null ? request.limit() : 20;

		if (page < 1 || limit < 1) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		Pageable pageable = PageRequest.of(page - 1, limit);

		Page<Account> accountPage;

		if (request.roleId() != null) {
			accountPage = accountRepository.findByRole_Id(
					request.roleId(), pageable
			);
		} else {
			accountPage = accountRepository.findAll(pageable);
		}

		List<Long> employeeIds = accountPage.getContent()
				.stream()
				.map(Account::getPersonId)
				.filter(Objects::nonNull)
				.toList();

		Map<Long, Employee> employeeMap = employeeRepository
				.findAllById(employeeIds)
				.stream()
				.collect(Collectors.toMap(
						Employee::getId,
						employee -> employee
				));

		List<GetUsersResult> results = new ArrayList<>();

		for (Account account : accountPage.getContent()) {

			Employee employee = employeeMap.get(account.getPersonId());

			results.add(new GetUsersResult(
					account.getId(),
					employee != null ? employee.getFullName() : null,
					account.getUsername(),
					account.getRole().getId(),
					Boolean.TRUE.equals(account.getActive()) ? 1 : 0
			));
		}

		return results;
	}

	public GetPatientResult getPatientUser(Long id) {

		Account account = accountRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Patient patient = patientRepository.findById(account.getPersonId())
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		return new GetPatientResult(
				account.getId(),
				patient.getFullName(),
				account.getUsername(),
				account.getRole().getId(),
				patient.getSex(),
				patient.getDateOfBirth() != null
						? patient.getDateOfBirth().getYear()
						: null,
				patient.getOccupation(),
				patient.getEthnicity(),
				patient.getAddress(),
				patient.getWard(),
				patient.getCity(),
				null,
				patient.getMedicalInsuranceNumber(),
				patient.getMedicalInsuranceExpiry() != null
						? patient.getMedicalInsuranceExpiry().toString()
						: null,
				null,
				patient.getFamilyPhoneNumber(),
				null
		);
	}

	public long countUsers(GetUsersRequest request) {

		if (request.roleId() != null) {
			return accountRepository.countByRole_Id(request.roleId());
		}

		return accountRepository.count();
	}

	@Transactional
	public CreateUserResult createUser(CreateUserRequest req) {

		if (req.fullName() == null || req.fullName().isBlank()
				|| req.phone() == null || req.phone().isBlank()
				|| req.password() == null || req.roleId() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		if (!req.phone().matches(Constant.PHONE_NUMBER_REGEX)
				|| req.password().length() < 8) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		int active = req.isActive() == null ? 1 : req.isActive();

		if (active != 0 && active != 1) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		if (accountRepository.existsByUsername(req.phone())) {
			throw new ApiException(ResponseCode.USER_EXISTED);
		}

		Roles roles = getRoleEnum(req.roleId());

		Role role = roleRepository.findById(req.roleId())
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		String sex = null;

		if (req.gender() != null && !req.gender().isBlank()) {
			sex = switch (req.gender().trim().toLowerCase()) {
				case "male" -> "Nam";
				case "female" -> "Nu";
				default -> throw new ApiException(
						ResponseCode.PARAMETER_VALUE_INVALID);
			};
		}

		Person person;

		if (roles == Roles.ADMIN || roles == Roles.DOCTOR || roles == Roles.NURSE) {

			if (req.branchId() == null) {
				throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
			}

			Branch branch = branchRepository.findById(req.branchId())
					.orElseThrow(() ->
							new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

			if (!Boolean.TRUE.equals(branch.getStatus())) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			Employee employee = new Employee();
			employee.setBranch(branch);
			employee.setFullName(req.fullName().trim());
			employee.setPhone(req.phone());
			employee.setSex(sex);

			person = employeeRepository.save(employee);

		} else if (roles == Roles.PATIENT) {

			Patient patient = new Patient();
			patient.setFullName(req.fullName().trim());
			patient.setPhone(req.phone());
			patient.setSex(sex);

			person = patientRepository.save(patient);

		} else {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		Account account = new Account();
		account.setPersonId(person.getId());
		account.setRole(role);
		account.setUsername(req.phone());
		account.setEncodedPassword(passwordEncoder.encode(req.password()));
		account.setActive(active == 1);

		accountRepository.save(account);

		return new CreateUserResult(
				account.getId(),
				person.getFullName(),
				account.getUsername(),
				role.getId(),
				active
		);
	}

	@Transactional
	public UpdateUserResult updateUser(Long id, UpdateUserRequest req) {

		Account account = accountRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Person person;
		Roles currentRole = getRoleEnum(account.getRole().getId());

		if (currentRole == Roles.PATIENT) {
			person = patientRepository.findById(account.getPersonId())
					.orElseThrow(() ->
							new ApiException(ResponseCode.OBJECT_NOT_EXISTED));
		} else {
			person = employeeRepository.findById(account.getPersonId())
					.orElseThrow(() ->
							new ApiException(ResponseCode.OBJECT_NOT_EXISTED));
		}

		// Update full name
		if (req.fullName() != null) {
			if (req.fullName().isBlank()) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			person.setFullName(req.fullName().trim());
		}

		// Update gender
		if (req.gender() != null) {
			if (!req.gender().equals("Nam") && !req.gender().equals("Nu")) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			person.setSex(req.gender());
		}

		// Update role
		if (req.roleId() != null) {

			Roles newRole = getRoleEnum(req.roleId());

			boolean currentIsPatient = currentRole == Roles.PATIENT;
			boolean newIsPatient = newRole == Roles.PATIENT;

			if (currentIsPatient != newIsPatient) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			Role role = roleRepository.findById(req.roleId())
					.orElseThrow(() ->
							new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

			account.setRole(role);
		}

		// Update employee branch
		if (person instanceof Employee employee && req.branchId() != null) {

			Branch branch = branchRepository.findById(req.branchId())
					.orElseThrow(() ->
							new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

			if (!Boolean.TRUE.equals(branch.getStatus())) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			employee.setBranch(branch);
		}

		// Update account status
		if (req.isActive() != null) {
			if (req.isActive() != 0 && req.isActive() != 1) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			account.setActive(req.isActive() == 1);
		}

		accountRepository.save(account);

		return new UpdateUserResult(
				account.getId(),
				person.getFullName(),
				person.getPhone(),
				person.getSex(),
				account.getRole().getId(),
				Boolean.TRUE.equals(account.getActive()) ? 1 : 0,
				person instanceof Employee employee
						? employee.getBranch().getId()
						: null
		);
	}

	@Transactional
	public void deleteUser(Long id) {

		Account account = accountRepository.findById(id)
				.orElseThrow(() ->
						new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Long personId = account.getPersonId();

		Roles role = getRoleEnum(account.getRole().getId());

		if (role == Roles.PATIENT) {

			Patient patient = patientRepository.findById(personId)
					.orElseThrow(() ->
							new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

			accountRepository.delete(account);
			patientRepository.delete(patient);

		} else if (role == Roles.ADMIN
				|| role == Roles.DOCTOR
				|| role == Roles.NURSE) {

			Employee employee = employeeRepository.findById(personId)
					.orElseThrow(() ->
							new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

			accountRepository.delete(account);
			employeeRepository.delete(employee);

		} else {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}
	}

	@Transactional
	public AssignEmployeeResult assignEmployee(Long userId, AssignEmployeeRequest request) {

		if (request.branchId() == null || request.departmentId() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		Account account = accountRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Roles role = getRoleEnum(account.getRole().getId());

		if (role == Roles.PATIENT) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		Employee employee = employeeRepository.findById(account.getPersonId())
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Branch branch = branchRepository.findById(request.branchId())
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		if (!Boolean.TRUE.equals(branch.getStatus())) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		Department department = departmentRepository.findById(request.departmentId())
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Room room = null;

		if (request.roomId() != null) {
			room = roomRepository.findById(request.roomId())
					.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));
		}

		employee.setBranch(branch);
		employeeRepository.save(employee);

		return new AssignEmployeeResult(
				account.getId(),
				branch.getId(),
				department.getId(),
				room != null ? room.getId() : null
		);
	}

	@Transactional
	public CreateRoleResult createRole(CreateRoleRequest request) {

		if (request.name() == null || request.name().isBlank()
				|| request.displayName() == null || request.displayName().isBlank()) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		String name = request.name().trim();
		String displayName = request.displayName().trim();

		if (name.length() > 50 || displayName.length() > 100) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		if (roleRepository.existsByName(name)) {
			throw new ApiException(ResponseCode.USER_EXISTED);
		}

		Role role = new Role();
		role.setName(name);
		role.setDisplayName(displayName);
		role.setDescription(request.description());

		role = roleRepository.save(role);

		return new CreateRoleResult(
				role.getId(),
				role.getName(),
				role.getDisplayName(),
				role.getDescription()
		);
	}

	@Transactional(readOnly = true)
	public List<GetRolesResult> getRoles() {
		return roleRepository.findAll()
				.stream()
				.map(role -> new GetRolesResult(
						role.getId(),
						role.getName(),
						role.getDescription()
				))
				.toList();
	}

	private Roles getRoleEnum(Long roleId) {
		try {
			return Roles.fromId(Math.toIntExact(roleId));
		} catch (IllegalArgumentException | ArithmeticException e) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}
	}
}
