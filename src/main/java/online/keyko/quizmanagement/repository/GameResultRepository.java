package online.keyko.quizmanagement.repository;

import online.keyko.quizmanagement.domain.GameResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GameResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {}
