package com.hromenko.computerperipherals.controller;

import com.hromenko.computerperipherals.dto.FavoriteRequest;
import com.hromenko.computerperipherals.dto.FavoriteResponse;
import com.hromenko.computerperipherals.model.Favorite;
import com.hromenko.computerperipherals.model.Peripheral;
import com.hromenko.computerperipherals.repository.FavoriteRepository;
import com.hromenko.computerperipherals.repository.PeripheralRepository;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping()
@Registered
public class PeripheralController {
    private final PeripheralRepository peripheralRepository;
    private final FavoriteRepository favoriteRepository;

    @GetMapping("/getAllPeripheral")
    public ResponseEntity<List<Peripheral>> getAllPeripheral(
            @RequestParam(defaultValue = "additionalInfo") String sortBy,
            @RequestParam(required = false) String additionalInfo
    ) {
        List<String> allowedSortFields = List.of("additionalInfo", "price");
        boolean desc = false;

        if (sortBy.startsWith("-")) {
            desc = true;
            sortBy = sortBy.substring(1);
        }

        if (!allowedSortFields.contains(sortBy)) {
            return ResponseEntity.badRequest().build();
        }

        Sort sort = desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        List<Peripheral> result;

        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            result = peripheralRepository.findByAdditionalInfoContainingIgnoreCase(additionalInfo, sort);
        } else {
            result = peripheralRepository.findAll(sort);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/favorites")
    public Favorite addFavorite(@RequestBody FavoriteRequest request) {
        Peripheral peripheral = peripheralRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Peripheral not found"));

        Favorite favorite = new Favorite();
        favorite.setParentId(peripheral);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getDetails());
        favorite.setUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return favoriteRepository.save(favorite);
    }

    @GetMapping("/favoritesPeripheral")
    public ResponseEntity<List<Peripheral>> getAllFavoritesPeripheral() {
        List<Peripheral> favorites = peripheralRepository.findAllFavourites(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/favorites")
    public List<FavoriteResponse> getFavorites() {
        List<Favorite> favorites = favoriteRepository.findAllByUser(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        List<FavoriteResponse> response = favorites.stream()
                .map(fav -> new FavoriteResponse(fav.getId(), fav.getParentId().getId()))
                .collect(Collectors.toList());

        return response;
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        if (!favoriteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        favoriteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peripheral> getPeripheral(@PathVariable Long id) {
         Peripheral peripheral=peripheralRepository.findById(id).orElseThrow(() -> new RuntimeException("Peripheral not found"));
         return ResponseEntity.ok(peripheral);
    }
}

