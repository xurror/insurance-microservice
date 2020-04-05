package com.apache.fineract.cn.insurance.repository;

import com.apache.fineract.cn.insurance.domain.MTA;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MTA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MTARepository extends JpaRepository<MTA, Long> {
}
