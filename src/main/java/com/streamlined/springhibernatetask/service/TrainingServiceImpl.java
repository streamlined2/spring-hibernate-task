package com.streamlined.springhibernatetask.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.entity.TrainingKey;
import com.streamlined.springhibernatetask.mapper.TrainingMapper;

import dto.TrainingDto;
import exception.EntityCreationException;
import exception.EntityQueryException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import repository.TrainingRepository;

@Service
@Log4j2
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepository;
    private final Validator validator;

    @Override
    @Transactional
    public TrainingDto create(TrainingDto dto) {
        try {
            Training training = trainingMapper.toEntity(dto);
            ValidationUtilities.checkIfValid(validator, training);
            return trainingMapper.toDto(trainingRepository.save(training));
        } catch (Exception e) {
            LOGGER.debug("Error creating training entity", e);
            throw new EntityCreationException("Error creating training entity", e);
        }
    }

    @Override
    public Optional<TrainingDto> findById(TrainingKey key) {
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
            return trainingRepository.findAll().stream().map(trainingMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying training entity", e);
            throw new EntityQueryException("Error querying training entity", e);
        }
    }

}
