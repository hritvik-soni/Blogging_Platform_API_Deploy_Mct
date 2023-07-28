package com.hritvik.BloggingPlatformAPI.model.dto;

import com.hritvik.BloggingPlatformAPI.model.enums.Gender;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRequest {
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "firstname is required")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email",unique = true)
    @Email(message = "Invalid email")
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "phone_number")
    @Pattern(regexp = "\\+\\d{2}-\\d{10}", message = "Phone number must be in the format +91-xxxxxxxxxx")
    private String phoneNumber;

    @Column(name = "password")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
}
