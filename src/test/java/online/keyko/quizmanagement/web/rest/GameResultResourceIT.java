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
import online.keyko.quizmanagement.domain.GameResult;
import online.keyko.quizmanagement.repository.GameResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link GameResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameResultResourceIT {

    private static final Integer DEFAULT_PLACE = 0;
    private static final Integer UPDATED_PLACE = 1;

    private static final Integer DEFAULT_POINTS = 0;
    private static final Integer UPDATED_POINTS = 1;

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final byte[] DEFAULT_TABLE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TABLE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TABLE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TABLE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/game-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameResultMockMvc;

    private GameResult gameResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameResult createEntity(EntityManager em) {
        GameResult gameResult = new GameResult()
            .place(DEFAULT_PLACE)
            .points(DEFAULT_POINTS)
            .link(DEFAULT_LINK)
            .table(DEFAULT_TABLE)
            .tableContentType(DEFAULT_TABLE_CONTENT_TYPE);
        return gameResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameResult createUpdatedEntity(EntityManager em) {
        GameResult gameResult = new GameResult()
            .place(UPDATED_PLACE)
            .points(UPDATED_POINTS)
            .link(UPDATED_LINK)
            .table(UPDATED_TABLE)
            .tableContentType(UPDATED_TABLE_CONTENT_TYPE);
        return gameResult;
    }

    @BeforeEach
    public void initTest() {
        gameResult = createEntity(em);
    }

    @Test
    @Transactional
    void createGameResult() throws Exception {
        int databaseSizeBeforeCreate = gameResultRepository.findAll().size();
        // Create the GameResult
        restGameResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameResult)))
            .andExpect(status().isCreated());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeCreate + 1);
        GameResult testGameResult = gameResultList.get(gameResultList.size() - 1);
        assertThat(testGameResult.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testGameResult.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testGameResult.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testGameResult.getTable()).isEqualTo(DEFAULT_TABLE);
        assertThat(testGameResult.getTableContentType()).isEqualTo(DEFAULT_TABLE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createGameResultWithExistingId() throws Exception {
        // Create the GameResult with an existing ID
        gameResult.setId(1L);

        int databaseSizeBeforeCreate = gameResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameResult)))
            .andExpect(status().isBadRequest());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameResultRepository.findAll().size();
        // set the field null
        gameResult.setPlace(null);

        // Create the GameResult, which fails.

        restGameResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameResult)))
            .andExpect(status().isBadRequest());

        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameResultRepository.findAll().size();
        // set the field null
        gameResult.setPoints(null);

        // Create the GameResult, which fails.

        restGameResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameResult)))
            .andExpect(status().isBadRequest());

        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGameResults() throws Exception {
        // Initialize the database
        gameResultRepository.saveAndFlush(gameResult);

        // Get all the gameResultList
        restGameResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].tableContentType").value(hasItem(DEFAULT_TABLE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].table").value(hasItem(Base64Utils.encodeToString(DEFAULT_TABLE))));
    }

    @Test
    @Transactional
    void getGameResult() throws Exception {
        // Initialize the database
        gameResultRepository.saveAndFlush(gameResult);

        // Get the gameResult
        restGameResultMockMvc
            .perform(get(ENTITY_API_URL_ID, gameResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameResult.getId().intValue()))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.tableContentType").value(DEFAULT_TABLE_CONTENT_TYPE))
            .andExpect(jsonPath("$.table").value(Base64Utils.encodeToString(DEFAULT_TABLE)));
    }

    @Test
    @Transactional
    void getNonExistingGameResult() throws Exception {
        // Get the gameResult
        restGameResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGameResult() throws Exception {
        // Initialize the database
        gameResultRepository.saveAndFlush(gameResult);

        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();

        // Update the gameResult
        GameResult updatedGameResult = gameResultRepository.findById(gameResult.getId()).get();
        // Disconnect from session so that the updates on updatedGameResult are not directly saved in db
        em.detach(updatedGameResult);
        updatedGameResult
            .place(UPDATED_PLACE)
            .points(UPDATED_POINTS)
            .link(UPDATED_LINK)
            .table(UPDATED_TABLE)
            .tableContentType(UPDATED_TABLE_CONTENT_TYPE);

        restGameResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameResult))
            )
            .andExpect(status().isOk());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
        GameResult testGameResult = gameResultList.get(gameResultList.size() - 1);
        assertThat(testGameResult.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testGameResult.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testGameResult.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testGameResult.getTable()).isEqualTo(UPDATED_TABLE);
        assertThat(testGameResult.getTableContentType()).isEqualTo(UPDATED_TABLE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingGameResult() throws Exception {
        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();
        gameResult.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameResult() throws Exception {
        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();
        gameResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameResult() throws Exception {
        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();
        gameResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameResult)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameResultWithPatch() throws Exception {
        // Initialize the database
        gameResultRepository.saveAndFlush(gameResult);

        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();

        // Update the gameResult using partial update
        GameResult partialUpdatedGameResult = new GameResult();
        partialUpdatedGameResult.setId(gameResult.getId());

        partialUpdatedGameResult
            .place(UPDATED_PLACE)
            .points(UPDATED_POINTS)
            .table(UPDATED_TABLE)
            .tableContentType(UPDATED_TABLE_CONTENT_TYPE);

        restGameResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameResult))
            )
            .andExpect(status().isOk());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
        GameResult testGameResult = gameResultList.get(gameResultList.size() - 1);
        assertThat(testGameResult.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testGameResult.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testGameResult.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testGameResult.getTable()).isEqualTo(UPDATED_TABLE);
        assertThat(testGameResult.getTableContentType()).isEqualTo(UPDATED_TABLE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateGameResultWithPatch() throws Exception {
        // Initialize the database
        gameResultRepository.saveAndFlush(gameResult);

        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();

        // Update the gameResult using partial update
        GameResult partialUpdatedGameResult = new GameResult();
        partialUpdatedGameResult.setId(gameResult.getId());

        partialUpdatedGameResult
            .place(UPDATED_PLACE)
            .points(UPDATED_POINTS)
            .link(UPDATED_LINK)
            .table(UPDATED_TABLE)
            .tableContentType(UPDATED_TABLE_CONTENT_TYPE);

        restGameResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameResult))
            )
            .andExpect(status().isOk());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
        GameResult testGameResult = gameResultList.get(gameResultList.size() - 1);
        assertThat(testGameResult.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testGameResult.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testGameResult.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testGameResult.getTable()).isEqualTo(UPDATED_TABLE);
        assertThat(testGameResult.getTableContentType()).isEqualTo(UPDATED_TABLE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingGameResult() throws Exception {
        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();
        gameResult.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameResult() throws Exception {
        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();
        gameResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameResult() throws Exception {
        int databaseSizeBeforeUpdate = gameResultRepository.findAll().size();
        gameResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameResult))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameResult in the database
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameResult() throws Exception {
        // Initialize the database
        gameResultRepository.saveAndFlush(gameResult);

        int databaseSizeBeforeDelete = gameResultRepository.findAll().size();

        // Delete the gameResult
        restGameResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameResult> gameResultList = gameResultRepository.findAll();
        assertThat(gameResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
