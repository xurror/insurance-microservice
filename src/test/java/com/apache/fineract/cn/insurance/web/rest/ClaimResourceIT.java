package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.InsuranceModuleApp;
import com.apache.fineract.cn.insurance.domain.Claim;
import com.apache.fineract.cn.insurance.repository.ClaimRepository;

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

import com.apache.fineract.cn.insurance.domain.enumeration.ClaimStatus;
import com.apache.fineract.cn.insurance.domain.enumeration.ClaimOutcomes;
/**
 * Integration tests for the {@link ClaimResource} REST controller.
 */
@SpringBootTest(classes = InsuranceModuleApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ClaimResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT = "BBBBBBBBBB";

    private static final ClaimStatus DEFAULT_CLAIM_STATUS = ClaimStatus.UnderReview;
    private static final ClaimStatus UPDATED_CLAIM_STATUS = ClaimStatus.Claimed;

    private static final ClaimOutcomes DEFAULT_CLAIM_OUTCOMES = ClaimOutcomes.Settled;
    private static final ClaimOutcomes UPDATED_CLAIM_OUTCOMES = ClaimOutcomes.Rejected;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClaimMockMvc;

    private Claim claim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Claim createEntity(EntityManager em) {
        Claim claim = new Claim()
            .date(DEFAULT_DATE)
            .amount(DEFAULT_AMOUNT)
            .claimStatus(DEFAULT_CLAIM_STATUS)
            .claimOutcomes(DEFAULT_CLAIM_OUTCOMES);
        return claim;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Claim createUpdatedEntity(EntityManager em) {
        Claim claim = new Claim()
            .date(UPDATED_DATE)
            .amount(UPDATED_AMOUNT)
            .claimStatus(UPDATED_CLAIM_STATUS)
            .claimOutcomes(UPDATED_CLAIM_OUTCOMES);
        return claim;
    }

    @BeforeEach
    public void initTest() {
        claim = createEntity(em);
    }

    @Test
    @Transactional
    public void createClaim() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();

        // Create the Claim
        restClaimMockMvc.perform(post("/api/claims")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isCreated());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate + 1);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testClaim.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testClaim.getClaimStatus()).isEqualTo(DEFAULT_CLAIM_STATUS);
        assertThat(testClaim.getClaimOutcomes()).isEqualTo(DEFAULT_CLAIM_OUTCOMES);
    }

    @Test
    @Transactional
    public void createClaimWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();

        // Create the Claim with an existing ID
        claim.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClaimMockMvc.perform(post("/api/claims")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setDate(null);

        // Create the Claim, which fails.

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setAmount(null);

        // Create the Claim, which fails.

        restClaimMockMvc.perform(post("/api/claims")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClaims() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        // Get all the claimList
        restClaimMockMvc.perform(get("/api/claims?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].claimStatus").value(hasItem(DEFAULT_CLAIM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].claimOutcomes").value(hasItem(DEFAULT_CLAIM_OUTCOMES.toString())));
    }
    
    @Test
    @Transactional
    public void getClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        // Get the claim
        restClaimMockMvc.perform(get("/api/claims/{id}", claim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(claim.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.claimStatus").value(DEFAULT_CLAIM_STATUS.toString()))
            .andExpect(jsonPath("$.claimOutcomes").value(DEFAULT_CLAIM_OUTCOMES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClaim() throws Exception {
        // Get the claim
        restClaimMockMvc.perform(get("/api/claims/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Update the claim
        Claim updatedClaim = claimRepository.findById(claim.getId()).get();
        // Disconnect from session so that the updates on updatedClaim are not directly saved in db
        em.detach(updatedClaim);
        updatedClaim
            .date(UPDATED_DATE)
            .amount(UPDATED_AMOUNT)
            .claimStatus(UPDATED_CLAIM_STATUS)
            .claimOutcomes(UPDATED_CLAIM_OUTCOMES);

        restClaimMockMvc.perform(put("/api/claims")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedClaim)))
            .andExpect(status().isOk());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testClaim.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testClaim.getClaimStatus()).isEqualTo(UPDATED_CLAIM_STATUS);
        assertThat(testClaim.getClaimOutcomes()).isEqualTo(UPDATED_CLAIM_OUTCOMES);
    }

    @Test
    @Transactional
    public void updateNonExistingClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Create the Claim

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClaimMockMvc.perform(put("/api/claims")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        int databaseSizeBeforeDelete = claimRepository.findAll().size();

        // Delete the claim
        restClaimMockMvc.perform(delete("/api/claims/{id}", claim.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
