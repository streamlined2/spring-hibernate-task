package com.streamlined.springhibernatetask.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TraineeDto(Long userId, String firstName, String lastName, String userName, boolean isActive,
        LocalDate dateOfBirth, String address) {

    @Override
    public String toString() {
        return "TraineeDto{userId=%d}".formatted(userId());
    }

}
