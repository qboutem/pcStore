package com.hromenko.computerperipherals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private Long parentId;

}
