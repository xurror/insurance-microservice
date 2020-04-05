package com.apache.fineract.cn.insurance.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Policy.
 */
@Entity
@Table(name = "policy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Policy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "premium", nullable = false)
    private Float premium;

    @NotNull
    @Column(name = "deductible", nullable = false)
    private Float deductible;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private LocalDate timestamp;

    @OneToOne
    @JoinColumn(unique = true)
    private Quote quoteId;

    @OneToMany(mappedBy = "policy")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MTA> mTAS = new HashSet<>();

    @OneToMany(mappedBy = "policy")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Endorsement> endorsements = new HashSet<>();

    @OneToMany(mappedBy = "policy")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Document> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Policy startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Policy endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Float getPremium() {
        return premium;
    }

    public Policy premium(Float premium) {
        this.premium = premium;
        return this;
    }

    public void setPremium(Float premium) {
        this.premium = premium;
    }

    public Float getDeductible() {
        return deductible;
    }

    public Policy deductible(Float deductible) {
        this.deductible = deductible;
        return this;
    }

    public void setDeductible(Float deductible) {
        this.deductible = deductible;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Policy isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public Policy timestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public Quote getQuoteId() {
        return quoteId;
    }

    public Policy quoteId(Quote quote) {
        this.quoteId = quote;
        return this;
    }

    public void setQuoteId(Quote quote) {
        this.quoteId = quote;
    }

    public Set<MTA> getMTAS() {
        return mTAS;
    }

    public Policy mTAS(Set<MTA> mTAS) {
        this.mTAS = mTAS;
        return this;
    }

    public Policy addMTA(MTA mTA) {
        this.mTAS.add(mTA);
        mTA.setPolicy(this);
        return this;
    }

    public Policy removeMTA(MTA mTA) {
        this.mTAS.remove(mTA);
        mTA.setPolicy(null);
        return this;
    }

    public void setMTAS(Set<MTA> mTAS) {
        this.mTAS = mTAS;
    }

    public Set<Endorsement> getEndorsements() {
        return endorsements;
    }

    public Policy endorsements(Set<Endorsement> endorsements) {
        this.endorsements = endorsements;
        return this;
    }

    public Policy addEndorsement(Endorsement endorsement) {
        this.endorsements.add(endorsement);
        endorsement.setPolicy(this);
        return this;
    }

    public Policy removeEndorsement(Endorsement endorsement) {
        this.endorsements.remove(endorsement);
        endorsement.setPolicy(null);
        return this;
    }

    public void setEndorsements(Set<Endorsement> endorsements) {
        this.endorsements = endorsements;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public Policy documents(Set<Document> documents) {
        this.documents = documents;
        return this;
    }

    public Policy addDocument(Document document) {
        this.documents.add(document);
        document.setPolicy(this);
        return this;
    }

    public Policy removeDocument(Document document) {
        this.documents.remove(document);
        document.setPolicy(null);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Policy)) {
            return false;
        }
        return id != null && id.equals(((Policy) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Policy{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", premium=" + getPremium() +
            ", deductible=" + getDeductible() +
            ", isActive='" + isIsActive() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
