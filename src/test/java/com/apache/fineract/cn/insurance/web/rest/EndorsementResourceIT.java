package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.InsuranceModuleApp;
import com.apache.fineract.cn.insurance.domain.Endorsement;
import com.apache.fineract.cn.insurance.repository.EndorsementRepository;

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
 * Integration tests for the {@link EndorsementResource} REST controller.
 */
@SpringBootTest(classes = InsuranceModuleApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class EndorsementResourceIT {

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private EndorsementRepository endorsementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEndorsementMockMvc;

    private Endorsement endorsement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endorsement createEntity(EntityManager em) {
        Endorsement endorsement = new Endorsement()
            .ref(DEFAULT_REF)
            .description(DEFAULT_DESCRIPTION);
        return endorsement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endorsement createUpdatedEntity(EntityManager em) {
        Endorsement endorsement = new Endorsement()
            .ref(UPDATED_REF)
            .description(UPDATED_DESCRIPTION);
        return endorsement;
    }

    @BeforeEach
    public void initTest() {
        endorsement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEndorsement() throws Exception {
        int databaseSizeBeforeCreate = endorsementRepository.findAll().size();

        // Create the Endorsement
        restEndorsementMockMvc.perform(post("/api/endorsements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(endorsement)))
            .andExpect(status().isCreated());

        // Validate the Endorsement in the database
        List<Endorsement> endorsementList = endorsementRepository.findAll();
        assertThat(endorsementList).hasSize(databaseSizeBeforeCreate + 1);
        Endorsement testEndorsement = endorsementList.get(endorsementList.size() - 1);
        assertThat(testEndorsement.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testEndorsement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createEndorsementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = endorsementRepository.findAll().size();

        // Create the Endorsement with an existing ID
        endorsement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEndorsementMockMvc.perform(post("/api/endorsements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(endorsement)))
            .andExpect(status().isBadRequest());

        // Validate the Endorsement in the database
        List<Endorsement> endorsementList = endorsementRepository.findAll();
        assertThat(endorsementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRefIsRequired() throws Exception {
        int databaseSizeBeforeTest = endorsementRepository.findAll().size();
        // set the field null
        endorsement.setRef(null);

        // Create the Endorsement, which fails.

        restEndorsementMockMvc.perform(post("/api/endorsements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(endorsement)))
            .andExpect(status().isBadRequest());

        List<Endorsement> endorsementList = endorsementRepository.findAll();
        assertThat(endorsementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = endorsementRepository.findAll().size();
        // set the field null
        endorsement.setDescription(null);

        // Create the Endorsement, which fails.

        restEndorsementMockMvc.perform(post("/api/endorsements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(endorsement)))
            .andExpect(status().isBadRequest());

        List<Endorsement> endorsementList = endorsementRepository.findAll();
        assertThat(endorsementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEndorsements() throws Exception {
        // Initialize the database
        endorsementRepository.saveAndFlush(endorsement);

        // Get all the endorsementList
        restEndorsementMockMvc.perform(get("/api/endorsements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endorsement.getId().intValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getEndorsement() throws Exception {
        // Initialize the database
        endorsementRepository.saveAndFlush(endorsement);

        // Get the endorsement
        restEndorsementMockMvc.perform(get("/api/endorsements/{id}", endorsement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(endorsement.getId().intValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingEndorsement() throws Exception {
        // Get the endorsement
        restEndorsementMockMvc.perform(get("/api/endorsements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEndorsement() throws Exception {
        // Initialize the database
        endorsementRepository.saveAndFlush(endorsement);

        int databaseSizeBeforeUpdate = endorsementRepository.findAll().size();

        // Update the endorsement
        Endorsement updatedEndorsement = endorsementRepository.findById(endorsement.getId()).get();
        // Disconnect from session so that the updates on updatedEndorsement are not directly saved in db
        em.detach(updatedEndorsement);
        updatedEndorsement
            .ref(UPDATED_REF)
            .description(UPDATED_DESCRIPTION);

        restEndorsementMockMvc.perform(put("/api/endorsements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEndorsement)))
            .andExpect(status().isOk());

        // Validate the Endorsement in the database
        List<Endorsement> endorsementList = endorsementRepository.findAll();
        assertThat(endorsementList).hasSize(databaseSizeBeforeUpdate);
        Endorsement testEndorsement = endorsementList.get(endorsementList.size() - 1);
        assertThat(testEndorsement.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testEndorsement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingEndorsement() throws Exception {
        int databaseSizeBeforeUpdate = endorsementRepository.findAll().size();

        // Create the Endorsement

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEndorsementMockMvc.perform(put("/api/endorsements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(endorsement)))
            .andExpect(status().isBadRequest());

        // Validate the Endorsement in the database
        List<Endorsement> endorsementList = endorsementRepository.findAll();
        assertThat(endorsementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEndorsement() throws Exception {
        // Initialize the database
        endorsementRepository.saveAndFlush(endorsement);

        int databaseSizeBeforeDelete = endorsementRepository.findAll().size();

        // Delete the endorsement
        restEndorsementMockMvc.perform(delete("/api/endorsements/{id}", endorsement.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Endorsement> endorsementList = endorsementRepository.findAll();
        assertThat(endorsementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
