package online.keyko.quizmanagement.repository;

import online.keyko.quizmanagement.domain.GameType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GameType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameTypeRepository extends JpaRepository<GameType, Long> {}
