package com.tk.service.post.web.rest;

import com.tk.service.post.PostServiceApp;
import com.tk.service.post.domain.Categories;
import com.tk.service.post.repository.CategoriesRepository;
import com.tk.service.post.service.CategoriesService;
import com.tk.service.post.service.dto.CategoriesDTO;
import com.tk.service.post.service.mapper.CategoriesMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.tk.service.post.web.rest.TestUtil.sameInstant;
import static com.tk.service.post.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tk.service.post.domain.enumeration.STATUS;
/**
 * Integration tests for the {@Link CategoriesResource} REST controller.
 */
@SpringBootTest(classes = PostServiceApp.class)
public class CategoriesResourceIT {

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_UPDATE_BY = 1L;
    private static final Long UPDATED_UPDATE_BY = 2L;

    private static final STATUS DEFAULT_STATUS = STATUS.PUBLIC;
    private static final STATUS UPDATED_STATUS = STATUS.DELETE;

    private static final Long DEFAULT_PARENT = 1L;
    private static final Long UPDATED_PARENT = 2L;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private CategoriesMapper categoriesMapper;

    @Autowired
    private CategoriesService categoriesService;

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

    private MockMvc restCategoriesMockMvc;

    private Categories categories;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategoriesResource categoriesResource = new CategoriesResource(categoriesService);
        this.restCategoriesMockMvc = MockMvcBuilders.standaloneSetup(categoriesResource)
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
    public static Categories createEntity(EntityManager em) {
        Categories categories = new Categories()
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .updateBy(DEFAULT_UPDATE_BY)
            .status(DEFAULT_STATUS)
            .parent(DEFAULT_PARENT);
        return categories;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categories createUpdatedEntity(EntityManager em) {
        Categories categories = new Categories()
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .updateBy(UPDATED_UPDATE_BY)
            .status(UPDATED_STATUS)
            .parent(UPDATED_PARENT);
        return categories;
    }

    @BeforeEach
    public void initTest() {
        categories = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategories() throws Exception {
        int databaseSizeBeforeCreate = categoriesRepository.findAll().size();

        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);
        restCategoriesMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoriesDTO)))
            .andExpect(status().isCreated());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeCreate + 1);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCategories.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCategories.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testCategories.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testCategories.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCategories.getParent()).isEqualTo(DEFAULT_PARENT);
    }

    @Test
    @Transactional
    public void createCategoriesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoriesRepository.findAll().size();

        // Create the Categories with an existing ID
        categories.setId(1L);
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriesMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoriesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList
        restCategoriesMockMvc.perform(get("/api/categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categories.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE))))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.intValue())));
    }
    
    @Test
    @Transactional
    public void getCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get the categories
        restCategoriesMockMvc.perform(get("/api/categories/{id}", categories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categories.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdate").value(sameInstant(DEFAULT_LAST_UPDATE)))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCategories() throws Exception {
        // Get the categories
        restCategoriesMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Update the categories
        Categories updatedCategories = categoriesRepository.findById(categories.getId()).get();
        // Disconnect from session so that the updates on updatedCategories are not directly saved in db
        em.detach(updatedCategories);
        updatedCategories
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .updateBy(UPDATED_UPDATE_BY)
            .status(UPDATED_STATUS)
            .parent(UPDATED_PARENT);
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(updatedCategories);

        restCategoriesMockMvc.perform(put("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoriesDTO)))
            .andExpect(status().isOk());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCategories.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCategories.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testCategories.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testCategories.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCategories.getParent()).isEqualTo(UPDATED_PARENT);
    }

    @Test
    @Transactional
    public void updateNonExistingCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Create the Categories
        CategoriesDTO categoriesDTO = categoriesMapper.toDto(categories);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriesMockMvc.perform(put("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoriesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeDelete = categoriesRepository.findAll().size();

        // Delete the categories
        restCategoriesMockMvc.perform(delete("/api/categories/{id}", categories.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categories.class);
        Categories categories1 = new Categories();
        categories1.setId(1L);
        Categories categories2 = new Categories();
        categories2.setId(categories1.getId());
        assertThat(categories1).isEqualTo(categories2);
        categories2.setId(2L);
        assertThat(categories1).isNotEqualTo(categories2);
        categories1.setId(null);
        assertThat(categories1).isNotEqualTo(categories2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriesDTO.class);
        CategoriesDTO categoriesDTO1 = new CategoriesDTO();
        categoriesDTO1.setId(1L);
        CategoriesDTO categoriesDTO2 = new CategoriesDTO();
        assertThat(categoriesDTO1).isNotEqualTo(categoriesDTO2);
        categoriesDTO2.setId(categoriesDTO1.getId());
        assertThat(categoriesDTO1).isEqualTo(categoriesDTO2);
        categoriesDTO2.setId(2L);
        assertThat(categoriesDTO1).isNotEqualTo(categoriesDTO2);
        categoriesDTO1.setId(null);
        assertThat(categoriesDTO1).isNotEqualTo(categoriesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(categoriesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(categoriesMapper.fromId(null)).isNull();
    }
}
