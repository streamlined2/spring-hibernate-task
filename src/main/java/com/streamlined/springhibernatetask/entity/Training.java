package com.streamlined.springhibernatetask.entity;

import java.time.Duration;
import java.time.LocalDate;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "training", uniqueConstraints = @UniqueConstraint(columnNames = { "trainee_id", "trainer_id", "date" }))
@IdClass(TrainingKey.class)
public class Training {

    @EqualsAndHashCode.Include
    @ToString.Include
    @JoinColumn(name = "trainee_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Id
    private Trainee traineeId;

    @EqualsAndHashCode.Include
    @ToString.Include
    @JoinColumn(name = "trainer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Id
    private Trainer trainerId;

    @NotBlank(message = "Training name should not be blank")
    @NotNull(message = "Training name should not be null")
    @Size(min = 3, message = "Training name should not be shorter than 3 characters")
    @NaturalId
    @ToString.Include
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ToString.Include
    @JoinColumn(name = "type_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private TrainingType type;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    @Id
    private LocalDate date;

    @ToString.Include
    @Column(name = "duration", nullable = false)
    private Duration duration;

}
