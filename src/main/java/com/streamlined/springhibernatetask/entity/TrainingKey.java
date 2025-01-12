package com.streamlined.springhibernatetask.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class TrainingKey {

    private Long trainerId;
    private Long traineeId;
    private LocalDate date;

}
