package com.apache.fineract.cn.insurance.repository;

import com.apache.fineract.cn.insurance.domain.Endorsement;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Endorsement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EndorsementRepository extends JpaRepository<Endorsement, Long> {
}
