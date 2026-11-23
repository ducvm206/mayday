package com.ducvm.mayday.service;

import com.ducvm.mayday.dataset.*;
import com.ducvm.mayday.entity.Branch;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.repository.BranchRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

	@Autowired
	private BranchRepository branchRepository;

	@Transactional
	public CreateBranchResult createBranch(CreateBranchRequest request) {

		if (request.name() == null || request.name().isBlank()
				|| request.address() == null || request.address().isBlank()
				|| request.phone() == null || request.phone().isBlank()) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		String name = request.name().trim();
		String address = request.address().trim();
		String phone = request.phone().trim();

		if (name.length() > 150 || phone.length() > 20) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		Branch branch = new Branch();
		branch.setName(name);
		branch.setAddress(address);
		branch.setHotline(phone);

		branch = branchRepository.save(branch);

		return new CreateBranchResult(
				branch.getId(),
				branch.getName(),
				branch.getAddress(),
				branch.getHotline()
		);
	}

	@Transactional(readOnly = true)
	public List<GetBranchesResult> getBranches() {
		return branchRepository.findAll().stream()
				.map(branch -> new GetBranchesResult(
						branch.getId(),
						branch.getName(),
						branch.getAddress(),
						branch.getHotline(),
						branch.getStatus()
				))
				.toList();
	}

	@Transactional(readOnly = true)
	public GetBranchResult getBranch(Long id) {
		Branch branch = branchRepository.findById(id)
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		return new GetBranchResult(
				branch.getId(),
				branch.getName(),
				branch.getAddress(),
				branch.getHotline()
		);
	}

	@Transactional
	public UpdateBranchResult updateBranch(Long id, UpdateBranchRequest request) {
		if (request.name() == null
				&& request.address() == null
				&& request.phone() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		Branch branch = branchRepository.findById(id)
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		if (request.name() != null) {
			if (request.name().isBlank()) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			String name = request.name().trim();

			if (name.length() > 150) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			if (branchRepository.existsByNameAndIdNot(name, id)) {
				throw new ApiException(ResponseCode.USER_EXISTED);
			}

			branch.setName(name);
		}

		if (request.address() != null) {
			if (request.address().isBlank()) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			branch.setAddress(request.address().trim());
		}

		if (request.phone() != null) {
			if (request.phone().isBlank() || request.phone().trim().length() > 20) {
				throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
			}

			branch.setHotline(request.phone().trim());
		}

		branch = branchRepository.save(branch);

		return new UpdateBranchResult(
				branch.getId(),
				branch.getName(),
				branch.getAddress(),
				branch.getHotline(),
				Instant.now()
		);
	}

	@Transactional
	public void deleteBranch(Long id) {
		Branch branch = branchRepository.findById(id)
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		try {
			branchRepository.delete(branch);
			branchRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new ApiException(ResponseCode.NOT_ACCESS);
		}
	}
}
