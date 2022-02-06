package online.keyko.quizmanagement.service.impl;

import java.util.Optional;
import online.keyko.quizmanagement.domain.Player;
import online.keyko.quizmanagement.repository.PlayerRepository;
import online.keyko.quizmanagement.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Player}.
 */
@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player save(Player player) {
        log.debug("Request to save Player : {}", player);
        return playerRepository.save(player);
    }

    @Override
    public Optional<Player> partialUpdate(Player player) {
        log.debug("Request to partially update Player : {}", player);

        return playerRepository
            .findById(player.getId())
            .map(existingPlayer -> {
                if (player.getNickName() != null) {
                    existingPlayer.setNickName(player.getNickName());
                }

                return existingPlayer;
            })
            .map(playerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Player> findAll(Pageable pageable) {
        log.debug("Request to get all Players");
        return playerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> findOne(Long id) {
        log.debug("Request to get Player : {}", id);
        return playerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Player : {}", id);
        playerRepository.deleteById(id);
    }
}
