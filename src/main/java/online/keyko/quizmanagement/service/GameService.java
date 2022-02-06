package online.keyko.quizmanagement.service;

import java.util.Optional;
import online.keyko.quizmanagement.domain.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Game}.
 */
public interface GameService {
    /**
     * Save a game.
     *
     * @param game the entity to save.
     * @return the persisted entity.
     */
    Game save(Game game);

    /**
     * Partially updates a game.
     *
     * @param game the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Game> partialUpdate(Game game);

    /**
     * Get all the games.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Game> findAll(Pageable pageable);

    /**
     * Get the "id" game.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Game> findOne(Long id);

    /**
     * Delete the "id" game.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
