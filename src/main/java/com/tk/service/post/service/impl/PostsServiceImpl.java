package com.tk.service.post.service.impl;

import com.tk.service.post.service.PostsService;
import com.tk.service.post.domain.Posts;
import com.tk.service.post.repository.PostsRepository;
import com.tk.service.post.service.dto.PostsDTO;
import com.tk.service.post.service.mapper.PostsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Posts}.
 */
@Service
@Transactional
public class PostsServiceImpl implements PostsService {

    private final Logger log = LoggerFactory.getLogger(PostsServiceImpl.class);

    private final PostsRepository postsRepository;

    private final PostsMapper postsMapper;

    public PostsServiceImpl(PostsRepository postsRepository, PostsMapper postsMapper) {
        this.postsRepository = postsRepository;
        this.postsMapper = postsMapper;
    }

    /**
     * Save a posts.
     *
     * @param postsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PostsDTO save(PostsDTO postsDTO) {
        log.debug("Request to save Posts : {}", postsDTO);
        Posts posts = postsMapper.toEntity(postsDTO);
        posts = postsRepository.save(posts);
        return postsMapper.toDto(posts);
    }

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postsRepository.findAll(pageable)
            .map(postsMapper::toDto);
    }

    /**
     * Get all the posts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PostsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return postsRepository.findAllWithEagerRelationships(pageable).map(postsMapper::toDto);
    }
    

    /**
     * Get one posts by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PostsDTO> findOne(Long id) {
        log.debug("Request to get Posts : {}", id);
        return postsRepository.findOneWithEagerRelationships(id)
            .map(postsMapper::toDto);
    }

    /**
     * Delete the posts by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Posts : {}", id);
        postsRepository.deleteById(id);
    }
}
