package online.keyko.quizmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import online.keyko.quizmanagement.IntegrationTest;
import online.keyko.quizmanagement.domain.Franchise;
import online.keyko.quizmanagement.repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FranchiseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FranchiseResourceIT {

    private static final String DEFAULT_FRANCHISE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FRANCHISE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/franchises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFranchiseMockMvc;

    private Franchise franchise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Franchise createEntity(EntityManager em) {
        Franchise franchise = new Franchise().franchiseName(DEFAULT_FRANCHISE_NAME);
        return franchise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Franchise createUpdatedEntity(EntityManager em) {
        Franchise franchise = new Franchise().franchiseName(UPDATED_FRANCHISE_NAME);
        return franchise;
    }

    @BeforeEach
    public void initTest() {
        franchise = createEntity(em);
    }

    @Test
    @Transactional
    void createFranchise() throws Exception {
        int databaseSizeBeforeCreate = franchiseRepository.findAll().size();
        // Create the Franchise
        restFranchiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(franchise)))
            .andExpect(status().isCreated());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeCreate + 1);
        Franchise testFranchise = franchiseList.get(franchiseList.size() - 1);
        assertThat(testFranchise.getFranchiseName()).isEqualTo(DEFAULT_FRANCHISE_NAME);
    }

    @Test
    @Transactional
    void createFranchiseWithExistingId() throws Exception {
        // Create the Franchise with an existing ID
        franchise.setId(1L);

        int databaseSizeBeforeCreate = franchiseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFranchiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(franchise)))
            .andExpect(status().isBadRequest());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFranchiseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = franchiseRepository.findAll().size();
        // set the field null
        franchise.setFranchiseName(null);

        // Create the Franchise, which fails.

        restFranchiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(franchise)))
            .andExpect(status().isBadRequest());

        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFranchises() throws Exception {
        // Initialize the database
        franchiseRepository.saveAndFlush(franchise);

        // Get all the franchiseList
        restFranchiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(franchise.getId().intValue())))
            .andExpect(jsonPath("$.[*].franchiseName").value(hasItem(DEFAULT_FRANCHISE_NAME)));
    }

    @Test
    @Transactional
    void getFranchise() throws Exception {
        // Initialize the database
        franchiseRepository.saveAndFlush(franchise);

        // Get the franchise
        restFranchiseMockMvc
            .perform(get(ENTITY_API_URL_ID, franchise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(franchise.getId().intValue()))
            .andExpect(jsonPath("$.franchiseName").value(DEFAULT_FRANCHISE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingFranchise() throws Exception {
        // Get the franchise
        restFranchiseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFranchise() throws Exception {
        // Initialize the database
        franchiseRepository.saveAndFlush(franchise);

        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();

        // Update the franchise
        Franchise updatedFranchise = franchiseRepository.findById(franchise.getId()).get();
        // Disconnect from session so that the updates on updatedFranchise are not directly saved in db
        em.detach(updatedFranchise);
        updatedFranchise.franchiseName(UPDATED_FRANCHISE_NAME);

        restFranchiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFranchise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFranchise))
            )
            .andExpect(status().isOk());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
        Franchise testFranchise = franchiseList.get(franchiseList.size() - 1);
        assertThat(testFranchise.getFranchiseName()).isEqualTo(UPDATED_FRANCHISE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFranchise() throws Exception {
        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();
        franchise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFranchiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, franchise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(franchise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFranchise() throws Exception {
        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();
        franchise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFranchiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(franchise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFranchise() throws Exception {
        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();
        franchise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFranchiseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(franchise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFranchiseWithPatch() throws Exception {
        // Initialize the database
        franchiseRepository.saveAndFlush(franchise);

        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();

        // Update the franchise using partial update
        Franchise partialUpdatedFranchise = new Franchise();
        partialUpdatedFranchise.setId(franchise.getId());

        partialUpdatedFranchise.franchiseName(UPDATED_FRANCHISE_NAME);

        restFranchiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFranchise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFranchise))
            )
            .andExpect(status().isOk());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
        Franchise testFranchise = franchiseList.get(franchiseList.size() - 1);
        assertThat(testFranchise.getFranchiseName()).isEqualTo(UPDATED_FRANCHISE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFranchiseWithPatch() throws Exception {
        // Initialize the database
        franchiseRepository.saveAndFlush(franchise);

        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();

        // Update the franchise using partial update
        Franchise partialUpdatedFranchise = new Franchise();
        partialUpdatedFranchise.setId(franchise.getId());

        partialUpdatedFranchise.franchiseName(UPDATED_FRANCHISE_NAME);

        restFranchiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFranchise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFranchise))
            )
            .andExpect(status().isOk());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
        Franchise testFranchise = franchiseList.get(franchiseList.size() - 1);
        assertThat(testFranchise.getFranchiseName()).isEqualTo(UPDATED_FRANCHISE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFranchise() throws Exception {
        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();
        franchise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFranchiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, franchise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(franchise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFranchise() throws Exception {
        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();
        franchise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFranchiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(franchise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFranchise() throws Exception {
        int databaseSizeBeforeUpdate = franchiseRepository.findAll().size();
        franchise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFranchiseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(franchise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Franchise in the database
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFranchise() throws Exception {
        // Initialize the database
        franchiseRepository.saveAndFlush(franchise);

        int databaseSizeBeforeDelete = franchiseRepository.findAll().size();

        // Delete the franchise
        restFranchiseMockMvc
            .perform(delete(ENTITY_API_URL_ID, franchise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Franchise> franchiseList = franchiseRepository.findAll();
        assertThat(franchiseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
