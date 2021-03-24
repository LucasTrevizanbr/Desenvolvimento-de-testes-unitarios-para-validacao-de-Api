package com.github.trevizanlucas.beerapi.repository;

import com.github.trevizanlucas.beerapi.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeerRepository extends JpaRepository<Beer, Long> {
    Optional<Beer> findByName(String name);
}
