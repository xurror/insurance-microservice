package com.apache.fineract.cn.insurance.web.rest;

import com.apache.fineract.cn.insurance.domain.MTA;
import com.apache.fineract.cn.insurance.repository.MTARepository;
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
 * REST controller for managing {@link com.apache.fineract.cn.insurance.domain.MTA}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MTAResource {

    private final Logger log = LoggerFactory.getLogger(MTAResource.class);

    private static final String ENTITY_NAME = "insuranceModuleMta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MTARepository mTARepository;

    public MTAResource(MTARepository mTARepository) {
        this.mTARepository = mTARepository;
    }

    /**
     * {@code POST  /mtas} : Create a new mTA.
     *
     * @param mTA the mTA to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mTA, or with status {@code 400 (Bad Request)} if the mTA has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mtas")
    public ResponseEntity<MTA> createMTA(@Valid @RequestBody MTA mTA) throws URISyntaxException {
        log.debug("REST request to save MTA : {}", mTA);
        if (mTA.getId() != null) {
            throw new BadRequestAlertException("A new mTA cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MTA result = mTARepository.save(mTA);
        return ResponseEntity.created(new URI("/api/mtas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mtas} : Updates an existing mTA.
     *
     * @param mTA the mTA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mTA,
     * or with status {@code 400 (Bad Request)} if the mTA is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mTA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mtas")
    public ResponseEntity<MTA> updateMTA(@Valid @RequestBody MTA mTA) throws URISyntaxException {
        log.debug("REST request to update MTA : {}", mTA);
        if (mTA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MTA result = mTARepository.save(mTA);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mTA.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mtas} : get all the mTAS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mTAS in body.
     */
    @GetMapping("/mtas")
    public List<MTA> getAllMTAS() {
        log.debug("REST request to get all MTAS");
        return mTARepository.findAll();
    }

    /**
     * {@code GET  /mtas/:id} : get the "id" mTA.
     *
     * @param id the id of the mTA to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mTA, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mtas/{id}")
    public ResponseEntity<MTA> getMTA(@PathVariable Long id) {
        log.debug("REST request to get MTA : {}", id);
        Optional<MTA> mTA = mTARepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mTA);
    }

    /**
     * {@code DELETE  /mtas/:id} : delete the "id" mTA.
     *
     * @param id the id of the mTA to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mtas/{id}")
    public ResponseEntity<Void> deleteMTA(@PathVariable Long id) {
        log.debug("REST request to delete MTA : {}", id);
        mTARepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
