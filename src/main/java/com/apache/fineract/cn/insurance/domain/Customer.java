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

import com.apache.fineract.cn.insurance.domain.enumeration.MaritalStatus;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotNull
    @Column(name = "s_sn", nullable = false)
    private String sSN;

    @NotNull
    @Column(name = "is_primary_policy_holder", nullable = false)
    private Boolean isPrimaryPolicyHolder;

    @NotNull
    @Column(name = "gender", nullable = false)
    private String gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false)
    private MaritalStatus maritalStatus;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private LocalDate timestamp;

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PolicyHolder> policyHolders = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Claim> claims = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Customer dob(LocalDate dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Customer telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getsSN() {
        return sSN;
    }

    public Customer sSN(String sSN) {
        this.sSN = sSN;
        return this;
    }

    public void setsSN(String sSN) {
        this.sSN = sSN;
    }

    public Boolean isIsPrimaryPolicyHolder() {
        return isPrimaryPolicyHolder;
    }

    public Customer isPrimaryPolicyHolder(Boolean isPrimaryPolicyHolder) {
        this.isPrimaryPolicyHolder = isPrimaryPolicyHolder;
        return this;
    }

    public void setIsPrimaryPolicyHolder(Boolean isPrimaryPolicyHolder) {
        this.isPrimaryPolicyHolder = isPrimaryPolicyHolder;
    }

    public String getGender() {
        return gender;
    }

    public Customer gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public Customer maritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Customer isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public Customer timestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public Customer addresses(Set<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Customer addAddress(Address address) {
        this.addresses.add(address);
        address.setCustomer(this);
        return this;
    }

    public Customer removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCustomer(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<PolicyHolder> getPolicyHolders() {
        return policyHolders;
    }

    public Customer policyHolders(Set<PolicyHolder> policyHolders) {
        this.policyHolders = policyHolders;
        return this;
    }

    public Customer addPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolders.add(policyHolder);
        policyHolder.setCustomer(this);
        return this;
    }

    public Customer removePolicyHolder(PolicyHolder policyHolder) {
        this.policyHolders.remove(policyHolder);
        policyHolder.setCustomer(null);
        return this;
    }

    public void setPolicyHolders(Set<PolicyHolder> policyHolders) {
        this.policyHolders = policyHolders;
    }

    public Set<Claim> getClaims() {
        return claims;
    }

    public Customer claims(Set<Claim> claims) {
        this.claims = claims;
        return this;
    }

    public Customer addClaim(Claim claim) {
        this.claims.add(claim);
        claim.setCustomer(this);
        return this;
    }

    public Customer removeClaim(Claim claim) {
        this.claims.remove(claim);
        claim.setCustomer(null);
        return this;
    }

    public void setClaims(Set<Claim> claims) {
        this.claims = claims;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", dob='" + getDob() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", sSN='" + getsSN() + "'" +
            ", isPrimaryPolicyHolder='" + isIsPrimaryPolicyHolder() + "'" +
            ", gender='" + getGender() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
