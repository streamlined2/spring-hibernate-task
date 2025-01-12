package com.streamlined.springhibernatetask.mapper;

import org.mapstruct.Mapper;

import com.streamlined.springhibernatetask.entity.Trainee;

import dto.TraineeDto;

@Mapper
public interface TraineeMapper {

    Trainee toEntity(TraineeDto dto);

    TraineeDto toDto(Trainee entity);

}
