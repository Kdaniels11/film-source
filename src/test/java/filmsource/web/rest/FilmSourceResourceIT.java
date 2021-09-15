package filmsource.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import filmsource.IntegrationTest;
import filmsource.domain.FilmSource;
import filmsource.repository.FilmSourceRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FilmSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FilmSourceResourceIT {

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Long DEFAULT_RATE = 1L;
    private static final Long UPDATED_RATE = 2L;

    private static final String ENTITY_API_URL = "/api/film-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilmSourceRepository filmSourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilmSourceMockMvc;

    private FilmSource filmSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilmSource createEntity(EntityManager em) {
        FilmSource filmSource = new FilmSource()
            .lastname(DEFAULT_LASTNAME)
            .firstname(DEFAULT_FIRSTNAME)
            .position(DEFAULT_POSITION)
            .location(DEFAULT_LOCATION)
            .rate(DEFAULT_RATE);
        return filmSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilmSource createUpdatedEntity(EntityManager em) {
        FilmSource filmSource = new FilmSource()
            .lastname(UPDATED_LASTNAME)
            .firstname(UPDATED_FIRSTNAME)
            .position(UPDATED_POSITION)
            .location(UPDATED_LOCATION)
            .rate(UPDATED_RATE);
        return filmSource;
    }

    @BeforeEach
    public void initTest() {
        filmSource = createEntity(em);
    }

    @Test
    @Transactional
    void createFilmSource() throws Exception {
        int databaseSizeBeforeCreate = filmSourceRepository.findAll().size();
        // Create the FilmSource
        restFilmSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmSource)))
            .andExpect(status().isCreated());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeCreate + 1);
        FilmSource testFilmSource = filmSourceList.get(filmSourceList.size() - 1);
        assertThat(testFilmSource.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testFilmSource.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testFilmSource.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testFilmSource.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testFilmSource.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    @Transactional
    void createFilmSourceWithExistingId() throws Exception {
        // Create the FilmSource with an existing ID
        filmSource.setId(1L);

        int databaseSizeBeforeCreate = filmSourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilmSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmSource)))
            .andExpect(status().isBadRequest());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFilmSources() throws Exception {
        // Initialize the database
        filmSourceRepository.saveAndFlush(filmSource);

        // Get all the filmSourceList
        restFilmSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filmSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())));
    }

    @Test
    @Transactional
    void getFilmSource() throws Exception {
        // Initialize the database
        filmSourceRepository.saveAndFlush(filmSource);

        // Get the filmSource
        restFilmSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, filmSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filmSource.getId().intValue()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFilmSource() throws Exception {
        // Get the filmSource
        restFilmSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFilmSource() throws Exception {
        // Initialize the database
        filmSourceRepository.saveAndFlush(filmSource);

        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();

        // Update the filmSource
        FilmSource updatedFilmSource = filmSourceRepository.findById(filmSource.getId()).get();
        // Disconnect from session so that the updates on updatedFilmSource are not directly saved in db
        em.detach(updatedFilmSource);
        updatedFilmSource
            .lastname(UPDATED_LASTNAME)
            .firstname(UPDATED_FIRSTNAME)
            .position(UPDATED_POSITION)
            .location(UPDATED_LOCATION)
            .rate(UPDATED_RATE);

        restFilmSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFilmSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFilmSource))
            )
            .andExpect(status().isOk());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
        FilmSource testFilmSource = filmSourceList.get(filmSourceList.size() - 1);
        assertThat(testFilmSource.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testFilmSource.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testFilmSource.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testFilmSource.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testFilmSource.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    @Transactional
    void putNonExistingFilmSource() throws Exception {
        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();
        filmSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filmSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filmSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilmSource() throws Exception {
        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();
        filmSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filmSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilmSource() throws Exception {
        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();
        filmSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmSource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilmSourceWithPatch() throws Exception {
        // Initialize the database
        filmSourceRepository.saveAndFlush(filmSource);

        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();

        // Update the filmSource using partial update
        FilmSource partialUpdatedFilmSource = new FilmSource();
        partialUpdatedFilmSource.setId(filmSource.getId());

        partialUpdatedFilmSource
            .lastname(UPDATED_LASTNAME)
            .firstname(UPDATED_FIRSTNAME)
            .position(UPDATED_POSITION)
            .location(UPDATED_LOCATION);

        restFilmSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilmSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilmSource))
            )
            .andExpect(status().isOk());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
        FilmSource testFilmSource = filmSourceList.get(filmSourceList.size() - 1);
        assertThat(testFilmSource.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testFilmSource.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testFilmSource.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testFilmSource.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testFilmSource.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    @Transactional
    void fullUpdateFilmSourceWithPatch() throws Exception {
        // Initialize the database
        filmSourceRepository.saveAndFlush(filmSource);

        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();

        // Update the filmSource using partial update
        FilmSource partialUpdatedFilmSource = new FilmSource();
        partialUpdatedFilmSource.setId(filmSource.getId());

        partialUpdatedFilmSource
            .lastname(UPDATED_LASTNAME)
            .firstname(UPDATED_FIRSTNAME)
            .position(UPDATED_POSITION)
            .location(UPDATED_LOCATION)
            .rate(UPDATED_RATE);

        restFilmSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilmSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilmSource))
            )
            .andExpect(status().isOk());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
        FilmSource testFilmSource = filmSourceList.get(filmSourceList.size() - 1);
        assertThat(testFilmSource.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testFilmSource.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testFilmSource.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testFilmSource.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testFilmSource.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    @Transactional
    void patchNonExistingFilmSource() throws Exception {
        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();
        filmSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filmSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filmSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilmSource() throws Exception {
        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();
        filmSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filmSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilmSource() throws Exception {
        int databaseSizeBeforeUpdate = filmSourceRepository.findAll().size();
        filmSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmSourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filmSource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FilmSource in the database
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilmSource() throws Exception {
        // Initialize the database
        filmSourceRepository.saveAndFlush(filmSource);

        int databaseSizeBeforeDelete = filmSourceRepository.findAll().size();

        // Delete the filmSource
        restFilmSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, filmSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FilmSource> filmSourceList = filmSourceRepository.findAll();
        assertThat(filmSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
