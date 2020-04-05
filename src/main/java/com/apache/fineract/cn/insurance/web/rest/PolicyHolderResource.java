package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.domain.PolicyHolder;
import com.apache.fineract.cn.insurance.repository.PolicyHolderRepository;
import com.apache.fineract.cn.insurance.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.apache.fineract.cn.insurance.domain.PolicyHolder}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PolicyHolderResource {

    private final Logger log = LoggerFactory.getLogger(PolicyHolderResource.class);

    private static final String ENTITY_NAME = "insuranceModulePolicyHolder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PolicyHolderRepository policyHolderRepository;

    public PolicyHolderResource(PolicyHolderRepository policyHolderRepository) {
        this.policyHolderRepository = policyHolderRepository;
    }

    /**
     * {@code POST  /policy-holders} : Create a new policyHolder.
     *
     * @param policyHolder the policyHolder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new policyHolder, or with status {@code 400 (Bad Request)} if the policyHolder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/policy-holders")
    public ResponseEntity<PolicyHolder> createPolicyHolder(@RequestBody PolicyHolder policyHolder) throws URISyntaxException {
        log.debug("REST request to save PolicyHolder : {}", policyHolder);
        if (policyHolder.getId() != null) {
            throw new BadRequestAlertException("A new policyHolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PolicyHolder result = policyHolderRepository.save(policyHolder);
        return ResponseEntity.created(new URI("/api/policy-holders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /policy-holders} : Updates an existing policyHolder.
     *
     * @param policyHolder the policyHolder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policyHolder,
     * or with status {@code 400 (Bad Request)} if the policyHolder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the policyHolder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/policy-holders")
    public ResponseEntity<PolicyHolder> updatePolicyHolder(@RequestBody PolicyHolder policyHolder) throws URISyntaxException {
        log.debug("REST request to update PolicyHolder : {}", policyHolder);
        if (policyHolder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PolicyHolder result = policyHolderRepository.save(policyHolder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, policyHolder.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /policy-holders} : get all the policyHolders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of policyHolders in body.
     */
    @GetMapping("/policy-holders")
    public List<PolicyHolder> getAllPolicyHolders() {
        log.debug("REST request to get all PolicyHolders");
        return policyHolderRepository.findAll();
    }

    /**
     * {@code GET  /policy-holders/:id} : get the "id" policyHolder.
     *
     * @param id the id of the policyHolder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the policyHolder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/policy-holders/{id}")
    public ResponseEntity<PolicyHolder> getPolicyHolder(@PathVariable Long id) {
        log.debug("REST request to get PolicyHolder : {}", id);
        Optional<PolicyHolder> policyHolder = policyHolderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(policyHolder);
    }

    /**
     * {@code DELETE  /policy-holders/:id} : delete the "id" policyHolder.
     *
     * @param id the id of the policyHolder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/policy-holders/{id}")
    public ResponseEntity<Void> deletePolicyHolder(@PathVariable Long id) {
        log.debug("REST request to delete PolicyHolder : {}", id);
        policyHolderRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
