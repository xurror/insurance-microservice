package com.apache.fineract.cn.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

import com.apache.fineract.cn.insurance.domain.enumeration.ClaimStatus;

import com.apache.fineract.cn.insurance.domain.enumeration.ClaimOutcomes;

/**
 * A Claim.
 */
@Entity
@Table(name = "claim")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Claim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "amount", nullable = false)
    private String amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status")
    private ClaimStatus claimStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_outcomes")
    private ClaimOutcomes claimOutcomes;

    @ManyToOne
    @JsonIgnoreProperties("claims")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Claim date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public Claim amount(String amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public Claim claimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
        return this;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public ClaimOutcomes getClaimOutcomes() {
        return claimOutcomes;
    }

    public Claim claimOutcomes(ClaimOutcomes claimOutcomes) {
        this.claimOutcomes = claimOutcomes;
        return this;
    }

    public void setClaimOutcomes(ClaimOutcomes claimOutcomes) {
        this.claimOutcomes = claimOutcomes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Claim customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Claim)) {
            return false;
        }
        return id != null && id.equals(((Claim) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Claim{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", amount='" + getAmount() + "'" +
            ", claimStatus='" + getClaimStatus() + "'" +
            ", claimOutcomes='" + getClaimOutcomes() + "'" +
            "}";
    }
}
