package com.streamlined.springhibernatetask.dto;

import java.time.Duration;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record TrainingDto(Long id, Long traineeId, Long trainerId, String name, Long typeId, LocalDate date,
        Duration duration) {

    @Override
    public String toString() {
        return "TrainingDto{id=%d, traineeId=%d, trainerId=%d, name=%s, type=%d, date=%tF, duration=%s}".formatted(id(),
                traineeId(), trainerId(), name(), typeId(), date(), duration.toString());
    }

}
