package com.streamlined.springhibernatetask.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.streamlined.springhibernatetask.dto.TrainingDto;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.exception.EntityCreationException;
import com.streamlined.springhibernatetask.exception.EntityQueryException;
import com.streamlined.springhibernatetask.mapper.TrainingMapper;
import com.streamlined.springhibernatetask.repository.TrainingRepository;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepository;
    private final Validator validator;

    @Override
    // TODO @Transactional
    public TrainingDto create(TrainingDto dto) {
        try {
            Training training = trainingMapper.toEntity(dto);
            ServiceUtilities.checkIfValid(validator, training);
            return trainingMapper.toDto(trainingRepository.save(training));
        } catch (Exception e) {
            LOGGER.debug("Error creating training entity", e);
            throw new EntityCreationException("Error creating training entity", e);
        }
    }

    @Override
    public Optional<TrainingDto> findById(Long key) {
        try {
            return trainingRepository.findById(key).map(trainingMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying training entity", e);
            throw new EntityQueryException("Error querying training entity", e);
        }
    }

    @Override
    public Stream<TrainingDto> findAll() {
        try {
            return ServiceUtilities.stream(trainingRepository.findAll()).map(trainingMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying training entity", e);
            throw new EntityQueryException("Error querying training entity", e);
        }
    }

}
