package com.tk.service.post.service;

import com.tk.service.post.domain.Medias;
import com.tk.service.post.repository.MediasRepository;
import com.tk.service.post.service.dto.MediasDTO;
import com.tk.service.post.service.mapper.MediasMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link Medias}.
 */
@Service
@Transactional
public class MediasService {

    private final Logger log = LoggerFactory.getLogger(MediasService.class);

    private final MediasRepository mediasRepository;

    private final MediasMapper mediasMapper;

    public MediasService(MediasRepository mediasRepository, MediasMapper mediasMapper) {
        this.mediasRepository = mediasRepository;
        this.mediasMapper = mediasMapper;
    }

    /**
     * Save a medias.
     *
     * @param mediasDTO the entity to save.
     * @return the persisted entity.
     */
    public MediasDTO save(MediasDTO mediasDTO) {
        log.debug("Request to save Medias : {}", mediasDTO);
        Medias medias = mediasMapper.toEntity(mediasDTO);
        medias = mediasRepository.save(medias);
        return mediasMapper.toDto(medias);
    }

    /**
     * Get all the medias.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MediasDTO> findAll() {
        log.debug("Request to get all Medias");
        return mediasRepository.findAll().stream()
            .map(mediasMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the medias where Mpost is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<MediasDTO> findAllWhereMpostIsNull() {
        log.debug("Request to get all medias where Mpost is null");
        return StreamSupport
            .stream(mediasRepository.findAll().spliterator(), false)
            .filter(medias -> medias.getMpost() == null)
            .map(mediasMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
    *  Get all the medias where Category is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<MediasDTO> findAllWhereCategoryIsNull() {
        log.debug("Request to get all medias where Category is null");
        return StreamSupport
            .stream(mediasRepository.findAll().spliterator(), false)
            .filter(medias -> medias.getCategory() == null)
            .map(mediasMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one medias by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MediasDTO> findOne(Long id) {
        log.debug("Request to get Medias : {}", id);
        return mediasRepository.findById(id)
            .map(mediasMapper::toDto);
    }

    /**
     * Delete the medias by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medias : {}", id);
        mediasRepository.deleteById(id);
    }
}
