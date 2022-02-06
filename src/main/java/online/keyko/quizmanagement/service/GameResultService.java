package online.keyko.quizmanagement.service;

import java.util.List;
import java.util.Optional;
import online.keyko.quizmanagement.domain.GameResult;

/**
 * Service Interface for managing {@link GameResult}.
 */
public interface GameResultService {
    /**
     * Save a gameResult.
     *
     * @param gameResult the entity to save.
     * @return the persisted entity.
     */
    GameResult save(GameResult gameResult);

    /**
     * Partially updates a gameResult.
     *
     * @param gameResult the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GameResult> partialUpdate(GameResult gameResult);

    /**
     * Get all the gameResults.
     *
     * @return the list of entities.
     */
    List<GameResult> findAll();
    /**
     * Get all the GameResult where Game is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<GameResult> findAllWhereGameIsNull();

    /**
     * Get the "id" gameResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameResult> findOne(Long id);

    /**
     * Delete the "id" gameResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
