package com.streamlined.springhibernatetask.entity;

import java.time.Duration;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "training")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ToString.Include
    @JoinColumn(name = "trainee_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Trainee trainee;

    @ToString.Include
    @JoinColumn(name = "trainer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Trainer trainer;

    @ToString.Include
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ToString.Include
    @JoinColumn(name = "type_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private TrainingType type;

    @ToString.Include
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ToString.Include
    @Column(name = "duration", nullable = false)
    private Duration duration;

}
