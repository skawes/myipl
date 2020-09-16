package com.myipl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myipl.domain.entity.PredictionEventAudit;

public interface EventAuditRepository extends JpaRepository<PredictionEventAudit, Long> {

}
