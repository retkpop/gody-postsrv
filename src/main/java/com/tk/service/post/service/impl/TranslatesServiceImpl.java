package com.tk.service.post.service.impl;

import com.tk.service.post.service.TranslatesService;
import com.tk.service.post.domain.Translates;
import com.tk.service.post.repository.TranslatesRepository;
import com.tk.service.post.service.dto.TranslatesDTO;
import com.tk.service.post.service.mapper.TranslatesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Translates}.
 */
@Service
@Transactional
public class TranslatesServiceImpl implements TranslatesService {

    private final Logger log = LoggerFactory.getLogger(TranslatesServiceImpl.class);

    private final TranslatesRepository translatesRepository;

    private final TranslatesMapper translatesMapper;

    public TranslatesServiceImpl(TranslatesRepository translatesRepository, TranslatesMapper translatesMapper) {
        this.translatesRepository = translatesRepository;
        this.translatesMapper = translatesMapper;
    }

    /**
     * Save a translates.
     *
     * @param translatesDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TranslatesDTO save(TranslatesDTO translatesDTO) {
        log.debug("Request to save Translates : {}", translatesDTO);
        Translates translates = translatesMapper.toEntity(translatesDTO);
        translates = translatesRepository.save(translates);
        return translatesMapper.toDto(translates);
    }

    /**
     * Get all the translates.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TranslatesDTO> findAll() {
        log.debug("Request to get all Translates");
        return translatesRepository.findAll().stream()
            .map(translatesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one translates by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TranslatesDTO> findOne(Long id) {
        log.debug("Request to get Translates : {}", id);
        return translatesRepository.findById(id)
            .map(translatesMapper::toDto);
    }

    /**
     * Delete the translates by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Translates : {}", id);
        translatesRepository.deleteById(id);
    }
}
