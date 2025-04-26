package com.hromenko.computerperipherals.repository;

import com.hromenko.computerperipherals.model.Peripheral;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface PeripheralRepository extends JpaRepository<Peripheral, Long> {
    List<Peripheral> findByAdditionalInfoContainingIgnoreCase(String additionalInfo, Sort sort);

    @Query(value = "SELECT p.* FROM favorite f JOIN peripheral p ON f.parent_id = p.id", nativeQuery = true)
    List<Peripheral> findAllFavoritesWithId();

    @Query(value = "SELECT p.* FROM peripheral p JOIN favorite f ON p.id = f.parent_id WHERE f.user = ?1", nativeQuery = true)
    List<Peripheral> findAllFavourites(String user);
}
