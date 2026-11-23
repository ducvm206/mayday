package com.ducvm.mayday.service;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.entity.Department;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.repository.BranchRepository;
import com.ducvm.mayday.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private BranchRepository branchRepository;

	@Transactional
	public CreateDepartmentResult createDepartment(CreateDepartmentRequest request) {
		if (request.name() == null || request.name().isBlank()
				|| request.departmentType() == null || request.departmentType().isBlank()
				|| request.branchId() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		String name = request.name().trim();
		String departmentType = request.departmentType().trim();

		if (name.length() > 150 || departmentType.length() > 50) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		if (!branchRepository.existsById(request.branchId())) {
			throw new ApiException(ResponseCode.OBJECT_NOT_EXISTED);
		}

		Department department = new Department();
		department.setName(name);
		department.setDepartmentType(departmentType);
		department.setDescription(request.description());
		department.setBranchId(request.branchId());

		department = departmentRepository.save(department);

		return new CreateDepartmentResult(
				department.getId(),
				department.getName(),
				department.getDepartmentType(),
				department.getDescription(),
				department.getBranchId()
		);
	}

	@Transactional(readOnly = true)
	public List<GetDepartmentsResult> getDepartments(Long branchId) {
		List<Department> departments = branchId == null
				? departmentRepository.findAll()
				: departmentRepository.findByBranchId(branchId);

		return departments.stream()
				.map(department -> new GetDepartmentsResult(
						department.getId(),
						department.getName(),
						department.getBranchId(),
						department.getDescription()
				))
				.toList();
	}

	@Transactional(readOnly = true)
	public GetDepartmentResult getDepartment(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		return new GetDepartmentResult(
				department.getId(),
				department.getName(),
				department.getDepartmentType(),
				department.getDescription(),
				department.getBranchId()
		);
	}

	@Transactional
	public UpdateDepartmentResult updateDepartment(
			Long id,
			UpdateDepartmentRequest request) {

		if (request.name() == null
				&& request.departmentType() == null
				&& request.description() == null
				&& request.branchId() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		if (request.name() != null) {
			if (request.name().isBlank() || request.name().trim().length() > 150) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}
			department.setName(request.name().trim());
		}

		if (request.departmentType() != null) {
			if (request.departmentType().isBlank()
					|| request.departmentType().trim().length() > 50) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}
			department.setDepartmentType(request.departmentType().trim());
		}

		if (request.description() != null) {
			department.setDescription(request.description());
		}

		if (request.branchId() != null) {
			if (!branchRepository.existsById(request.branchId())) {
				throw new ApiException(ResponseCode.OBJECT_NOT_EXISTED);
			}
			department.setBranchId(request.branchId());
		}

		department = departmentRepository.save(department);

		return new UpdateDepartmentResult(
				department.getId(),
				department.getName(),
				department.getDepartmentType(),
				department.getDescription(),
				department.getBranchId(),
				Instant.now()
		);
	}

	@Transactional
	public void deleteDepartment(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		try {
			departmentRepository.delete(department);
			departmentRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}
	}

}
