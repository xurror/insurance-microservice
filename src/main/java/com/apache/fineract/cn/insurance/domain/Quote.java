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
 * A Quote.
 */
@Entity
@Table(name = "quote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ref", nullable = false)
    private String ref;

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
    @Column(name = "i_pt", nullable = false)
    private String iPT;

    @OneToMany(mappedBy = "quote")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "quote")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PolicyHolder> policyHolders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public Quote ref(String ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Quote startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Quote endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Float getPremium() {
        return premium;
    }

    public Quote premium(Float premium) {
        this.premium = premium;
        return this;
    }

    public void setPremium(Float premium) {
        this.premium = premium;
    }

    public String getiPT() {
        return iPT;
    }

    public Quote iPT(String iPT) {
        this.iPT = iPT;
        return this;
    }

    public void setiPT(String iPT) {
        this.iPT = iPT;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Quote products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Quote addProduct(Product product) {
        this.products.add(product);
        product.setQuote(this);
        return this;
    }

    public Quote removeProduct(Product product) {
        this.products.remove(product);
        product.setQuote(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<PolicyHolder> getPolicyHolders() {
        return policyHolders;
    }

    public Quote policyHolders(Set<PolicyHolder> policyHolders) {
        this.policyHolders = policyHolders;
        return this;
    }

    public Quote addPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolders.add(policyHolder);
        policyHolder.setQuote(this);
        return this;
    }

    public Quote removePolicyHolder(PolicyHolder policyHolder) {
        this.policyHolders.remove(policyHolder);
        policyHolder.setQuote(null);
        return this;
    }

    public void setPolicyHolders(Set<PolicyHolder> policyHolders) {
        this.policyHolders = policyHolders;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quote)) {
            return false;
        }
        return id != null && id.equals(((Quote) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Quote{" +
            "id=" + getId() +
            ", ref='" + getRef() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", premium=" + getPremium() +
            ", iPT='" + getiPT() + "'" +
            "}";
    }
}
