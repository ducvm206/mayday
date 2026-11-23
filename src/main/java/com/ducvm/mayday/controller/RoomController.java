package com.ducvm.mayday.controller;

import com.ducvm.mayday.dataset.CreateRoomRequest;
import com.ducvm.mayday.dataset.CreateRoomResult;
import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.enums.Roles;
import com.ducvm.mayday.exceptions.ApiException;
import com.ducvm.mayday.security.JwtService;
import com.ducvm.mayday.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/api/rooms")
public class RoomController extends BaseController {

	@Autowired
	private RoomService roomService;

	@Autowired
	private JwtService jwtService;

	@PostMapping
	public ResponseEntity<?> createRoom(
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@RequestBody CreateRoomRequest request) {

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return respond(HttpStatus.UNAUTHORIZED, ResponseCode.TOKEN_INVALID,
					ResponseCode.TOKEN_INVALID.getMessageVi(), null);
		}

		try {
			String token = authorization.substring(7);

			if (!Objects.equals(jwtService.extractRoleId(token), Roles.ADMIN.getId())) {
				return respond(HttpStatus.FORBIDDEN, ResponseCode.NOT_ACCESS,
						ResponseCode.NOT_ACCESS.getMessageVi(), null);
			}

			CreateRoomResult result = roomService.createRoom(request);

			return respond(HttpStatus.CREATED, ResponseCode.SUCCESS,
					"Tạo phòng khám thành công", result);

		} catch (ApiException e) {
			return handleApiException(e);
		}
	}
}
