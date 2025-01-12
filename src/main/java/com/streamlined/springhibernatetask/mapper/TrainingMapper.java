package com.streamlined.springhibernatetask.mapper;

import org.mapstruct.Mapper;

import com.streamlined.springhibernatetask.entity.Training;

import dto.TrainingDto;

@Mapper
public interface TrainingMapper {

    Training toEntity(TrainingDto dto);

    TrainingDto toDto(Training entity);

}
