package com.tk.service.post.service;

import com.tk.service.post.service.dto.PostsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.tk.service.post.domain.Posts}.
 */
public interface PostsService {

    /**
     * Save a posts.
     *
     * @param postsDTO the entity to save.
     * @return the persisted entity.
     */
    PostsDTO save(PostsDTO postsDTO);

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostsDTO> findAll(Pageable pageable);

    /**
     * Get all the posts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<PostsDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" posts.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostsDTO> findOne(Long id);

    /**
     * Delete the "id" posts.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
