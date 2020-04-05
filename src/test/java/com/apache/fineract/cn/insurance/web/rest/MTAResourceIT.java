package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.InsuranceModuleApp;
import com.apache.fineract.cn.insurance.domain.MTA;
import com.apache.fineract.cn.insurance.repository.MTARepository;

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
 * Integration tests for the {@link MTAResource} REST controller.
 */
@SpringBootTest(classes = InsuranceModuleApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MTAResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MTARepository mTARepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMTAMockMvc;

    private MTA mTA;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MTA createEntity(EntityManager em) {
        MTA mTA = new MTA()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return mTA;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MTA createUpdatedEntity(EntityManager em) {
        MTA mTA = new MTA()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return mTA;
    }

    @BeforeEach
    public void initTest() {
        mTA = createEntity(em);
    }

    @Test
    @Transactional
    public void createMTA() throws Exception {
        int databaseSizeBeforeCreate = mTARepository.findAll().size();

        // Create the MTA
        restMTAMockMvc.perform(post("/api/mtas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mTA)))
            .andExpect(status().isCreated());

        // Validate the MTA in the database
        List<MTA> mTAList = mTARepository.findAll();
        assertThat(mTAList).hasSize(databaseSizeBeforeCreate + 1);
        MTA testMTA = mTAList.get(mTAList.size() - 1);
        assertThat(testMTA.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMTA.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createMTAWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mTARepository.findAll().size();

        // Create the MTA with an existing ID
        mTA.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMTAMockMvc.perform(post("/api/mtas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mTA)))
            .andExpect(status().isBadRequest());

        // Validate the MTA in the database
        List<MTA> mTAList = mTARepository.findAll();
        assertThat(mTAList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mTARepository.findAll().size();
        // set the field null
        mTA.setStartDate(null);

        // Create the MTA, which fails.

        restMTAMockMvc.perform(post("/api/mtas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mTA)))
            .andExpect(status().isBadRequest());

        List<MTA> mTAList = mTARepository.findAll();
        assertThat(mTAList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = mTARepository.findAll().size();
        // set the field null
        mTA.setEndDate(null);

        // Create the MTA, which fails.

        restMTAMockMvc.perform(post("/api/mtas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mTA)))
            .andExpect(status().isBadRequest());

        List<MTA> mTAList = mTARepository.findAll();
        assertThat(mTAList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMTAS() throws Exception {
        // Initialize the database
        mTARepository.saveAndFlush(mTA);

        // Get all the mTAList
        restMTAMockMvc.perform(get("/api/mtas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mTA.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getMTA() throws Exception {
        // Initialize the database
        mTARepository.saveAndFlush(mTA);

        // Get the mTA
        restMTAMockMvc.perform(get("/api/mtas/{id}", mTA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mTA.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMTA() throws Exception {
        // Get the mTA
        restMTAMockMvc.perform(get("/api/mtas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMTA() throws Exception {
        // Initialize the database
        mTARepository.saveAndFlush(mTA);

        int databaseSizeBeforeUpdate = mTARepository.findAll().size();

        // Update the mTA
        MTA updatedMTA = mTARepository.findById(mTA.getId()).get();
        // Disconnect from session so that the updates on updatedMTA are not directly saved in db
        em.detach(updatedMTA);
        updatedMTA
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restMTAMockMvc.perform(put("/api/mtas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMTA)))
            .andExpect(status().isOk());

        // Validate the MTA in the database
        List<MTA> mTAList = mTARepository.findAll();
        assertThat(mTAList).hasSize(databaseSizeBeforeUpdate);
        MTA testMTA = mTAList.get(mTAList.size() - 1);
        assertThat(testMTA.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMTA.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMTA() throws Exception {
        int databaseSizeBeforeUpdate = mTARepository.findAll().size();

        // Create the MTA

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMTAMockMvc.perform(put("/api/mtas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mTA)))
            .andExpect(status().isBadRequest());

        // Validate the MTA in the database
        List<MTA> mTAList = mTARepository.findAll();
        assertThat(mTAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMTA() throws Exception {
        // Initialize the database
        mTARepository.saveAndFlush(mTA);

        int databaseSizeBeforeDelete = mTARepository.findAll().size();

        // Delete the mTA
        restMTAMockMvc.perform(delete("/api/mtas/{id}", mTA.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MTA> mTAList = mTARepository.findAll();
        assertThat(mTAList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
