package com.streamlined.springhibernatetask.entity;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class TrainingKey implements Serializable {

    private Long trainerId;
    private Long traineeId;
    private LocalDate date;

}
