package com.tk.service.post.web.rest;

import com.tk.service.post.PostServiceApp;
import com.tk.service.post.domain.Translates;
import com.tk.service.post.repository.TranslatesRepository;
import com.tk.service.post.service.TranslatesService;
import com.tk.service.post.service.dto.TranslatesDTO;
import com.tk.service.post.service.mapper.TranslatesMapper;
import com.tk.service.post.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tk.service.post.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tk.service.post.domain.enumeration.LANG;
import com.tk.service.post.domain.enumeration.TYPELANG;
/**
 * Integration tests for the {@Link TranslatesResource} REST controller.
 */
@SpringBootTest(classes = PostServiceApp.class)
public class TranslatesResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final LANG DEFAULT_LANG = LANG.EN;
    private static final LANG UPDATED_LANG = LANG.VI;

    private static final TYPELANG DEFAULT_TYPE_LANG = TYPELANG.TITLE;
    private static final TYPELANG UPDATED_TYPE_LANG = TYPELANG.SLUG;

    @Autowired
    private TranslatesRepository translatesRepository;

    @Autowired
    private TranslatesMapper translatesMapper;

    @Autowired
    private TranslatesService translatesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTranslatesMockMvc;

    private Translates translates;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TranslatesResource translatesResource = new TranslatesResource(translatesService);
        this.restTranslatesMockMvc = MockMvcBuilders.standaloneSetup(translatesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Translates createEntity(EntityManager em) {
        Translates translates = new Translates()
            .content(DEFAULT_CONTENT)
            .lang(DEFAULT_LANG)
            .typeLang(DEFAULT_TYPE_LANG);
        return translates;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Translates createUpdatedEntity(EntityManager em) {
        Translates translates = new Translates()
            .content(UPDATED_CONTENT)
            .lang(UPDATED_LANG)
            .typeLang(UPDATED_TYPE_LANG);
        return translates;
    }

    @BeforeEach
    public void initTest() {
        translates = createEntity(em);
    }

    @Test
    @Transactional
    public void createTranslates() throws Exception {
        int databaseSizeBeforeCreate = translatesRepository.findAll().size();

        // Create the Translates
        TranslatesDTO translatesDTO = translatesMapper.toDto(translates);
        restTranslatesMockMvc.perform(post("/api/translates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatesDTO)))
            .andExpect(status().isCreated());

        // Validate the Translates in the database
        List<Translates> translatesList = translatesRepository.findAll();
        assertThat(translatesList).hasSize(databaseSizeBeforeCreate + 1);
        Translates testTranslates = translatesList.get(translatesList.size() - 1);
        assertThat(testTranslates.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTranslates.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testTranslates.getTypeLang()).isEqualTo(DEFAULT_TYPE_LANG);
    }

    @Test
    @Transactional
    public void createTranslatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = translatesRepository.findAll().size();

        // Create the Translates with an existing ID
        translates.setId(1L);
        TranslatesDTO translatesDTO = translatesMapper.toDto(translates);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranslatesMockMvc.perform(post("/api/translates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Translates in the database
        List<Translates> translatesList = translatesRepository.findAll();
        assertThat(translatesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTranslates() throws Exception {
        // Initialize the database
        translatesRepository.saveAndFlush(translates);

        // Get all the translatesList
        restTranslatesMockMvc.perform(get("/api/translates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(translates.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].typeLang").value(hasItem(DEFAULT_TYPE_LANG.toString())));
    }
    
    @Test
    @Transactional
    public void getTranslates() throws Exception {
        // Initialize the database
        translatesRepository.saveAndFlush(translates);

        // Get the translates
        restTranslatesMockMvc.perform(get("/api/translates/{id}", translates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(translates.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.typeLang").value(DEFAULT_TYPE_LANG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranslates() throws Exception {
        // Get the translates
        restTranslatesMockMvc.perform(get("/api/translates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranslates() throws Exception {
        // Initialize the database
        translatesRepository.saveAndFlush(translates);

        int databaseSizeBeforeUpdate = translatesRepository.findAll().size();

        // Update the translates
        Translates updatedTranslates = translatesRepository.findById(translates.getId()).get();
        // Disconnect from session so that the updates on updatedTranslates are not directly saved in db
        em.detach(updatedTranslates);
        updatedTranslates
            .content(UPDATED_CONTENT)
            .lang(UPDATED_LANG)
            .typeLang(UPDATED_TYPE_LANG);
        TranslatesDTO translatesDTO = translatesMapper.toDto(updatedTranslates);

        restTranslatesMockMvc.perform(put("/api/translates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatesDTO)))
            .andExpect(status().isOk());

        // Validate the Translates in the database
        List<Translates> translatesList = translatesRepository.findAll();
        assertThat(translatesList).hasSize(databaseSizeBeforeUpdate);
        Translates testTranslates = translatesList.get(translatesList.size() - 1);
        assertThat(testTranslates.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTranslates.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testTranslates.getTypeLang()).isEqualTo(UPDATED_TYPE_LANG);
    }

    @Test
    @Transactional
    public void updateNonExistingTranslates() throws Exception {
        int databaseSizeBeforeUpdate = translatesRepository.findAll().size();

        // Create the Translates
        TranslatesDTO translatesDTO = translatesMapper.toDto(translates);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranslatesMockMvc.perform(put("/api/translates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Translates in the database
        List<Translates> translatesList = translatesRepository.findAll();
        assertThat(translatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTranslates() throws Exception {
        // Initialize the database
        translatesRepository.saveAndFlush(translates);

        int databaseSizeBeforeDelete = translatesRepository.findAll().size();

        // Delete the translates
        restTranslatesMockMvc.perform(delete("/api/translates/{id}", translates.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Translates> translatesList = translatesRepository.findAll();
        assertThat(translatesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Translates.class);
        Translates translates1 = new Translates();
        translates1.setId(1L);
        Translates translates2 = new Translates();
        translates2.setId(translates1.getId());
        assertThat(translates1).isEqualTo(translates2);
        translates2.setId(2L);
        assertThat(translates1).isNotEqualTo(translates2);
        translates1.setId(null);
        assertThat(translates1).isNotEqualTo(translates2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TranslatesDTO.class);
        TranslatesDTO translatesDTO1 = new TranslatesDTO();
        translatesDTO1.setId(1L);
        TranslatesDTO translatesDTO2 = new TranslatesDTO();
        assertThat(translatesDTO1).isNotEqualTo(translatesDTO2);
        translatesDTO2.setId(translatesDTO1.getId());
        assertThat(translatesDTO1).isEqualTo(translatesDTO2);
        translatesDTO2.setId(2L);
        assertThat(translatesDTO1).isNotEqualTo(translatesDTO2);
        translatesDTO1.setId(null);
        assertThat(translatesDTO1).isNotEqualTo(translatesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(translatesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(translatesMapper.fromId(null)).isNull();
    }
}
