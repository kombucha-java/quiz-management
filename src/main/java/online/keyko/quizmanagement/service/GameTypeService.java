package online.keyko.quizmanagement.service;

import java.util.Optional;
import online.keyko.quizmanagement.domain.GameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link GameType}.
 */
public interface GameTypeService {
    /**
     * Save a gameType.
     *
     * @param gameType the entity to save.
     * @return the persisted entity.
     */
    GameType save(GameType gameType);

    /**
     * Partially updates a gameType.
     *
     * @param gameType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameType> partialUpdate(GameType gameType);

    /**
     * Get all the gameTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GameType> findAll(Pageable pageable);

    /**
     * Get the "id" gameType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameType> findOne(Long id);

    /**
     * Delete the "id" gameType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
