package com.streamlined.springhibernatetask.mapper;

import org.mapstruct.Mapper;

import com.streamlined.springhibernatetask.dto.TrainingDto;
import com.streamlined.springhibernatetask.entity.Training;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    Training toEntity(TrainingDto dto);

    TrainingDto toDto(Training entity);

}
