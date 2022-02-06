package online.keyko.quizmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import online.keyko.quizmanagement.domain.GameResult;
import online.keyko.quizmanagement.repository.GameResultRepository;
import online.keyko.quizmanagement.service.GameResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GameResult}.
 */
@Service
@Transactional
public class GameResultServiceImpl implements GameResultService {

    private final Logger log = LoggerFactory.getLogger(GameResultServiceImpl.class);

    private final GameResultRepository gameResultRepository;

    public GameResultServiceImpl(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    @Override
    public GameResult save(GameResult gameResult) {
        log.debug("Request to save GameResult : {}", gameResult);
        return gameResultRepository.save(gameResult);
    }

    @Override
    public Optional<GameResult> partialUpdate(GameResult gameResult) {
        log.debug("Request to partially update GameResult : {}", gameResult);

        return gameResultRepository
            .findById(gameResult.getId())
            .map(existingGameResult -> {
                if (gameResult.getPlace() != null) {
                    existingGameResult.setPlace(gameResult.getPlace());
                }
                if (gameResult.getPoints() != null) {
                    existingGameResult.setPoints(gameResult.getPoints());
                }
                if (gameResult.getLink() != null) {
                    existingGameResult.setLink(gameResult.getLink());
                }
                if (gameResult.getTable() != null) {
                    existingGameResult.setTable(gameResult.getTable());
                }
                if (gameResult.getTableContentType() != null) {
                    existingGameResult.setTableContentType(gameResult.getTableContentType());
                }

                return existingGameResult;
            })
            .map(gameResultRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameResult> findAll() {
        log.debug("Request to get all GameResults");
        return gameResultRepository.findAll();
    }

    /**
     *  Get all the gameResults where Game is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GameResult> findAllWhereGameIsNull() {
        log.debug("Request to get all gameResults where Game is null");
        return StreamSupport
            .stream(gameResultRepository.findAll().spliterator(), false)
            .filter(gameResult -> gameResult.getGame() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameResult> findOne(Long id) {
        log.debug("Request to get GameResult : {}", id);
        return gameResultRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GameResult : {}", id);
        gameResultRepository.deleteById(id);
    }
}
