package com.vytsablinskas.flavorfare.database.repositories;

import com.vytsablinskas.flavorfare.database.domain.RestaurantEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends CrudRepository<RestaurantEntity, Integer> { }