package com.ducvm.mayday.specification;

import com.ducvm.mayday.entity.Patient;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientSpecification {

	public static Specification<Patient> search(
			String keyword,
			String gender,
			LocalDate startDate,
			LocalDate endDate) {

		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (keyword != null && !keyword.isBlank()) {
				String pattern = "%" + keyword.trim().toLowerCase() + "%";

				predicates.add(cb.or(
						cb.like(cb.lower(root.get("fullName")), pattern),
						cb.like(root.get("phone"), pattern)
				));
			}

			if (gender != null) {
				predicates.add(cb.equal(root.get("sex"), gender));
			}

			if (startDate != null && endDate != null) {
				predicates.add(cb.greaterThanOrEqualTo(
						root.<LocalDate>get("dateOfBirth"), startDate));

				predicates.add(cb.lessThan(
						root.<LocalDate>get("dateOfBirth"), endDate));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
