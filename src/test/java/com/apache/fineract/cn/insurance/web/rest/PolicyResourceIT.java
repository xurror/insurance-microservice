package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.InsuranceModuleApp;
import com.apache.fineract.cn.insurance.domain.Policy;
import com.apache.fineract.cn.insurance.repository.PolicyRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PolicyResource} REST controller.
 */
@SpringBootTest(classes = InsuranceModuleApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PolicyResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_PREMIUM = 1F;
    private static final Float UPDATED_PREMIUM = 2F;

    private static final Float DEFAULT_DEDUCTIBLE = 1F;
    private static final Float UPDATED_DEDUCTIBLE = 2F;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LocalDate DEFAULT_TIMESTAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPolicyMockMvc;

    private Policy policy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Policy createEntity(EntityManager em) {
        Policy policy = new Policy()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .premium(DEFAULT_PREMIUM)
            .deductible(DEFAULT_DEDUCTIBLE)
            .isActive(DEFAULT_IS_ACTIVE)
            .timestamp(DEFAULT_TIMESTAMP);
        return policy;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Policy createUpdatedEntity(EntityManager em) {
        Policy policy = new Policy()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .premium(UPDATED_PREMIUM)
            .deductible(UPDATED_DEDUCTIBLE)
            .isActive(UPDATED_IS_ACTIVE)
            .timestamp(UPDATED_TIMESTAMP);
        return policy;
    }

    @BeforeEach
    public void initTest() {
        policy = createEntity(em);
    }

    @Test
    @Transactional
    public void createPolicy() throws Exception {
        int databaseSizeBeforeCreate = policyRepository.findAll().size();

        // Create the Policy
        restPolicyMockMvc.perform(post("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isCreated());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeCreate + 1);
        Policy testPolicy = policyList.get(policyList.size() - 1);
        assertThat(testPolicy.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPolicy.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPolicy.getPremium()).isEqualTo(DEFAULT_PREMIUM);
        assertThat(testPolicy.getDeductible()).isEqualTo(DEFAULT_DEDUCTIBLE);
        assertThat(testPolicy.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPolicy.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createPolicyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = policyRepository.findAll().size();

        // Create the Policy with an existing ID
        policy.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolicyMockMvc.perform(post("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isBadRequest());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setStartDate(null);

        // Create the Policy, which fails.

        restPolicyMockMvc.perform(post("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setEndDate(null);

        // Create the Policy, which fails.

        restPolicyMockMvc.perform(post("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPremiumIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setPremium(null);

        // Create the Policy, which fails.

        restPolicyMockMvc.perform(post("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeductibleIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setDeductible(null);

        // Create the Policy, which fails.

        restPolicyMockMvc.perform(post("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setIsActive(null);

        // Create the Policy, which fails.

        restPolicyMockMvc.perform(post("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = policyRepository.findAll().size();
        // set the field null
        policy.setTimestamp(null);

        // Create the Policy, which fails.

        restPolicyMockMvc.perform(post("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isBadRequest());

        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPolicies() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policyList
        restPolicyMockMvc.perform(get("/api/policies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policy.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].premium").value(hasItem(DEFAULT_PREMIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].deductible").value(hasItem(DEFAULT_DEDUCTIBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }
    
    @Test
    @Transactional
    public void getPolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get the policy
        restPolicyMockMvc.perform(get("/api/policies/{id}", policy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(policy.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.premium").value(DEFAULT_PREMIUM.doubleValue()))
            .andExpect(jsonPath("$.deductible").value(DEFAULT_DEDUCTIBLE.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolicy() throws Exception {
        // Get the policy
        restPolicyMockMvc.perform(get("/api/policies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        int databaseSizeBeforeUpdate = policyRepository.findAll().size();

        // Update the policy
        Policy updatedPolicy = policyRepository.findById(policy.getId()).get();
        // Disconnect from session so that the updates on updatedPolicy are not directly saved in db
        em.detach(updatedPolicy);
        updatedPolicy
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .premium(UPDATED_PREMIUM)
            .deductible(UPDATED_DEDUCTIBLE)
            .isActive(UPDATED_IS_ACTIVE)
            .timestamp(UPDATED_TIMESTAMP);

        restPolicyMockMvc.perform(put("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPolicy)))
            .andExpect(status().isOk());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
        Policy testPolicy = policyList.get(policyList.size() - 1);
        assertThat(testPolicy.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPolicy.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPolicy.getPremium()).isEqualTo(UPDATED_PREMIUM);
        assertThat(testPolicy.getDeductible()).isEqualTo(UPDATED_DEDUCTIBLE);
        assertThat(testPolicy.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPolicy.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingPolicy() throws Exception {
        int databaseSizeBeforeUpdate = policyRepository.findAll().size();

        // Create the Policy

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyMockMvc.perform(put("/api/policies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policy)))
            .andExpect(status().isBadRequest());

        // Validate the Policy in the database
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        int databaseSizeBeforeDelete = policyRepository.findAll().size();

        // Delete the policy
        restPolicyMockMvc.perform(delete("/api/policies/{id}", policy.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Policy> policyList = policyRepository.findAll();
        assertThat(policyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
