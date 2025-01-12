package com.streamlined.springhibernatetask.mapper;

import org.mapstruct.Mapper;

import com.streamlined.springhibernatetask.entity.TrainingType;

import dto.TrainingTypeDto;

@Mapper
public interface TrainingTypeMapper {

    TrainingType toEntity(TrainingTypeDto dto);

    TrainingTypeDto toDto(TrainingType entity);

}
