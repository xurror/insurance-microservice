package com.apache.fineract.cn.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A PolicyHolder.
 */
@Entity
@Table(name = "policy_holder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PolicyHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "policyHolder")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PaymentSchedule> paymentSchedules = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("policyHolders")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("policyHolders")
    private Quote quote;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PaymentSchedule> getPaymentSchedules() {
        return paymentSchedules;
    }

    public PolicyHolder paymentSchedules(Set<PaymentSchedule> paymentSchedules) {
        this.paymentSchedules = paymentSchedules;
        return this;
    }

    public PolicyHolder addPaymentSchedule(PaymentSchedule paymentSchedule) {
        this.paymentSchedules.add(paymentSchedule);
        paymentSchedule.setPolicyHolder(this);
        return this;
    }

    public PolicyHolder removePaymentSchedule(PaymentSchedule paymentSchedule) {
        this.paymentSchedules.remove(paymentSchedule);
        paymentSchedule.setPolicyHolder(null);
        return this;
    }

    public void setPaymentSchedules(Set<PaymentSchedule> paymentSchedules) {
        this.paymentSchedules = paymentSchedules;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PolicyHolder customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Quote getQuote() {
        return quote;
    }

    public PolicyHolder quote(Quote quote) {
        this.quote = quote;
        return this;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PolicyHolder)) {
            return false;
        }
        return id != null && id.equals(((PolicyHolder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PolicyHolder{" +
            "id=" + getId() +
            "}";
    }
}
