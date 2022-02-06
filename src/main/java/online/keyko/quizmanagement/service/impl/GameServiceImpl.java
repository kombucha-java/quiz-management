package online.keyko.quizmanagement.service.impl;

import java.util.Optional;
import online.keyko.quizmanagement.domain.Game;
import online.keyko.quizmanagement.repository.GameRepository;
import online.keyko.quizmanagement.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Game}.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game save(Game game) {
        log.debug("Request to save Game : {}", game);
        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> partialUpdate(Game game) {
        log.debug("Request to partially update Game : {}", game);

        return gameRepository
            .findById(game.getId())
            .map(existingGame -> {
                if (game.getDateTime() != null) {
                    existingGame.setDateTime(game.getDateTime());
                }
                if (game.getTheme() != null) {
                    existingGame.setTheme(game.getTheme());
                }

                return existingGame;
            })
            .map(gameRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Game> findAll(Pageable pageable) {
        log.debug("Request to get all Games");
        return gameRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Game> findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        return gameRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.deleteById(id);
    }
}
