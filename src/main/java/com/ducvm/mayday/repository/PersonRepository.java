package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}
