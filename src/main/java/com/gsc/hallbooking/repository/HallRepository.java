package com.gsc.hallbooking.repository;

import com.gsc.hallbooking.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByName(String name);
    List<Hall> findByActiveTrue();
    boolean existsByName(String name);
}

