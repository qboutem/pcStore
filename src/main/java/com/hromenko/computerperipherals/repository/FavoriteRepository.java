package com.hromenko.computerperipherals.repository;

import com.hromenko.computerperipherals.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>{
    List<Favorite> findAllByUser(String user);
}