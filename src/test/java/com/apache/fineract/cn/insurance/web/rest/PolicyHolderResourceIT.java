package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.InsuranceModuleApp;
import com.apache.fineract.cn.insurance.domain.PolicyHolder;
import com.apache.fineract.cn.insurance.repository.PolicyHolderRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PolicyHolderResource} REST controller.
 */
@SpringBootTest(classes = InsuranceModuleApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PolicyHolderResourceIT {

    @Autowired
    private PolicyHolderRepository policyHolderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPolicyHolderMockMvc;

    private PolicyHolder policyHolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicyHolder createEntity(EntityManager em) {
        PolicyHolder policyHolder = new PolicyHolder();
        return policyHolder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicyHolder createUpdatedEntity(EntityManager em) {
        PolicyHolder policyHolder = new PolicyHolder();
        return policyHolder;
    }

    @BeforeEach
    public void initTest() {
        policyHolder = createEntity(em);
    }

    @Test
    @Transactional
    public void createPolicyHolder() throws Exception {
        int databaseSizeBeforeCreate = policyHolderRepository.findAll().size();

        // Create the PolicyHolder
        restPolicyHolderMockMvc.perform(post("/api/policy-holders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policyHolder)))
            .andExpect(status().isCreated());

        // Validate the PolicyHolder in the database
        List<PolicyHolder> policyHolderList = policyHolderRepository.findAll();
        assertThat(policyHolderList).hasSize(databaseSizeBeforeCreate + 1);
        PolicyHolder testPolicyHolder = policyHolderList.get(policyHolderList.size() - 1);
    }

    @Test
    @Transactional
    public void createPolicyHolderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = policyHolderRepository.findAll().size();

        // Create the PolicyHolder with an existing ID
        policyHolder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolicyHolderMockMvc.perform(post("/api/policy-holders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policyHolder)))
            .andExpect(status().isBadRequest());

        // Validate the PolicyHolder in the database
        List<PolicyHolder> policyHolderList = policyHolderRepository.findAll();
        assertThat(policyHolderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPolicyHolders() throws Exception {
        // Initialize the database
        policyHolderRepository.saveAndFlush(policyHolder);

        // Get all the policyHolderList
        restPolicyHolderMockMvc.perform(get("/api/policy-holders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policyHolder.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getPolicyHolder() throws Exception {
        // Initialize the database
        policyHolderRepository.saveAndFlush(policyHolder);

        // Get the policyHolder
        restPolicyHolderMockMvc.perform(get("/api/policy-holders/{id}", policyHolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(policyHolder.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPolicyHolder() throws Exception {
        // Get the policyHolder
        restPolicyHolderMockMvc.perform(get("/api/policy-holders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolicyHolder() throws Exception {
        // Initialize the database
        policyHolderRepository.saveAndFlush(policyHolder);

        int databaseSizeBeforeUpdate = policyHolderRepository.findAll().size();

        // Update the policyHolder
        PolicyHolder updatedPolicyHolder = policyHolderRepository.findById(policyHolder.getId()).get();
        // Disconnect from session so that the updates on updatedPolicyHolder are not directly saved in db
        em.detach(updatedPolicyHolder);

        restPolicyHolderMockMvc.perform(put("/api/policy-holders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPolicyHolder)))
            .andExpect(status().isOk());

        // Validate the PolicyHolder in the database
        List<PolicyHolder> policyHolderList = policyHolderRepository.findAll();
        assertThat(policyHolderList).hasSize(databaseSizeBeforeUpdate);
        PolicyHolder testPolicyHolder = policyHolderList.get(policyHolderList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingPolicyHolder() throws Exception {
        int databaseSizeBeforeUpdate = policyHolderRepository.findAll().size();

        // Create the PolicyHolder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyHolderMockMvc.perform(put("/api/policy-holders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(policyHolder)))
            .andExpect(status().isBadRequest());

        // Validate the PolicyHolder in the database
        List<PolicyHolder> policyHolderList = policyHolderRepository.findAll();
        assertThat(policyHolderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePolicyHolder() throws Exception {
        // Initialize the database
        policyHolderRepository.saveAndFlush(policyHolder);

        int databaseSizeBeforeDelete = policyHolderRepository.findAll().size();

        // Delete the policyHolder
        restPolicyHolderMockMvc.perform(delete("/api/policy-holders/{id}", policyHolder.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PolicyHolder> policyHolderList = policyHolderRepository.findAll();
        assertThat(policyHolderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
