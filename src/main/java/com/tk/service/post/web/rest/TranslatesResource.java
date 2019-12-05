package com.tk.service.post.web.rest;

import com.tk.service.post.service.TranslatesService;
import com.tk.service.post.web.rest.errors.BadRequestAlertException;
import com.tk.service.post.service.dto.TranslatesDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.tk.service.post.domain.Translates}.
 */
@RestController
@RequestMapping("/api")
public class TranslatesResource {

    private final Logger log = LoggerFactory.getLogger(TranslatesResource.class);

    private static final String ENTITY_NAME = "postServiceTranslates";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TranslatesService translatesService;

    public TranslatesResource(TranslatesService translatesService) {
        this.translatesService = translatesService;
    }

    /**
     * {@code POST  /translates} : Create a new translates.
     *
     * @param translatesDTO the translatesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new translatesDTO, or with status {@code 400 (Bad Request)} if the translates has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/translates")
    public ResponseEntity<TranslatesDTO> createTranslates(@RequestBody TranslatesDTO translatesDTO) throws URISyntaxException {
        log.debug("REST request to save Translates : {}", translatesDTO);
        if (translatesDTO.getId() != null) {
            throw new BadRequestAlertException("A new translates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TranslatesDTO result = translatesService.save(translatesDTO);
        return ResponseEntity.created(new URI("/api/translates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /translates} : Updates an existing translates.
     *
     * @param translatesDTO the translatesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated translatesDTO,
     * or with status {@code 400 (Bad Request)} if the translatesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the translatesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/translates")
    public ResponseEntity<TranslatesDTO> updateTranslates(@RequestBody TranslatesDTO translatesDTO) throws URISyntaxException {
        log.debug("REST request to update Translates : {}", translatesDTO);
        if (translatesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TranslatesDTO result = translatesService.save(translatesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, translatesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /translates} : get all the translates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of translates in body.
     */
    @GetMapping("/translates")
    public List<TranslatesDTO> getAllTranslates() {
        log.debug("REST request to get all Translates");
        return translatesService.findAll();
    }

    /**
     * {@code GET  /translates/:id} : get the "id" translates.
     *
     * @param id the id of the translatesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the translatesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/translates/{id}")
    public ResponseEntity<TranslatesDTO> getTranslates(@PathVariable Long id) {
        log.debug("REST request to get Translates : {}", id);
        Optional<TranslatesDTO> translatesDTO = translatesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(translatesDTO);
    }

    /**
     * {@code DELETE  /translates/:id} : delete the "id" translates.
     *
     * @param id the id of the translatesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/translates/{id}")
    public ResponseEntity<Void> deleteTranslates(@PathVariable Long id) {
        log.debug("REST request to delete Translates : {}", id);
        translatesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
