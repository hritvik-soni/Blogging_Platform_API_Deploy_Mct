package com.hritvik.BloggingPlatformAPI.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hritvik.BloggingPlatformAPI.model.enums.Gender;
import com.hritvik.BloggingPlatformAPI.service.utility.dataMasking.MaskData;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;


    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "firstname is required")
    private String firstName;


    @Column(name = "last_name")
    private String lastName;


    @Column(name = "email",unique = true)
    private String email;


    @Column(name = "date_of_birth") // 2023-07-23
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @MaskData
    @Column(name = "phone_number")
    @Pattern(regexp = "\\+\\d{2}-\\d{10}", message = "Phone number must be in the format +91-xxxxxxxxxx")
    private String phoneNumber;


    @Column(name = "password")
    @MaskData
    private String password;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt ;



}