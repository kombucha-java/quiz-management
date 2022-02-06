package online.keyko.quizmanagement.service.impl;

import java.util.Optional;
import online.keyko.quizmanagement.domain.GameType;
import online.keyko.quizmanagement.repository.GameTypeRepository;
import online.keyko.quizmanagement.service.GameTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GameType}.
 */
@Service
@Transactional
public class GameTypeServiceImpl implements GameTypeService {

    private final Logger log = LoggerFactory.getLogger(GameTypeServiceImpl.class);

    private final GameTypeRepository gameTypeRepository;

    public GameTypeServiceImpl(GameTypeRepository gameTypeRepository) {
        this.gameTypeRepository = gameTypeRepository;
    }

    @Override
    public GameType save(GameType gameType) {
        log.debug("Request to save GameType : {}", gameType);
        return gameTypeRepository.save(gameType);
    }

    @Override
    public Optional<GameType> partialUpdate(GameType gameType) {
        log.debug("Request to partially update GameType : {}", gameType);

        return gameTypeRepository
            .findById(gameType.getId())
            .map(existingGameType -> {
                if (gameType.getGameTypeName() != null) {
                    existingGameType.setGameTypeName(gameType.getGameTypeName());
                }

                return existingGameType;
            })
            .map(gameTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameType> findAll(Pageable pageable) {
        log.debug("Request to get all GameTypes");
        return gameTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameType> findOne(Long id) {
        log.debug("Request to get GameType : {}", id);
        return gameTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GameType : {}", id);
        gameTypeRepository.deleteById(id);
    }
}
