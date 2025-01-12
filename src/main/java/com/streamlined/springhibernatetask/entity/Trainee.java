package com.streamlined.springhibernatetask.entity;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.annotation.Nullable;
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
    private @Nullable LocalDate dateOfBirth;

    @Column(name = "address")
    @Length(min = 10, message = "Length of address can't less than 10")
    private @Nullable String address;

}
