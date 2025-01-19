package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.streamlined.springhibernatetask.dto.TraineeCreatedResponse;
import com.streamlined.springhibernatetask.dto.TraineeDto;
import com.streamlined.springhibernatetask.dto.TrainingDto;
import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.entity.TrainingType;
import com.streamlined.springhibernatetask.exception.EntityCreationException;
import com.streamlined.springhibernatetask.exception.EntityDeletionException;
import com.streamlined.springhibernatetask.exception.EntityQueryException;
import com.streamlined.springhibernatetask.exception.EntityUpdateException;
import com.streamlined.springhibernatetask.exception.NoSuchEntityException;
import com.streamlined.springhibernatetask.mapper.TraineeMapper;
import com.streamlined.springhibernatetask.mapper.TrainingMapper;
import com.streamlined.springhibernatetask.repository.TraineeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TraineeServiceImpl extends UserServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final SecurityService securityService;
    private final Validator validator;
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public TraineeDto create(TraineeDto dto, char[] password) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            ServiceUtilities.checkIfValid(validator, dto);
            Trainee trainee = traineeMapper.toEntity(dto);
            trainee.setId(null);
            trainee.setPasswordHash(securityService.getPasswordHash(password));
            transaction = entityManager.getTransaction();
            transaction.begin();
            setNextUsernameSerial(trainee);
            Trainee createdTrainee = traineeRepository.save(trainee);
            transaction.commit();
            securityService.clearPassword(password);
            return traineeMapper.toDto(createdTrainee);
        } catch (Exception e) {
            LOGGER.debug("Error creating trainee entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityCreationException("Error creating trainee entity", e);
        }
    }

    @Override
    public TraineeCreatedResponse create(TraineeDto dto) throws EntityCreationException {
        try {
            char[] password = securityService.getNewPassword();
            TraineeDto createdTrainee = create(dto, password);
            return TraineeCreatedResponse.builder().id(createdTrainee.id()).userName(createdTrainee.userName())
                    .password(password).build();
        } catch (Exception e) {
            LOGGER.debug("Error creating trainee entity", e);
            throw new EntityCreationException("Error creating trainee entity", e);
        }
    }

    @Override
    public TraineeDto update(TraineeDto dto) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            ServiceUtilities.checkIfValid(validator, dto);
            Trainee newTrainee = traineeMapper.toEntity(dto);
            transaction = entityManager.getTransaction();
            transaction.begin();
            Trainee trainee = traineeRepository.findById(dto.id())
                    .orElseThrow(() -> new NoSuchEntityException("No trainee entity with id %d".formatted(dto.id())));
            newTrainee.setPasswordHash(trainee.getPasswordHash());
            newTrainee.setUserName(trainee.getUserName());
            Trainee savedTrainee = traineeRepository.save(newTrainee);
            transaction.commit();
            return traineeMapper.toDto(savedTrainee);
        } catch (Exception e) {
            LOGGER.debug("Error updating trainee entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityUpdateException("Error updating trainee entity", e);
        }
    }

    @Override
    public TraineeDto updatePassword(Long id, char[] password) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Trainee trainee = traineeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("No trainee entity with id %d".formatted(id)));
            trainee.setPasswordHash(securityService.getPasswordHash(password));
            Trainee updatedTrainee = traineeRepository.save(trainee);
            transaction.commit();
            securityService.clearPassword(password);
            return traineeMapper.toDto(updatedTrainee);
        } catch (Exception e) {
            LOGGER.debug("Error updating password for trainee entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityUpdateException("Error updating password for trainee entity", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            traineeRepository.deleteById(id);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.debug("Error deleting trainee entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityDeletionException("Error deleting trainee entity", e);
        }
    }

    @Override
    public void deleteByUserName(String userName) throws EntityDeletionException {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            traineeRepository.deleteByUserName(userName);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.debug("Error deleting trainee entity", e);
            if (transaction != null)
                transaction.rollback();
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
            return ServiceUtilities.stream(traineeRepository.findAll()).map(traineeMapper::toDto);
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
    public boolean changeActiveStatus(Long id) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Trainee trainee = traineeRepository.findById(id).map(this::changeUserStatus)
                    .orElseThrow(() -> new NoSuchEntityException("No trainee entity with id %d".formatted(id)));
            Trainee savedTrainee = traineeRepository.save(trainee);
            transaction.commit();
            return savedTrainee.isActive();
        } catch (Exception e) {
            LOGGER.debug("Error querying trainee entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityQueryException("Error querying trainee entity", e);
        }
    }

    @Override
    public Stream<TrainingDto> getTrainingListByUserNameDateRangeTrainerNameType(String traineeUserName,
            LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType)
            throws EntityQueryException {
        try {
            Iterable<Training> trainings = traineeRepository.getTrainingListByUserNameDateRangeTrainerNameType(
                    traineeUserName, fromDate, toDate, trainerName, trainingType);
            return ServiceUtilities.stream(trainings).map(trainingMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainee entity", e);
            throw new EntityQueryException("Error querying trainee entity", e);
        }
    }

}
