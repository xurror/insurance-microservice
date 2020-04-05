package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.domain.PaymentSchedule;
import com.apache.fineract.cn.insurance.repository.PaymentScheduleRepository;
import com.apache.fineract.cn.insurance.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.apache.fineract.cn.insurance.domain.PaymentSchedule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PaymentScheduleResource {

    private final Logger log = LoggerFactory.getLogger(PaymentScheduleResource.class);

    private static final String ENTITY_NAME = "insuranceModulePaymentSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentScheduleRepository paymentScheduleRepository;

    public PaymentScheduleResource(PaymentScheduleRepository paymentScheduleRepository) {
        this.paymentScheduleRepository = paymentScheduleRepository;
    }

    /**
     * {@code POST  /payment-schedules} : Create a new paymentSchedule.
     *
     * @param paymentSchedule the paymentSchedule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentSchedule, or with status {@code 400 (Bad Request)} if the paymentSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-schedules")
    public ResponseEntity<PaymentSchedule> createPaymentSchedule(@Valid @RequestBody PaymentSchedule paymentSchedule) throws URISyntaxException {
        log.debug("REST request to save PaymentSchedule : {}", paymentSchedule);
        if (paymentSchedule.getId() != null) {
            throw new BadRequestAlertException("A new paymentSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentSchedule result = paymentScheduleRepository.save(paymentSchedule);
        return ResponseEntity.created(new URI("/api/payment-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-schedules} : Updates an existing paymentSchedule.
     *
     * @param paymentSchedule the paymentSchedule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSchedule,
     * or with status {@code 400 (Bad Request)} if the paymentSchedule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentSchedule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-schedules")
    public ResponseEntity<PaymentSchedule> updatePaymentSchedule(@Valid @RequestBody PaymentSchedule paymentSchedule) throws URISyntaxException {
        log.debug("REST request to update PaymentSchedule : {}", paymentSchedule);
        if (paymentSchedule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentSchedule result = paymentScheduleRepository.save(paymentSchedule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSchedule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-schedules} : get all the paymentSchedules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentSchedules in body.
     */
    @GetMapping("/payment-schedules")
    public List<PaymentSchedule> getAllPaymentSchedules() {
        log.debug("REST request to get all PaymentSchedules");
        return paymentScheduleRepository.findAll();
    }

    /**
     * {@code GET  /payment-schedules/:id} : get the "id" paymentSchedule.
     *
     * @param id the id of the paymentSchedule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentSchedule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-schedules/{id}")
    public ResponseEntity<PaymentSchedule> getPaymentSchedule(@PathVariable Long id) {
        log.debug("REST request to get PaymentSchedule : {}", id);
        Optional<PaymentSchedule> paymentSchedule = paymentScheduleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paymentSchedule);
    }

    /**
     * {@code DELETE  /payment-schedules/:id} : delete the "id" paymentSchedule.
     *
     * @param id the id of the paymentSchedule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-schedules/{id}")
    public ResponseEntity<Void> deletePaymentSchedule(@PathVariable Long id) {
        log.debug("REST request to delete PaymentSchedule : {}", id);
        paymentScheduleRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
