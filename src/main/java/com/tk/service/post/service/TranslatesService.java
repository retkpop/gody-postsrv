package com.tk.service.post.service;

import com.tk.service.post.service.dto.TranslatesDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.tk.service.post.domain.Translates}.
 */
public interface TranslatesService {

    /**
     * Save a translates.
     *
     * @param translatesDTO the entity to save.
     * @return the persisted entity.
     */
    TranslatesDTO save(TranslatesDTO translatesDTO);

    /**
     * Get all the translates.
     *
     * @return the list of entities.
     */
    List<TranslatesDTO> findAll();


    /**
     * Get the "id" translates.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TranslatesDTO> findOne(Long id);

    /**
     * Delete the "id" translates.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
