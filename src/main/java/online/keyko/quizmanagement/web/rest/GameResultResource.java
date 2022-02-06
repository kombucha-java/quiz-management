package online.keyko.quizmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import online.keyko.quizmanagement.domain.GameResult;
import online.keyko.quizmanagement.repository.GameResultRepository;
import online.keyko.quizmanagement.service.GameResultService;
import online.keyko.quizmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link online.keyko.quizmanagement.domain.GameResult}.
 */
@RestController
@RequestMapping("/api")
public class GameResultResource {

    private final Logger log = LoggerFactory.getLogger(GameResultResource.class);

    private static final String ENTITY_NAME = "gameResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameResultService gameResultService;

    private final GameResultRepository gameResultRepository;

    public GameResultResource(GameResultService gameResultService, GameResultRepository gameResultRepository) {
        this.gameResultService = gameResultService;
        this.gameResultRepository = gameResultRepository;
    }

    /**
     * {@code POST  /game-results} : Create a new gameResult.
     *
     * @param gameResult the gameResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameResult, or with status {@code 400 (Bad Request)} if the gameResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-results")
    public ResponseEntity<GameResult> createGameResult(@Valid @RequestBody GameResult gameResult) throws URISyntaxException {
        log.debug("REST request to save GameResult : {}", gameResult);
        if (gameResult.getId() != null) {
            throw new BadRequestAlertException("A new gameResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameResult result = gameResultService.save(gameResult);
        return ResponseEntity
            .created(new URI("/api/game-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-results/:id} : Updates an existing gameResult.
     *
     * @param id the id of the gameResult to save.
     * @param gameResult the gameResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameResult,
     * or with status {@code 400 (Bad Request)} if the gameResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-results/{id}")
    public ResponseEntity<GameResult> updateGameResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GameResult gameResult
    ) throws URISyntaxException {
        log.debug("REST request to update GameResult : {}, {}", id, gameResult);
        if (gameResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameResult result = gameResultService.save(gameResult);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameResult.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-results/:id} : Partial updates given fields of an existing gameResult, field will ignore if it is null
     *
     * @param id the id of the gameResult to save.
     * @param gameResult the gameResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameResult,
     * or with status {@code 400 (Bad Request)} if the gameResult is not valid,
     * or with status {@code 404 (Not Found)} if the gameResult is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-results/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameResult> partialUpdateGameResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GameResult gameResult
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameResult partially : {}, {}", id, gameResult);
        if (gameResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameResult> result = gameResultService.partialUpdate(gameResult);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameResult.getId().toString())
        );
    }

    /**
     * {@code GET  /game-results} : get all the gameResults.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameResults in body.
     */
    @GetMapping("/game-results")
    public List<GameResult> getAllGameResults(@RequestParam(required = false) String filter) {
        if ("game-is-null".equals(filter)) {
            log.debug("REST request to get all GameResults where game is null");
            return gameResultService.findAllWhereGameIsNull();
        }
        log.debug("REST request to get all GameResults");
        return gameResultService.findAll();
    }

    /**
     * {@code GET  /game-results/:id} : get the "id" gameResult.
     *
     * @param id the id of the gameResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-results/{id}")
    public ResponseEntity<GameResult> getGameResult(@PathVariable Long id) {
        log.debug("REST request to get GameResult : {}", id);
        Optional<GameResult> gameResult = gameResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameResult);
    }

    /**
     * {@code DELETE  /game-results/:id} : delete the "id" gameResult.
     *
     * @param id the id of the gameResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-results/{id}")
    public ResponseEntity<Void> deleteGameResult(@PathVariable Long id) {
        log.debug("REST request to delete GameResult : {}", id);
        gameResultService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
