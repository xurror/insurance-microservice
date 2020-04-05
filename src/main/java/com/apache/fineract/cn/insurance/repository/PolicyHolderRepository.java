package com.apache.fineract.cn.insurance.repository;

import com.apache.fineract.cn.insurance.domain.PolicyHolder;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PolicyHolder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PolicyHolderRepository extends JpaRepository<PolicyHolder, Long> {
}
