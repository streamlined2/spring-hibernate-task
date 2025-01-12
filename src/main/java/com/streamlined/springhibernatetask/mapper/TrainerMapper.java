package com.streamlined.springhibernatetask.mapper;

import org.mapstruct.Mapper;

import com.streamlined.springhibernatetask.entity.Trainer;

import dto.TrainerDto;

@Mapper
public interface TrainerMapper {

    Trainer toEntity(TrainerDto dto);

    TrainerDto toDto(Trainer entity);

}
