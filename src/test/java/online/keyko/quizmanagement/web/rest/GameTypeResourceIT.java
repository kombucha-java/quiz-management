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
import online.keyko.quizmanagement.domain.GameType;
import online.keyko.quizmanagement.repository.GameTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GameTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameTypeResourceIT {

    private static final String DEFAULT_GAME_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GAME_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/game-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameTypeRepository gameTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameTypeMockMvc;

    private GameType gameType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameType createEntity(EntityManager em) {
        GameType gameType = new GameType().gameTypeName(DEFAULT_GAME_TYPE_NAME);
        return gameType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameType createUpdatedEntity(EntityManager em) {
        GameType gameType = new GameType().gameTypeName(UPDATED_GAME_TYPE_NAME);
        return gameType;
    }

    @BeforeEach
    public void initTest() {
        gameType = createEntity(em);
    }

    @Test
    @Transactional
    void createGameType() throws Exception {
        int databaseSizeBeforeCreate = gameTypeRepository.findAll().size();
        // Create the GameType
        restGameTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameType)))
            .andExpect(status().isCreated());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeCreate + 1);
        GameType testGameType = gameTypeList.get(gameTypeList.size() - 1);
        assertThat(testGameType.getGameTypeName()).isEqualTo(DEFAULT_GAME_TYPE_NAME);
    }

    @Test
    @Transactional
    void createGameTypeWithExistingId() throws Exception {
        // Create the GameType with an existing ID
        gameType.setId(1L);

        int databaseSizeBeforeCreate = gameTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameType)))
            .andExpect(status().isBadRequest());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGameTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameTypeRepository.findAll().size();
        // set the field null
        gameType.setGameTypeName(null);

        // Create the GameType, which fails.

        restGameTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameType)))
            .andExpect(status().isBadRequest());

        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGameTypes() throws Exception {
        // Initialize the database
        gameTypeRepository.saveAndFlush(gameType);

        // Get all the gameTypeList
        restGameTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameType.getId().intValue())))
            .andExpect(jsonPath("$.[*].gameTypeName").value(hasItem(DEFAULT_GAME_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getGameType() throws Exception {
        // Initialize the database
        gameTypeRepository.saveAndFlush(gameType);

        // Get the gameType
        restGameTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, gameType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameType.getId().intValue()))
            .andExpect(jsonPath("$.gameTypeName").value(DEFAULT_GAME_TYPE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingGameType() throws Exception {
        // Get the gameType
        restGameTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGameType() throws Exception {
        // Initialize the database
        gameTypeRepository.saveAndFlush(gameType);

        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();

        // Update the gameType
        GameType updatedGameType = gameTypeRepository.findById(gameType.getId()).get();
        // Disconnect from session so that the updates on updatedGameType are not directly saved in db
        em.detach(updatedGameType);
        updatedGameType.gameTypeName(UPDATED_GAME_TYPE_NAME);

        restGameTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameType))
            )
            .andExpect(status().isOk());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
        GameType testGameType = gameTypeList.get(gameTypeList.size() - 1);
        assertThat(testGameType.getGameTypeName()).isEqualTo(UPDATED_GAME_TYPE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingGameType() throws Exception {
        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();
        gameType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameType() throws Exception {
        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();
        gameType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameType() throws Exception {
        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();
        gameType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameTypeWithPatch() throws Exception {
        // Initialize the database
        gameTypeRepository.saveAndFlush(gameType);

        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();

        // Update the gameType using partial update
        GameType partialUpdatedGameType = new GameType();
        partialUpdatedGameType.setId(gameType.getId());

        partialUpdatedGameType.gameTypeName(UPDATED_GAME_TYPE_NAME);

        restGameTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameType))
            )
            .andExpect(status().isOk());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
        GameType testGameType = gameTypeList.get(gameTypeList.size() - 1);
        assertThat(testGameType.getGameTypeName()).isEqualTo(UPDATED_GAME_TYPE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateGameTypeWithPatch() throws Exception {
        // Initialize the database
        gameTypeRepository.saveAndFlush(gameType);

        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();

        // Update the gameType using partial update
        GameType partialUpdatedGameType = new GameType();
        partialUpdatedGameType.setId(gameType.getId());

        partialUpdatedGameType.gameTypeName(UPDATED_GAME_TYPE_NAME);

        restGameTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameType))
            )
            .andExpect(status().isOk());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
        GameType testGameType = gameTypeList.get(gameTypeList.size() - 1);
        assertThat(testGameType.getGameTypeName()).isEqualTo(UPDATED_GAME_TYPE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingGameType() throws Exception {
        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();
        gameType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameType() throws Exception {
        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();
        gameType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameType))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameType() throws Exception {
        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();
        gameType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameType in the database
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameType() throws Exception {
        // Initialize the database
        gameTypeRepository.saveAndFlush(gameType);

        int databaseSizeBeforeDelete = gameTypeRepository.findAll().size();

        // Delete the gameType
        restGameTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameType> gameTypeList = gameTypeRepository.findAll();
        assertThat(gameTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
