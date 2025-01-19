package com.streamlined.springhibernatetask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TrainingTypeDto(
        Long id, 
        
        @NotBlank(message = "Training type name should not be blank")
        @NotNull(message = "Training type name should not be null")
        @Size(min = 3, message = "Training type name should not be shorter than 3 characters")
        @Pattern(regexp = "^\\p{Upper}\\p{Lower}*$", message = "Training type name should start with capital letter and the rest should be lowercase")
        String name) {
}
