package com.hromenko.computerperipherals.dto;

import lombok.Data;

@Data

public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String location;
    private String birthDate;
    private String phoneNumber;

}