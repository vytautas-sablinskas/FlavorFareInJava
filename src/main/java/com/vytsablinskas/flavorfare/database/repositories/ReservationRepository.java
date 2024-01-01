package com.vytsablinskas.flavorfare.database.repositories;

import com.vytsablinskas.flavorfare.database.domain.ReservationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Integer> {
    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.table t JOIN t.restaurant rest WHERE rest.id = :restaurantId")
    List<ReservationEntity> findByRestaurantId(@Param("restaurantId") Integer restaurantId);
}