package online.keyko.quizmanagement.service.impl;

import java.util.Optional;
import online.keyko.quizmanagement.domain.Franchise;
import online.keyko.quizmanagement.repository.FranchiseRepository;
import online.keyko.quizmanagement.service.FranchiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Franchise}.
 */
@Service
@Transactional
public class FranchiseServiceImpl implements FranchiseService {

    private final Logger log = LoggerFactory.getLogger(FranchiseServiceImpl.class);

    private final FranchiseRepository franchiseRepository;

    public FranchiseServiceImpl(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    @Override
    public Franchise save(Franchise franchise) {
        log.debug("Request to save Franchise : {}", franchise);
        return franchiseRepository.save(franchise);
    }

    @Override
    public Optional<Franchise> partialUpdate(Franchise franchise) {
        log.debug("Request to partially update Franchise : {}", franchise);

        return franchiseRepository
            .findById(franchise.getId())
            .map(existingFranchise -> {
                if (franchise.getFranchiseName() != null) {
                    existingFranchise.setFranchiseName(franchise.getFranchiseName());
                }

                return existingFranchise;
            })
            .map(franchiseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Franchise> findAll(Pageable pageable) {
        log.debug("Request to get all Franchises");
        return franchiseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Franchise> findOne(Long id) {
        log.debug("Request to get Franchise : {}", id);
        return franchiseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Franchise : {}", id);
        franchiseRepository.deleteById(id);
    }
}
