package com.streamlined.springhibernatetask.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.mapper.TraineeMapper;
import com.streamlined.springhibernatetask.validator.TraineeValidator;

import dto.TraineeDto;
import exception.EntityCreationException;
import exception.EntityDeletionException;
import exception.EntityQueryException;
import exception.EntityUpdateException;
import exception.NoSuchEntityException;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(readOnly = true)
public class TraineeServiceImpl extends UserServiceImpl implements TraineeService {

    private final TraineeMapper traineeMapper;
    private final SecurityService securityService;

    public TraineeServiceImpl(TraineeMapper traineeMapper, SecurityService securityService,
            TraineeValidator traineeValidator) {
        this.traineeMapper = traineeMapper;
        this.securityService = securityService;
    }

    @Override
    @Transactional
    public void create(TraineeDto dto, char[] password) {
        try {
            Trainee trainee = traineeMapper.toEntity(dto);
            trainee.setPasswordHash(securityService.getPasswordHash(password));
            setNextUsernameSerial(trainee);
            traineeRepository.save(trainee);
        } catch (Exception e) {
            LOGGER.debug("Error creating trainee entity", e);
            throw new EntityCreationException("Error creating trainee entity", e);
        }
    }

    @Override
    @Transactional
    public void update(TraineeDto dto) {
        try {
            Trainee trainee = traineeRepository.findById(dto.userId()).orElseThrow(
                    () -> new NoSuchEntityException("No trainee entity with id %d".formatted(dto.userId())));
            Trainee newTrainee = traineeMapper.toEntity(dto);
            newTrainee.setPasswordHash(trainee.getPasswordHash());
            newTrainee.setUserName(trainee.getUserName());
            traineeRepository.save(newTrainee);
        } catch (Exception e) {
            LOGGER.debug("Error updating trainee entity", e);
            throw new EntityUpdateException("Error updating trainee entity", e);
        }
    }

    @Override
    @Transactional
    public void updatePassword(Long id, char[] password) {
        try {
            Trainee trainee = traineeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("No trainee entity with id %d".formatted(id)));
            trainee.setPasswordHash(securityService.getPasswordHash(password));
            traineeRepository.save(trainee);
        } catch (Exception e) {
            LOGGER.debug("Error updating password for trainee entity", e);
            throw new EntityUpdateException("Error updating password for trainee entity", e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            traineeRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.debug("Error deleting trainee entity", e);
            throw new EntityDeletionException("Error deleting trainee entity", e);
        }
    }

    @Override
    public Optional<TraineeDto> findById(Long id) {
        try {
            return traineeRepository.findById(id).map(traineeMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainee entity", e);
            throw new EntityQueryException("Error querying trainee entity", e);
        }
    }

    @Override
    public Stream<TraineeDto> findAll() {
        try {
            return traineeRepository.findAll().stream().map(traineeMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainee entity", e);
            throw new EntityQueryException("Error querying trainee entity", e);
        }
    }

    @Override
    public Optional<TraineeDto> findByUserName(String userName) {
        try {
            return traineeRepository.findByUserName(userName).map(traineeMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainee entity", e);
            throw new EntityQueryException("Error querying trainee entity", e);
        }
    }

}
