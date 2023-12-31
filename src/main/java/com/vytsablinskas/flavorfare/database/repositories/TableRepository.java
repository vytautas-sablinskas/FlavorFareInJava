package com.vytsablinskas.flavorfare.database.repositories;

import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableRepository extends CrudRepository<TableEntity, Integer> {
    @Query("SELECT t FROM TableEntity t WHERE t.restaurant.id = :restaurantId")
    List<TableEntity> findByCondition(@Param("restaurantId") Integer restaurantId);
}