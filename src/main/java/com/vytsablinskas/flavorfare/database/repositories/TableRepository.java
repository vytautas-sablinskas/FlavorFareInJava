package com.vytsablinskas.flavorfare.database.repositories;

import com.vytsablinskas.flavorfare.database.domain.TableEntity;
import org.springframework.data.repository.CrudRepository;

public interface TableRepository extends CrudRepository<TableEntity, Integer> { }
