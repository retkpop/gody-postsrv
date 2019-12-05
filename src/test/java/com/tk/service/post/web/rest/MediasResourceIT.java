package com.tk.service.post.web.rest;

import com.tk.service.post.PostServiceApp;
import com.tk.service.post.domain.Medias;
import com.tk.service.post.repository.MediasRepository;
import com.tk.service.post.service.MediasService;
import com.tk.service.post.service.dto.MediasDTO;
import com.tk.service.post.service.mapper.MediasMapper;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tk.service.post.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tk.service.post.domain.enumeration.TYPEMEDIA;
/**
 * Integration tests for the {@Link MediasResource} REST controller.
 */
@SpringBootTest(classes = PostServiceApp.class)
public class MediasResourceIT {

    private static final byte[] DEFAULT_NAME = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_NAME = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_NAME_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_NAME_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_FILE_EXTENSION = "BBBBBBBBBB";

    private static final TYPEMEDIA DEFAULT_TYPE = TYPEMEDIA.THUMBNAIL;
    private static final TYPEMEDIA UPDATED_TYPE = TYPEMEDIA.GALLERY;

    @Autowired
    private MediasRepository mediasRepository;

    @Autowired
    private MediasMapper mediasMapper;

    @Autowired
    private MediasService mediasService;

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

    private MockMvc restMediasMockMvc;

    private Medias medias;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MediasResource mediasResource = new MediasResource(mediasService);
        this.restMediasMockMvc = MockMvcBuilders.standaloneSetup(mediasResource)
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
    public static Medias createEntity(EntityManager em) {
        Medias medias = new Medias()
            .name(DEFAULT_NAME)
            .nameContentType(DEFAULT_NAME_CONTENT_TYPE)
            .fileExtension(DEFAULT_FILE_EXTENSION)
            .type(DEFAULT_TYPE);
        return medias;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medias createUpdatedEntity(EntityManager em) {
        Medias medias = new Medias()
            .name(UPDATED_NAME)
            .nameContentType(UPDATED_NAME_CONTENT_TYPE)
            .fileExtension(UPDATED_FILE_EXTENSION)
            .type(UPDATED_TYPE);
        return medias;
    }

    @BeforeEach
    public void initTest() {
        medias = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedias() throws Exception {
        int databaseSizeBeforeCreate = mediasRepository.findAll().size();

        // Create the Medias
        MediasDTO mediasDTO = mediasMapper.toDto(medias);
        restMediasMockMvc.perform(post("/api/medias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediasDTO)))
            .andExpect(status().isCreated());

        // Validate the Medias in the database
        List<Medias> mediasList = mediasRepository.findAll();
        assertThat(mediasList).hasSize(databaseSizeBeforeCreate + 1);
        Medias testMedias = mediasList.get(mediasList.size() - 1);
        assertThat(testMedias.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedias.getNameContentType()).isEqualTo(DEFAULT_NAME_CONTENT_TYPE);
        assertThat(testMedias.getFileExtension()).isEqualTo(DEFAULT_FILE_EXTENSION);
        assertThat(testMedias.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createMediasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mediasRepository.findAll().size();

        // Create the Medias with an existing ID
        medias.setId(1L);
        MediasDTO mediasDTO = mediasMapper.toDto(medias);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediasMockMvc.perform(post("/api/medias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medias in the database
        List<Medias> mediasList = mediasRepository.findAll();
        assertThat(mediasList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMedias() throws Exception {
        // Initialize the database
        mediasRepository.saveAndFlush(medias);

        // Get all the mediasList
        restMediasMockMvc.perform(get("/api/medias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medias.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameContentType").value(hasItem(DEFAULT_NAME_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(Base64Utils.encodeToString(DEFAULT_NAME))))
            .andExpect(jsonPath("$.[*].fileExtension").value(hasItem(DEFAULT_FILE_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getMedias() throws Exception {
        // Initialize the database
        mediasRepository.saveAndFlush(medias);

        // Get the medias
        restMediasMockMvc.perform(get("/api/medias/{id}", medias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medias.getId().intValue()))
            .andExpect(jsonPath("$.nameContentType").value(DEFAULT_NAME_CONTENT_TYPE))
            .andExpect(jsonPath("$.name").value(Base64Utils.encodeToString(DEFAULT_NAME)))
            .andExpect(jsonPath("$.fileExtension").value(DEFAULT_FILE_EXTENSION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedias() throws Exception {
        // Get the medias
        restMediasMockMvc.perform(get("/api/medias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedias() throws Exception {
        // Initialize the database
        mediasRepository.saveAndFlush(medias);

        int databaseSizeBeforeUpdate = mediasRepository.findAll().size();

        // Update the medias
        Medias updatedMedias = mediasRepository.findById(medias.getId()).get();
        // Disconnect from session so that the updates on updatedMedias are not directly saved in db
        em.detach(updatedMedias);
        updatedMedias
            .name(UPDATED_NAME)
            .nameContentType(UPDATED_NAME_CONTENT_TYPE)
            .fileExtension(UPDATED_FILE_EXTENSION)
            .type(UPDATED_TYPE);
        MediasDTO mediasDTO = mediasMapper.toDto(updatedMedias);

        restMediasMockMvc.perform(put("/api/medias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediasDTO)))
            .andExpect(status().isOk());

        // Validate the Medias in the database
        List<Medias> mediasList = mediasRepository.findAll();
        assertThat(mediasList).hasSize(databaseSizeBeforeUpdate);
        Medias testMedias = mediasList.get(mediasList.size() - 1);
        assertThat(testMedias.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedias.getNameContentType()).isEqualTo(UPDATED_NAME_CONTENT_TYPE);
        assertThat(testMedias.getFileExtension()).isEqualTo(UPDATED_FILE_EXTENSION);
        assertThat(testMedias.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingMedias() throws Exception {
        int databaseSizeBeforeUpdate = mediasRepository.findAll().size();

        // Create the Medias
        MediasDTO mediasDTO = mediasMapper.toDto(medias);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediasMockMvc.perform(put("/api/medias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medias in the database
        List<Medias> mediasList = mediasRepository.findAll();
        assertThat(mediasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMedias() throws Exception {
        // Initialize the database
        mediasRepository.saveAndFlush(medias);

        int databaseSizeBeforeDelete = mediasRepository.findAll().size();

        // Delete the medias
        restMediasMockMvc.perform(delete("/api/medias/{id}", medias.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medias> mediasList = mediasRepository.findAll();
        assertThat(mediasList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medias.class);
        Medias medias1 = new Medias();
        medias1.setId(1L);
        Medias medias2 = new Medias();
        medias2.setId(medias1.getId());
        assertThat(medias1).isEqualTo(medias2);
        medias2.setId(2L);
        assertThat(medias1).isNotEqualTo(medias2);
        medias1.setId(null);
        assertThat(medias1).isNotEqualTo(medias2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MediasDTO.class);
        MediasDTO mediasDTO1 = new MediasDTO();
        mediasDTO1.setId(1L);
        MediasDTO mediasDTO2 = new MediasDTO();
        assertThat(mediasDTO1).isNotEqualTo(mediasDTO2);
        mediasDTO2.setId(mediasDTO1.getId());
        assertThat(mediasDTO1).isEqualTo(mediasDTO2);
        mediasDTO2.setId(2L);
        assertThat(mediasDTO1).isNotEqualTo(mediasDTO2);
        mediasDTO1.setId(null);
        assertThat(mediasDTO1).isNotEqualTo(mediasDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mediasMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mediasMapper.fromId(null)).isNull();
    }
}
