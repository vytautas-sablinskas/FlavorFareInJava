package com.vytsablinskas.flavorfare.database.repositories;

import com.vytsablinskas.flavorfare.database.domain.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Integer> { }