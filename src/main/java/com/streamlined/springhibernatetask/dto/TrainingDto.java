package com.streamlined.springhibernatetask.dto;

import java.time.Duration;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TrainingDto(
        Long id, 
                
        Long traineeId, 
        
        Long trainerId, 
        
        @NotBlank(message = "Training name should not be blank")
        @NotNull(message = "Training name should not be null")
        @Size(min = 3, message = "Training name should not be shorter than 3 characters")
        @Pattern(regexp = "^\\p{Upper}\\p{Lower}*$", message = "Training name should start with capital letter and the rest should be lowercase")
        String name, 
        
        Long typeId, 
        
        LocalDate date,
        
        Duration duration) {

    @Override
    public String toString() {
        return "TrainingDto{id=%d, traineeId=%d, trainerId=%d, name=%s, type=%d, date=%tF, duration=%s}".formatted(id(),
                traineeId(), trainerId(), name(), typeId(), date(), duration.toString());
    }

}
