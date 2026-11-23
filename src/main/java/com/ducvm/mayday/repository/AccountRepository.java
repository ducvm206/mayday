package com.ducvm.mayday.repository;

import com.ducvm.mayday.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByUsername(String username);

	Optional<Account> findByPersonId(Long personId);

	Optional<Account> findByUsername(String username);

	Page<Account> findByRole_Id(Long roleId, Pageable pageable);

	long countByRole_Id(Long roleId);
}
