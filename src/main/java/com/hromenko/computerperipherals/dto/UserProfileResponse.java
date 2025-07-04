package com.hromenko.computerperipherals.dto;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String location;
    private String birthDate;

}
