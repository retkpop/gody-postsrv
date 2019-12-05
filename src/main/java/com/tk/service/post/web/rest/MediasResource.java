package com.tk.service.post.web.rest;

import com.tk.service.post.service.MediasService;
import com.tk.service.post.web.rest.errors.BadRequestAlertException;
import com.tk.service.post.service.dto.MediasDTO;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.tk.service.post.domain.Medias}.
 */
@RestController
@RequestMapping("/api")
public class MediasResource {

    private final Logger log = LoggerFactory.getLogger(MediasResource.class);

    private static final String ENTITY_NAME = "postServiceMedias";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MediasService mediasService;

    public MediasResource(MediasService mediasService) {
        this.mediasService = mediasService;
    }

    /**
     * {@code POST  /medias} : Create a new medias.
     *
     * @param mediasDTO the mediasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mediasDTO, or with status {@code 400 (Bad Request)} if the medias has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medias")
    public ResponseEntity<MediasDTO> createMedias(@RequestBody MediasDTO mediasDTO) throws URISyntaxException {
        log.debug("REST request to save Medias : {}", mediasDTO);
        if (mediasDTO.getId() != null) {
            throw new BadRequestAlertException("A new medias cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MediasDTO result = mediasService.save(mediasDTO);
        return ResponseEntity.created(new URI("/api/medias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medias} : Updates an existing medias.
     *
     * @param mediasDTO the mediasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mediasDTO,
     * or with status {@code 400 (Bad Request)} if the mediasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mediasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medias")
    public ResponseEntity<MediasDTO> updateMedias(@RequestBody MediasDTO mediasDTO) throws URISyntaxException {
        log.debug("REST request to update Medias : {}", mediasDTO);
        if (mediasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MediasDTO result = mediasService.save(mediasDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mediasDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /medias} : get all the medias.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medias in body.
     */
    @GetMapping("/medias")
    public List<MediasDTO> getAllMedias(@RequestParam(required = false) String filter) {
        if ("mpost-is-null".equals(filter)) {
            log.debug("REST request to get all Mediass where mpost is null");
            return mediasService.findAllWhereMpostIsNull();
        }
        if ("category-is-null".equals(filter)) {
            log.debug("REST request to get all Mediass where category is null");
            return mediasService.findAllWhereCategoryIsNull();
        }
        log.debug("REST request to get all Medias");
        return mediasService.findAll();
    }

    /**
     * {@code GET  /medias/:id} : get the "id" medias.
     *
     * @param id the id of the mediasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mediasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medias/{id}")
    public ResponseEntity<MediasDTO> getMedias(@PathVariable Long id) {
        log.debug("REST request to get Medias : {}", id);
        Optional<MediasDTO> mediasDTO = mediasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mediasDTO);
    }

    /**
     * {@code DELETE  /medias/:id} : delete the "id" medias.
     *
     * @param id the id of the mediasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medias/{id}")
    public ResponseEntity<Void> deleteMedias(@PathVariable Long id) {
        log.debug("REST request to delete Medias : {}", id);
        mediasService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
