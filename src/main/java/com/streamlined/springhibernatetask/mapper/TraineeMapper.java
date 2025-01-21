package com.streamlined.springhibernatetask.mapper;

import org.mapstruct.Mapper;

import com.streamlined.springhibernatetask.dto.TraineeDto;
import com.streamlined.springhibernatetask.entity.Trainee;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    Trainee toEntity(TraineeDto dto);

    TraineeDto toDto(Trainee entity);

}
