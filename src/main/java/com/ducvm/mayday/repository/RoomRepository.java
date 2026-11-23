package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
