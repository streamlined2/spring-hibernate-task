package com.streamlined.springhibernatetask.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "trainingtype")
public class TrainingType implements EntityType<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    @ToString.Include
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Override
    public Long getPrimaryKey() {
        return getId();
    }

    @Override
    public boolean isIdenticalTo(EntityType<Long> entity) {
        if (entity instanceof TrainingType trainingType) {
            return Objects.equals(getId(), trainingType.getId()) && Objects.equals(getName(), trainingType.getName());
        }
        return false;
    }

}
