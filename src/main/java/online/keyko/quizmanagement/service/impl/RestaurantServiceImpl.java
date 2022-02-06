package online.keyko.quizmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import online.keyko.quizmanagement.domain.Restaurant;
import online.keyko.quizmanagement.repository.RestaurantRepository;
import online.keyko.quizmanagement.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurant}.
 */
@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        log.debug("Request to save Restaurant : {}", restaurant);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Optional<Restaurant> partialUpdate(Restaurant restaurant) {
        log.debug("Request to partially update Restaurant : {}", restaurant);

        return restaurantRepository
            .findById(restaurant.getId())
            .map(existingRestaurant -> {
                if (restaurant.getRestaurantName() != null) {
                    existingRestaurant.setRestaurantName(restaurant.getRestaurantName());
                }
                if (restaurant.getAddress() != null) {
                    existingRestaurant.setAddress(restaurant.getAddress());
                }

                return existingRestaurant;
            })
            .map(restaurantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> findAll() {
        log.debug("Request to get all Restaurants");
        return restaurantRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurant> findOne(Long id) {
        log.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
    }
}
