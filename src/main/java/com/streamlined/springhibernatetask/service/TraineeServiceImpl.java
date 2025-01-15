package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.TrainingType;
import com.streamlined.springhibernatetask.mapper.TraineeMapper;
import com.streamlined.springhibernatetask.mapper.TrainingMapper;

import dto.TraineeCreatedResponse;
import dto.TraineeDto;
import dto.TrainingDto;
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
    private final TrainingMapper trainingMapper;
    private final SecurityService securityService;
    private final TraineeService traineeService;

    public TraineeServiceImpl(TraineeMapper traineeMapper, TrainingMapper trainingMapper,
            SecurityService securityService, TraineeService traineeService) {
        this.traineeMapper = traineeMapper;
        this.trainingMapper = trainingMapper;
        this.securityService = securityService;
        this.traineeService = traineeService;
    }

    @Override
    @Transactional
    public TraineeDto create(TraineeDto dto, char[] password) {
        try {
            Trainee trainee = traineeMapper.toEntity(dto);
            trainee.setId(null);
            trainee.setPasswordHash(securityService.getPasswordHash(password));
            setNextUsernameSerial(trainee);
            Trainee createdTrainee = traineeRepository.save(trainee);
            securityService.clearPassword(password);
            return traineeMapper.toDto(createdTrainee);
        } catch (Exception e) {
            LOGGER.debug("Error creating trainee entity", e);
            throw new EntityCreationException("Error creating trainee entity", e);
        }
    }

    @Override
    public TraineeCreatedResponse create(TraineeDto dto) throws EntityCreationException {
        try {
            char[] password = securityService.getNewPassword();
            TraineeDto createdTrainee = traineeService.create(dto, password);
            return TraineeCreatedResponse.builder().userId(createdTrainee.userId()).userName(createdTrainee.userName())
                    .password(password).build();
        } catch (Exception e) {
            LOGGER.debug("Error creating trainee entity", e);
            throw new EntityCreationException("Error creating trainee entity", e);
        }
    }

    @Override
    @Transactional
    public TraineeDto update(TraineeDto dto) {
        try {
            Trainee trainee = traineeRepository.findById(dto.userId()).orElseThrow(
                    () -> new NoSuchEntityException("No trainee entity with id %d".formatted(dto.userId())));
            Trainee newTrainee = traineeMapper.toEntity(dto);
            newTrainee.setPasswordHash(trainee.getPasswordHash());
            newTrainee.setUserName(trainee.getUserName());
            return traineeMapper.toDto(traineeRepository.save(newTrainee));
        } catch (Exception e) {
            LOGGER.debug("Error updating trainee entity", e);
            throw new EntityUpdateException("Error updating trainee entity", e);
        }
    }

    @Override
    @Transactional
    public TraineeDto updatePassword(Long id, char[] password) {
        try {
            Trainee trainee = traineeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("No trainee entity with id %d".formatted(id)));
            trainee.setPasswordHash(securityService.getPasswordHash(password));
            Trainee updatedTrainee = traineeRepository.save(trainee);
            securityService.clearPassword(password);
            return traineeMapper.toDto(updatedTrainee);
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
    public void deleteByUserName(String userName) throws EntityDeletionException {
        try {
            traineeRepository.deleteByUserName(userName);
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

    @Override
    @Transactional
    public boolean changeActiveStatus(Long id) {
        try {
            Trainee trainee = traineeRepository.findById(id).map(this::changeUserStatus)
                    .orElseThrow(() -> new NoSuchEntityException("No trainee entity with id %d".formatted(id)));
            traineeRepository.save(trainee);
            return trainee.isActive();
        } catch (Exception e) {
            LOGGER.debug("Error querying trainee entity", e);
            throw new EntityQueryException("Error querying trainee entity", e);
        }
    }

    @Override
    public Stream<TrainingDto> getTrainingListByUserNameDateRangeTrainerNameType(String traineeUserName,
            LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType)
            throws EntityQueryException {
        try {
            return traineeRepository.getTrainingListByUserNameDateRangeTrainerNameType(traineeUserName, fromDate,
                    toDate, trainerName, trainingType).stream().map(trainingMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainee entity", e);
            throw new EntityQueryException("Error querying trainee entity", e);
        }
    }

}
