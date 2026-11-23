package com.ducvm.mayday.service;

import com.ducvm.mayday.dataset.CreateRoomRequest;
import com.ducvm.mayday.dataset.CreateRoomResult;
import com.ducvm.mayday.entity.Department;
import com.ducvm.mayday.entity.Room;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.repository.DepartmentRepository;
import com.ducvm.mayday.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Transactional
	public CreateRoomResult createRoom(CreateRoomRequest request) {
		if (request.name() == null || request.name().isBlank()
				|| request.departmentId() == null) {
			throw new ApiException(ResponseCode.PARAMETER_NOT_ENOUGH);
		}

		String name = request.name().trim();

		if (name.length() > 150) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		Department department = departmentRepository.findById(request.departmentId())
				.orElseThrow(() -> new ApiException(ResponseCode.OBJECT_NOT_EXISTED));

		Room room = new Room();
		room.setName(name);
		room.setBranchId(department.getBranchId());

		room = roomRepository.save(room);

		return new CreateRoomResult(
				room.getId(),
				room.getName(),
				department.getId()
		);
	}
}
