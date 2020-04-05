package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.InsuranceModuleApp;
import com.apache.fineract.cn.insurance.domain.PaymentSchedule;
import com.apache.fineract.cn.insurance.repository.PaymentScheduleRepository;

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
 * Integration tests for the {@link PaymentScheduleResource} REST controller.
 */
@SpringBootTest(classes = InsuranceModuleApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PaymentScheduleResourceIT {

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT = "BBBBBBBBBB";

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentScheduleMockMvc;

    private PaymentSchedule paymentSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSchedule createEntity(EntityManager em) {
        PaymentSchedule paymentSchedule = new PaymentSchedule()
            .dueDate(DEFAULT_DUE_DATE)
            .amount(DEFAULT_AMOUNT);
        return paymentSchedule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSchedule createUpdatedEntity(EntityManager em) {
        PaymentSchedule paymentSchedule = new PaymentSchedule()
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);
        return paymentSchedule;
    }

    @BeforeEach
    public void initTest() {
        paymentSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentSchedule() throws Exception {
        int databaseSizeBeforeCreate = paymentScheduleRepository.findAll().size();

        // Create the PaymentSchedule
        restPaymentScheduleMockMvc.perform(post("/api/payment-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentSchedule)))
            .andExpect(status().isCreated());

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentSchedule testPaymentSchedule = paymentScheduleList.get(paymentScheduleList.size() - 1);
        assertThat(testPaymentSchedule.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testPaymentSchedule.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createPaymentScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentScheduleRepository.findAll().size();

        // Create the PaymentSchedule with an existing ID
        paymentSchedule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentScheduleMockMvc.perform(post("/api/payment-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentScheduleRepository.findAll().size();
        // set the field null
        paymentSchedule.setDueDate(null);

        // Create the PaymentSchedule, which fails.

        restPaymentScheduleMockMvc.perform(post("/api/payment-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentSchedule)))
            .andExpect(status().isBadRequest());

        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentScheduleRepository.findAll().size();
        // set the field null
        paymentSchedule.setAmount(null);

        // Create the PaymentSchedule, which fails.

        restPaymentScheduleMockMvc.perform(post("/api/payment-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentSchedule)))
            .andExpect(status().isBadRequest());

        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentSchedules() throws Exception {
        // Initialize the database
        paymentScheduleRepository.saveAndFlush(paymentSchedule);

        // Get all the paymentScheduleList
        restPaymentScheduleMockMvc.perform(get("/api/payment-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }
    
    @Test
    @Transactional
    public void getPaymentSchedule() throws Exception {
        // Initialize the database
        paymentScheduleRepository.saveAndFlush(paymentSchedule);

        // Get the paymentSchedule
        restPaymentScheduleMockMvc.perform(get("/api/payment-schedules/{id}", paymentSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentSchedule.getId().intValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentSchedule() throws Exception {
        // Get the paymentSchedule
        restPaymentScheduleMockMvc.perform(get("/api/payment-schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentSchedule() throws Exception {
        // Initialize the database
        paymentScheduleRepository.saveAndFlush(paymentSchedule);

        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().size();

        // Update the paymentSchedule
        PaymentSchedule updatedPaymentSchedule = paymentScheduleRepository.findById(paymentSchedule.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentSchedule are not directly saved in db
        em.detach(updatedPaymentSchedule);
        updatedPaymentSchedule
            .dueDate(UPDATED_DUE_DATE)
            .amount(UPDATED_AMOUNT);

        restPaymentScheduleMockMvc.perform(put("/api/payment-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentSchedule)))
            .andExpect(status().isOk());

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
        PaymentSchedule testPaymentSchedule = paymentScheduleList.get(paymentScheduleList.size() - 1);
        assertThat(testPaymentSchedule.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testPaymentSchedule.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentSchedule() throws Exception {
        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().size();

        // Create the PaymentSchedule

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentScheduleMockMvc.perform(put("/api/payment-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentSchedule() throws Exception {
        // Initialize the database
        paymentScheduleRepository.saveAndFlush(paymentSchedule);

        int databaseSizeBeforeDelete = paymentScheduleRepository.findAll().size();

        // Delete the paymentSchedule
        restPaymentScheduleMockMvc.perform(delete("/api/payment-schedules/{id}", paymentSchedule.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
