package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.domain.Endorsement;
import com.apache.fineract.cn.insurance.repository.EndorsementRepository;
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
 * REST controller for managing {@link com.apache.fineract.cn.insurance.domain.Endorsement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EndorsementResource {

    private final Logger log = LoggerFactory.getLogger(EndorsementResource.class);

    private static final String ENTITY_NAME = "insuranceModuleEndorsement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EndorsementRepository endorsementRepository;

    public EndorsementResource(EndorsementRepository endorsementRepository) {
        this.endorsementRepository = endorsementRepository;
    }

    /**
     * {@code POST  /endorsements} : Create a new endorsement.
     *
     * @param endorsement the endorsement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new endorsement, or with status {@code 400 (Bad Request)} if the endorsement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/endorsements")
    public ResponseEntity<Endorsement> createEndorsement(@Valid @RequestBody Endorsement endorsement) throws URISyntaxException {
        log.debug("REST request to save Endorsement : {}", endorsement);
        if (endorsement.getId() != null) {
            throw new BadRequestAlertException("A new endorsement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Endorsement result = endorsementRepository.save(endorsement);
        return ResponseEntity.created(new URI("/api/endorsements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /endorsements} : Updates an existing endorsement.
     *
     * @param endorsement the endorsement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated endorsement,
     * or with status {@code 400 (Bad Request)} if the endorsement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the endorsement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/endorsements")
    public ResponseEntity<Endorsement> updateEndorsement(@Valid @RequestBody Endorsement endorsement) throws URISyntaxException {
        log.debug("REST request to update Endorsement : {}", endorsement);
        if (endorsement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Endorsement result = endorsementRepository.save(endorsement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, endorsement.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /endorsements} : get all the endorsements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of endorsements in body.
     */
    @GetMapping("/endorsements")
    public List<Endorsement> getAllEndorsements() {
        log.debug("REST request to get all Endorsements");
        return endorsementRepository.findAll();
    }

    /**
     * {@code GET  /endorsements/:id} : get the "id" endorsement.
     *
     * @param id the id of the endorsement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the endorsement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/endorsements/{id}")
    public ResponseEntity<Endorsement> getEndorsement(@PathVariable Long id) {
        log.debug("REST request to get Endorsement : {}", id);
        Optional<Endorsement> endorsement = endorsementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(endorsement);
    }

    /**
     * {@code DELETE  /endorsements/:id} : delete the "id" endorsement.
     *
     * @param id the id of the endorsement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/endorsements/{id}")
    public ResponseEntity<Void> deleteEndorsement(@PathVariable Long id) {
        log.debug("REST request to delete Endorsement : {}", id);
        endorsementRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
