package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.InsuranceModuleApp;
import com.apache.fineract.cn.insurance.domain.Quote;
import com.apache.fineract.cn.insurance.repository.QuoteRepository;

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
 * Integration tests for the {@link QuoteResource} REST controller.
 */
@SpringBootTest(classes = InsuranceModuleApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class QuoteResourceIT {

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_PREMIUM = 1F;
    private static final Float UPDATED_PREMIUM = 2F;

    private static final String DEFAULT_I_PT = "AAAAAAAAAA";
    private static final String UPDATED_I_PT = "BBBBBBBBBB";

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteMockMvc;

    private Quote quote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createEntity(EntityManager em) {
        Quote quote = new Quote()
            .ref(DEFAULT_REF)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .premium(DEFAULT_PREMIUM)
            .iPT(DEFAULT_I_PT);
        return quote;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createUpdatedEntity(EntityManager em) {
        Quote quote = new Quote()
            .ref(UPDATED_REF)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .premium(UPDATED_PREMIUM)
            .iPT(UPDATED_I_PT);
        return quote;
    }

    @BeforeEach
    public void initTest() {
        quote = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuote() throws Exception {
        int databaseSizeBeforeCreate = quoteRepository.findAll().size();

        // Create the Quote
        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isCreated());

        // Validate the Quote in the database
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeCreate + 1);
        Quote testQuote = quoteList.get(quoteList.size() - 1);
        assertThat(testQuote.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testQuote.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testQuote.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testQuote.getPremium()).isEqualTo(DEFAULT_PREMIUM);
        assertThat(testQuote.getiPT()).isEqualTo(DEFAULT_I_PT);
    }

    @Test
    @Transactional
    public void createQuoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quoteRepository.findAll().size();

        // Create the Quote with an existing ID
        quote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRefIsRequired() throws Exception {
        int databaseSizeBeforeTest = quoteRepository.findAll().size();
        // set the field null
        quote.setRef(null);

        // Create the Quote, which fails.

        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isBadRequest());

        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = quoteRepository.findAll().size();
        // set the field null
        quote.setStartDate(null);

        // Create the Quote, which fails.

        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isBadRequest());

        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = quoteRepository.findAll().size();
        // set the field null
        quote.setEndDate(null);

        // Create the Quote, which fails.

        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isBadRequest());

        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPremiumIsRequired() throws Exception {
        int databaseSizeBeforeTest = quoteRepository.findAll().size();
        // set the field null
        quote.setPremium(null);

        // Create the Quote, which fails.

        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isBadRequest());

        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkiPTIsRequired() throws Exception {
        int databaseSizeBeforeTest = quoteRepository.findAll().size();
        // set the field null
        quote.setiPT(null);

        // Create the Quote, which fails.

        restQuoteMockMvc.perform(post("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isBadRequest());

        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuotes() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);

        // Get all the quoteList
        restQuoteMockMvc.perform(get("/api/quotes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().intValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].premium").value(hasItem(DEFAULT_PREMIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].iPT").value(hasItem(DEFAULT_I_PT)));
    }
    
    @Test
    @Transactional
    public void getQuote() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);

        // Get the quote
        restQuoteMockMvc.perform(get("/api/quotes/{id}", quote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quote.getId().intValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.premium").value(DEFAULT_PREMIUM.doubleValue()))
            .andExpect(jsonPath("$.iPT").value(DEFAULT_I_PT));
    }

    @Test
    @Transactional
    public void getNonExistingQuote() throws Exception {
        // Get the quote
        restQuoteMockMvc.perform(get("/api/quotes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuote() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);

        int databaseSizeBeforeUpdate = quoteRepository.findAll().size();

        // Update the quote
        Quote updatedQuote = quoteRepository.findById(quote.getId()).get();
        // Disconnect from session so that the updates on updatedQuote are not directly saved in db
        em.detach(updatedQuote);
        updatedQuote
            .ref(UPDATED_REF)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .premium(UPDATED_PREMIUM)
            .iPT(UPDATED_I_PT);

        restQuoteMockMvc.perform(put("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuote)))
            .andExpect(status().isOk());

        // Validate the Quote in the database
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeUpdate);
        Quote testQuote = quoteList.get(quoteList.size() - 1);
        assertThat(testQuote.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testQuote.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testQuote.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testQuote.getPremium()).isEqualTo(UPDATED_PREMIUM);
        assertThat(testQuote.getiPT()).isEqualTo(UPDATED_I_PT);
    }

    @Test
    @Transactional
    public void updateNonExistingQuote() throws Exception {
        int databaseSizeBeforeUpdate = quoteRepository.findAll().size();

        // Create the Quote

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteMockMvc.perform(put("/api/quotes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quote)))
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuote() throws Exception {
        // Initialize the database
        quoteRepository.saveAndFlush(quote);

        int databaseSizeBeforeDelete = quoteRepository.findAll().size();

        // Delete the quote
        restQuoteMockMvc.perform(delete("/api/quotes/{id}", quote.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quote> quoteList = quoteRepository.findAll();
        assertThat(quoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
