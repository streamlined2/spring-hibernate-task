package com.streamlined.springhibernatetask.entity;

import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Past;
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
@Table(name = "trainee")
public class Trainee extends User {

    @Past(message = "User birth date should belong to past")
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @Column(name = "address")
    @Length(min = 10, message = "Length of address can't less than 10")
    private String address;

    @Override
    public Long getPrimaryKey() {
        return getId();
    }

    @Override
    public boolean isIdenticalTo(EntityType<Long> entity) {
        if (entity instanceof Trainee trainee) {
            return Objects.equals(getId(), trainee.getId()) && Objects.equals(getFirstName(), trainee.getFirstName())
                    && Objects.equals(getLastName(), trainee.getLastName())
                    && Objects.equals(getUserName(), trainee.getUserName())
                    && Objects.equals(getPasswordHash(), trainee.getPasswordHash())
                    && Objects.equals(isActive(), trainee.isActive())
                    && Objects.equals(getDateOfBirth(), trainee.getDateOfBirth())
                    && Objects.equals(getAddress(), trainee.getAddress());
        }
        return false;
    }

}
