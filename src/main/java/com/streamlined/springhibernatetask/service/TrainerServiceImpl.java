package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.streamlined.springhibernatetask.EntityManagerStorage;
import com.streamlined.springhibernatetask.Utilities;
import com.streamlined.springhibernatetask.dto.TrainerCreatedResponse;
import com.streamlined.springhibernatetask.dto.TrainerDto;
import com.streamlined.springhibernatetask.dto.TrainingDto;
import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.exception.EntityCreationException;
import com.streamlined.springhibernatetask.exception.EntityDeletionException;
import com.streamlined.springhibernatetask.exception.EntityQueryException;
import com.streamlined.springhibernatetask.exception.EntityUpdateException;
import com.streamlined.springhibernatetask.exception.NoSuchEntityException;
import com.streamlined.springhibernatetask.mapper.TrainerMapper;
import com.streamlined.springhibernatetask.mapper.TrainingMapper;
import com.streamlined.springhibernatetask.repository.TrainerRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TrainerServiceImpl extends UserServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    private final SecurityService securityService;
    private final Validator validator;
    private final EntityManagerStorage entityManagerStorage;

    @Override
    public TrainerDto create(TrainerDto dto, char[] password) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            Utilities.checkIfValid(validator, dto);
            Trainer trainer = trainerMapper.toEntity(dto);
            trainer.setId(null);
            trainer.setPasswordHash(securityService.getPasswordHash(password));
            transaction = entityManager.getTransaction();
            transaction.begin();
            setNextUsernameSerial(trainer);
            Trainer createdTrainer = trainerRepository.create(trainer);
            transaction.commit();
            securityService.clearPassword(password);
            return trainerMapper.toDto(createdTrainer);
        } catch (Exception e) {
            LOGGER.debug("Error creating trainer entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityCreationException("Error creating trainer entity", e);
        }
    }

    @Override
    public TrainerCreatedResponse create(TrainerDto dto) throws EntityCreationException {
        try {
            char[] password = securityService.getNewPassword();
            TrainerDto createdTrainer = create(dto, password);
            return TrainerCreatedResponse.builder().id(createdTrainer.id()).userName(createdTrainer.userName())
                    .password(password).build();
        } catch (Exception e) {
            LOGGER.debug("Error creating trainer entity", e);
            throw new EntityCreationException("Error creating trainer entity", e);
        }
    }

    @Override
    public TrainerDto update(TrainerDto dto) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            Utilities.checkIfValid(validator, dto);
            Trainer newTrainer = trainerMapper.toEntity(dto);
            transaction = entityManager.getTransaction();
            transaction.begin();
            Trainer trainer = trainerRepository.findById(dto.id())
                    .orElseThrow(() -> new NoSuchEntityException("No trainer entity with id %d".formatted(dto.id())));
            newTrainer.setPasswordHash(trainer.getPasswordHash());
            newTrainer.setUserName(trainer.getUserName());
            Trainer savedTrainer = trainerRepository.update(newTrainer);
            transaction.commit();
            return trainerMapper.toDto(savedTrainer);
        } catch (Exception e) {
            LOGGER.debug("Error updating trainer entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityUpdateException("Error updating trainer entity", e);
        }
    }

    @Override
    public TrainerDto updatePassword(Long id, char[] password) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Trainer trainer = trainerRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("No trainer entity with id %d".formatted(id)));
            trainer.setPasswordHash(securityService.getPasswordHash(password));
            Trainer updatedTrainer = trainerRepository.update(trainer);
            transaction.commit();
            securityService.clearPassword(password);
            return trainerMapper.toDto(updatedTrainer);
        } catch (Exception e) {
            LOGGER.debug("Error updating password for trainer entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityUpdateException("Error updating password for trainer entity", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            trainerRepository.deleteById(id);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.debug("Error deleting trainer entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityDeletionException("Error deleting trainer entity", e);
        }
    }

    @Override
    public Optional<TrainerDto> findById(Long id) {
        try {
            return trainerRepository.findById(id).map(trainerMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainer entity", e);
            throw new EntityQueryException("Error querying trainer entity", e);
        }
    }

    @Override
    public Stream<TrainerDto> findAll() {
        try {
            return Utilities.stream(trainerRepository.findAll()).map(trainerMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainer entity", e);
            throw new EntityQueryException("Error querying trainer entity", e);
        }
    }

    @Override
    public Optional<TrainerDto> findByUserName(String userName) {
        try {
            return trainerRepository.findByUserName(userName).map(trainerMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainer entity", e);
            throw new EntityQueryException("Error querying trainer entity", e);
        }
    }

    @Override
    public boolean changeActiveStatus(Long id) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Trainer trainer = trainerRepository.findById(id).map(this::changeUserStatus)
                    .orElseThrow(() -> new NoSuchEntityException("No trainer entity with id %d".formatted(id)));
            Trainer savedTrainer = trainerRepository.update(trainer);
            transaction.commit();
            return savedTrainer.isActive();
        } catch (Exception e) {
            LOGGER.debug("Error querying trainer entity", e);
            if (transaction != null)
                transaction.rollback();
            throw new EntityQueryException("Error querying trainer entity", e);
        }
    }

    @Override
    public Stream<TrainingDto> getTrainingListByUserNameDateRangeTraineeName(String trainerUserName, LocalDate fromDate,
            LocalDate toDate, String traineeName) throws EntityQueryException {
        try {
            Iterable<Training> trainings = trainerRepository
                    .getTrainingListByUserNameDateRangeTraineeNameType(trainerUserName, fromDate, toDate, traineeName);
            return Utilities.stream(trainings).map(trainingMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainer entity", e);
            throw new EntityQueryException("Error querying trainer entity", e);
        }
    }

    @Override
    public Stream<TrainerDto> getNonAssignedTrainers(String traineeUserName) {
        Iterable<Trainer> trainers = trainerRepository.getNonAssignedTrainers(traineeUserName);
        return Utilities.stream(trainers).map(trainerMapper::toDto);
    }

}
