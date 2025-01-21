package com.streamlined.springhibernatetask.mapper;

import org.mapstruct.Mapper;

import com.streamlined.springhibernatetask.dto.TrainingTypeDto;
import com.streamlined.springhibernatetask.entity.TrainingType;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {

    TrainingType toEntity(TrainingTypeDto dto);

    TrainingTypeDto toDto(TrainingType entity);

}
