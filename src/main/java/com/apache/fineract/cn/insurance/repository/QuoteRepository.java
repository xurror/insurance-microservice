package com.apache.fineract.cn.insurance.repository;

import com.apache.fineract.cn.insurance.domain.Quote;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Quote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
