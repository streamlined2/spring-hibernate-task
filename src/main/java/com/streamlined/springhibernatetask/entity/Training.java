package com.streamlined.springhibernatetask.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

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
import jakarta.validation.constraints.Pattern;
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
public class Training implements EntityType<TrainingKey> {

    @EqualsAndHashCode.Include
    @ToString.Include
    @JoinColumn(name = "trainee_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Id
    private Trainee trainee;

    @EqualsAndHashCode.Include
    @ToString.Include
    @JoinColumn(name = "trainer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Id
    private Trainer trainer;

    @NotBlank(message = "Training name should not be blank")
    @NotNull(message = "Training name should not be null")
    @Size(min = 3, message = "Training name should not be shorter than 3 characters")
    @Pattern(regexp = "^\\p{Upper}\\p{Lower}*$", message = "Training name should start with capital letter and the rest should be lowercase")
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

    @Override
    public TrainingKey getPrimaryKey() {
        return TrainingKey.of(trainer.getId(), trainee.getId(), date);
    }

    @Override
    public boolean isIdenticalTo(EntityType<TrainingKey> entity) {
        if (entity instanceof Training training) {
            return Objects.equals(getTrainee(), training.getTrainee())
                    && Objects.equals(getTrainer(), training.getTrainer())
                    && Objects.equals(getName(), training.getName()) && Objects.equals(getType(), training.getType())
                    && Objects.equals(getDate(), training.getDate())
                    && Objects.equals(getDuration(), training.getDuration());
        }
        return false;
    }

}
