package com.tk.service.post.web.rest;

import com.tk.service.post.PostServiceApp;
import com.tk.service.post.domain.Posts;
import com.tk.service.post.repository.PostsRepository;
import com.tk.service.post.service.PostsService;
import com.tk.service.post.service.dto.PostsDTO;
import com.tk.service.post.service.mapper.PostsMapper;
import com.tk.service.post.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;

import static com.tk.service.post.web.rest.TestUtil.sameInstant;
import static com.tk.service.post.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tk.service.post.domain.enumeration.STATUS;
import com.tk.service.post.domain.enumeration.TYPE;
/**
 * Integration tests for the {@Link PostsResource} REST controller.
 */
@SpringBootTest(classes = PostServiceApp.class)
public class PostsResourceIT {

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_UPDATE_BY = 1L;
    private static final Long UPDATED_UPDATE_BY = 2L;

    private static final Integer DEFAULT_VIEWS = 1;
    private static final Integer UPDATED_VIEWS = 2;

    private static final STATUS DEFAULT_STATUS = STATUS.PUBLIC;
    private static final STATUS UPDATED_STATUS = STATUS.DELETE;

    private static final TYPE DEFAULT_TYPE = TYPE.POST;
    private static final TYPE UPDATED_TYPE = TYPE.PAGE;

    @Autowired
    private PostsRepository postsRepository;

    @Mock
    private PostsRepository postsRepositoryMock;

    @Autowired
    private PostsMapper postsMapper;

    @Mock
    private PostsService postsServiceMock;

    @Autowired
    private PostsService postsService;

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

    private MockMvc restPostsMockMvc;

    private Posts posts;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostsResource postsResource = new PostsResource(postsService);
        this.restPostsMockMvc = MockMvcBuilders.standaloneSetup(postsResource)
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
    public static Posts createEntity(EntityManager em) {
        Posts posts = new Posts()
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .updateBy(DEFAULT_UPDATE_BY)
            .views(DEFAULT_VIEWS)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE);
        return posts;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posts createUpdatedEntity(EntityManager em) {
        Posts posts = new Posts()
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .updateBy(UPDATED_UPDATE_BY)
            .views(UPDATED_VIEWS)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE);
        return posts;
    }

    @BeforeEach
    public void initTest() {
        posts = createEntity(em);
    }

    @Test
    @Transactional
    public void createPosts() throws Exception {
        int databaseSizeBeforeCreate = postsRepository.findAll().size();

        // Create the Posts
        PostsDTO postsDTO = postsMapper.toDto(posts);
        restPostsMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isCreated());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeCreate + 1);
        Posts testPosts = postsList.get(postsList.size() - 1);
        assertThat(testPosts.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPosts.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPosts.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testPosts.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testPosts.getViews()).isEqualTo(DEFAULT_VIEWS);
        assertThat(testPosts.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPosts.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createPostsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postsRepository.findAll().size();

        // Create the Posts with an existing ID
        posts.setId(1L);
        PostsDTO postsDTO = postsMapper.toDto(posts);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostsMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList
        restPostsMockMvc.perform(get("/api/posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posts.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE))))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.intValue())))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPostsWithEagerRelationshipsIsEnabled() throws Exception {
        PostsResource postsResource = new PostsResource(postsServiceMock);
        when(postsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPostsMockMvc = MockMvcBuilders.standaloneSetup(postsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPostsMockMvc.perform(get("/api/posts?eagerload=true"))
        .andExpect(status().isOk());

        verify(postsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPostsWithEagerRelationshipsIsNotEnabled() throws Exception {
        PostsResource postsResource = new PostsResource(postsServiceMock);
            when(postsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPostsMockMvc = MockMvcBuilders.standaloneSetup(postsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPostsMockMvc.perform(get("/api/posts?eagerload=true"))
        .andExpect(status().isOk());

            verify(postsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get the posts
        restPostsMockMvc.perform(get("/api/posts/{id}", posts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(posts.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdate").value(sameInstant(DEFAULT_LAST_UPDATE)))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.intValue()))
            .andExpect(jsonPath("$.views").value(DEFAULT_VIEWS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPosts() throws Exception {
        // Get the posts
        restPostsMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        int databaseSizeBeforeUpdate = postsRepository.findAll().size();

        // Update the posts
        Posts updatedPosts = postsRepository.findById(posts.getId()).get();
        // Disconnect from session so that the updates on updatedPosts are not directly saved in db
        em.detach(updatedPosts);
        updatedPosts
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .updateBy(UPDATED_UPDATE_BY)
            .views(UPDATED_VIEWS)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE);
        PostsDTO postsDTO = postsMapper.toDto(updatedPosts);

        restPostsMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isOk());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeUpdate);
        Posts testPosts = postsList.get(postsList.size() - 1);
        assertThat(testPosts.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPosts.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPosts.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testPosts.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testPosts.getViews()).isEqualTo(UPDATED_VIEWS);
        assertThat(testPosts.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPosts.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPosts() throws Exception {
        int databaseSizeBeforeUpdate = postsRepository.findAll().size();

        // Create the Posts
        PostsDTO postsDTO = postsMapper.toDto(posts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostsMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        int databaseSizeBeforeDelete = postsRepository.findAll().size();

        // Delete the posts
        restPostsMockMvc.perform(delete("/api/posts/{id}", posts.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Posts.class);
        Posts posts1 = new Posts();
        posts1.setId(1L);
        Posts posts2 = new Posts();
        posts2.setId(posts1.getId());
        assertThat(posts1).isEqualTo(posts2);
        posts2.setId(2L);
        assertThat(posts1).isNotEqualTo(posts2);
        posts1.setId(null);
        assertThat(posts1).isNotEqualTo(posts2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostsDTO.class);
        PostsDTO postsDTO1 = new PostsDTO();
        postsDTO1.setId(1L);
        PostsDTO postsDTO2 = new PostsDTO();
        assertThat(postsDTO1).isNotEqualTo(postsDTO2);
        postsDTO2.setId(postsDTO1.getId());
        assertThat(postsDTO1).isEqualTo(postsDTO2);
        postsDTO2.setId(2L);
        assertThat(postsDTO1).isNotEqualTo(postsDTO2);
        postsDTO1.setId(null);
        assertThat(postsDTO1).isNotEqualTo(postsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(postsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(postsMapper.fromId(null)).isNull();
    }
}
