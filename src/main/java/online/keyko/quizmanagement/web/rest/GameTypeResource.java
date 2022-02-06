package online.keyko.quizmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import online.keyko.quizmanagement.domain.GameType;
import online.keyko.quizmanagement.repository.GameTypeRepository;
import online.keyko.quizmanagement.service.GameTypeService;
import online.keyko.quizmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link online.keyko.quizmanagement.domain.GameType}.
 */
@RestController
@RequestMapping("/api")
public class GameTypeResource {

    private final Logger log = LoggerFactory.getLogger(GameTypeResource.class);

    private static final String ENTITY_NAME = "gameType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameTypeService gameTypeService;

    private final GameTypeRepository gameTypeRepository;

    public GameTypeResource(GameTypeService gameTypeService, GameTypeRepository gameTypeRepository) {
        this.gameTypeService = gameTypeService;
        this.gameTypeRepository = gameTypeRepository;
    }

    /**
     * {@code POST  /game-types} : Create a new gameType.
     *
     * @param gameType the gameType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameType, or with status {@code 400 (Bad Request)} if the gameType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-types")
    public ResponseEntity<GameType> createGameType(@Valid @RequestBody GameType gameType) throws URISyntaxException {
        log.debug("REST request to save GameType : {}", gameType);
        if (gameType.getId() != null) {
            throw new BadRequestAlertException("A new gameType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameType result = gameTypeService.save(gameType);
        return ResponseEntity
            .created(new URI("/api/game-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-types/:id} : Updates an existing gameType.
     *
     * @param id the id of the gameType to save.
     * @param gameType the gameType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameType,
     * or with status {@code 400 (Bad Request)} if the gameType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-types/{id}")
    public ResponseEntity<GameType> updateGameType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GameType gameType
    ) throws URISyntaxException {
        log.debug("REST request to update GameType : {}, {}", id, gameType);
        if (gameType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameType result = gameTypeService.save(gameType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-types/:id} : Partial updates given fields of an existing gameType, field will ignore if it is null
     *
     * @param id the id of the gameType to save.
     * @param gameType the gameType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameType,
     * or with status {@code 400 (Bad Request)} if the gameType is not valid,
     * or with status {@code 404 (Not Found)} if the gameType is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameType> partialUpdateGameType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GameType gameType
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameType partially : {}, {}", id, gameType);
        if (gameType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameType> result = gameTypeService.partialUpdate(gameType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameType.getId().toString())
        );
    }

    /**
     * {@code GET  /game-types} : get all the gameTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameTypes in body.
     */
    @GetMapping("/game-types")
    public ResponseEntity<List<GameType>> getAllGameTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of GameTypes");
        Page<GameType> page = gameTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /game-types/:id} : get the "id" gameType.
     *
     * @param id the id of the gameType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-types/{id}")
    public ResponseEntity<GameType> getGameType(@PathVariable Long id) {
        log.debug("REST request to get GameType : {}", id);
        Optional<GameType> gameType = gameTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameType);
    }

    /**
     * {@code DELETE  /game-types/:id} : delete the "id" gameType.
     *
     * @param id the id of the gameType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-types/{id}")
    public ResponseEntity<Void> deleteGameType(@PathVariable Long id) {
        log.debug("REST request to delete GameType : {}", id);
        gameTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
