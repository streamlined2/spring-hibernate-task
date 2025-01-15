package com.streamlined.springhibernatetask.entity;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User implements EntityType<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NotBlank(message = "User first name should not be blank")
    @NotNull(message = "User first name should not be null")
    @Size(min = 3, message = "User first name should not be shorter than 3 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "User first name should not be blank")
    @NotNull(message = "User first name should not be null")
    @Size(min = 3, message = "User last name should not be shorter than 3 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NaturalId
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public boolean userNameStartsWith(String firstName, String lastName) {
        return getUserName().startsWith(getInitialUsername(firstName, lastName));
    }

    private String getInitialUsername(String firstName, String lastName) {
        return firstName + "." + lastName;
    }

    public String getUsernameSerial() {
        return getUserName().substring(getInitialUsername(firstName, lastName).length());
    }

    public void setUsernameSerial(String serial) {
        userName = getInitialUsername(firstName, lastName) + serial;
    }

}
