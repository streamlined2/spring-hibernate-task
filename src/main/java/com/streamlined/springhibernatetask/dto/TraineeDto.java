package com.streamlined.springhibernatetask.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TraineeDto(
        Long id,
        
        @NotBlank(message = "User first name should not be blank") 
        @NotNull(message = "User first name should not be null") 
        @Size(min = 3, message = "User first name should not be shorter than 3 characters") 
        @Pattern(regexp = "^\\p{Upper}\\p{Lower}*$", message = "First name should start with capital letter and the rest should be lowercase") 
        String firstName,
        
        @NotBlank(message = "User first name should not be blank") 
        @NotNull(message = "User first name should not be null") 
        @Size(min = 3, message = "User last name should not be shorter than 3 characters") 
        @Pattern(regexp = "^\\p{Upper}\\p{Lower}*$", message = "Last name should start with capital letter and the rest should be lowercase") 
        String lastName,
        
        @Pattern(regexp = "^(\\p{Upper}\\p{Lower}*)\\.(\\p{Upper}\\p{Lower}*)\\d*$", message = "User name should consist of first and last name separated by dot and ending with number") 
        String userName, 
        
        boolean isActive,
        
        @Past(message = "User birth date should belong to past") 
        LocalDate dateOfBirth,
        
        @Length(min = 10, message = "Length of address can't less than 10") 
        String address) {

    @Override
    public String toString() {
        return "TraineeDto{userId=%d}".formatted(id());
    }

}
