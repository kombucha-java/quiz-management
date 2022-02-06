package online.keyko.quizmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import online.keyko.quizmanagement.domain.Franchise;
import online.keyko.quizmanagement.repository.FranchiseRepository;
import online.keyko.quizmanagement.service.FranchiseService;
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
 * REST controller for managing {@link online.keyko.quizmanagement.domain.Franchise}.
 */
@RestController
@RequestMapping("/api")
public class FranchiseResource {

    private final Logger log = LoggerFactory.getLogger(FranchiseResource.class);

    private static final String ENTITY_NAME = "franchise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FranchiseService franchiseService;

    private final FranchiseRepository franchiseRepository;

    public FranchiseResource(FranchiseService franchiseService, FranchiseRepository franchiseRepository) {
        this.franchiseService = franchiseService;
        this.franchiseRepository = franchiseRepository;
    }

    /**
     * {@code POST  /franchises} : Create a new franchise.
     *
     * @param franchise the franchise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new franchise, or with status {@code 400 (Bad Request)} if the franchise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/franchises")
    public ResponseEntity<Franchise> createFranchise(@Valid @RequestBody Franchise franchise) throws URISyntaxException {
        log.debug("REST request to save Franchise : {}", franchise);
        if (franchise.getId() != null) {
            throw new BadRequestAlertException("A new franchise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Franchise result = franchiseService.save(franchise);
        return ResponseEntity
            .created(new URI("/api/franchises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /franchises/:id} : Updates an existing franchise.
     *
     * @param id the id of the franchise to save.
     * @param franchise the franchise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated franchise,
     * or with status {@code 400 (Bad Request)} if the franchise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the franchise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/franchises/{id}")
    public ResponseEntity<Franchise> updateFranchise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Franchise franchise
    ) throws URISyntaxException {
        log.debug("REST request to update Franchise : {}, {}", id, franchise);
        if (franchise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, franchise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!franchiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Franchise result = franchiseService.save(franchise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, franchise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /franchises/:id} : Partial updates given fields of an existing franchise, field will ignore if it is null
     *
     * @param id the id of the franchise to save.
     * @param franchise the franchise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated franchise,
     * or with status {@code 400 (Bad Request)} if the franchise is not valid,
     * or with status {@code 404 (Not Found)} if the franchise is not found,
     * or with status {@code 500 (Internal Server Error)} if the franchise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/franchises/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Franchise> partialUpdateFranchise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Franchise franchise
    ) throws URISyntaxException {
        log.debug("REST request to partial update Franchise partially : {}, {}", id, franchise);
        if (franchise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, franchise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!franchiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Franchise> result = franchiseService.partialUpdate(franchise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, franchise.getId().toString())
        );
    }

    /**
     * {@code GET  /franchises} : get all the franchises.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of franchises in body.
     */
    @GetMapping("/franchises")
    public ResponseEntity<List<Franchise>> getAllFranchises(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Franchises");
        Page<Franchise> page = franchiseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /franchises/:id} : get the "id" franchise.
     *
     * @param id the id of the franchise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the franchise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/franchises/{id}")
    public ResponseEntity<Franchise> getFranchise(@PathVariable Long id) {
        log.debug("REST request to get Franchise : {}", id);
        Optional<Franchise> franchise = franchiseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(franchise);
    }

    /**
     * {@code DELETE  /franchises/:id} : delete the "id" franchise.
     *
     * @param id the id of the franchise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/franchises/{id}")
    public ResponseEntity<Void> deleteFranchise(@PathVariable Long id) {
        log.debug("REST request to delete Franchise : {}", id);
        franchiseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
