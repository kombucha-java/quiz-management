package online.keyko.quizmanagement.repository;

import online.keyko.quizmanagement.domain.Franchise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Franchise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, Long> {}
