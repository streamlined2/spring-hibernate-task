package com.streamlined.springhibernatetask.mapper;

import org.mapstruct.Mapper;

import com.streamlined.springhibernatetask.dto.TrainerDto;
import com.streamlined.springhibernatetask.entity.Trainer;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    Trainer toEntity(TrainerDto dto);

    TrainerDto toDto(Trainer entity);

}
