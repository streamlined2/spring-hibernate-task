package com.streamlined.springhibernatetask.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "trainer")
public class Trainer extends User {

    @JoinColumn(name = "specialization", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull(message = "Trainer specialization can't be null")
    private TrainingType specialization;

    @Override
    public Long getPrimaryKey() {
        return getUserId();
    }

    @Override
    public boolean isIdenticalTo(EntityType<Long> entity) {
        if (entity instanceof Trainer trainer) {
            return Objects.equals(getUserId(), trainer.getUserId())
                    && Objects.equals(getFirstName(), trainer.getFirstName())
                    && Objects.equals(getLastName(), trainer.getLastName())
                    && Objects.equals(getUserName(), trainer.getUserName())
                    && Objects.equals(getPasswordHash(), trainer.getPasswordHash())
                    && Objects.equals(isActive(), trainer.isActive())
                    && Objects.equals(getSpecialization(), trainer.getSpecialization());
        }
        return false;
    }

}
