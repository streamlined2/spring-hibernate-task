package com.streamlined.springhibernatetask.dto;

import java.time.Duration;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record TrainingDto(Long traineeId, Long trainerId, String name, Long typeId, LocalDate date, Duration duration) {

    @Override
    public String toString() {
        return "TrainingDto{traineeId=%d, trainerId=%d, name=%s, type=%d, date=%tF, duration=%s}".formatted(traineeId(),
                trainerId(), name(), typeId(), date(), duration.toString());
    }

}
