package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.springhibernatetask.dto.TrainerCreatedResponse;
import com.streamlined.springhibernatetask.dto.TrainerDto;
import com.streamlined.springhibernatetask.dto.TrainingDto;
import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.exception.EntityCreationException;
import com.streamlined.springhibernatetask.exception.EntityDeletionException;
import com.streamlined.springhibernatetask.exception.EntityQueryException;
import com.streamlined.springhibernatetask.exception.EntityUpdateException;
import com.streamlined.springhibernatetask.exception.NoSuchEntityException;
import com.streamlined.springhibernatetask.mapper.TrainerMapper;
import com.streamlined.springhibernatetask.mapper.TrainingMapper;
import com.streamlined.springhibernatetask.repository.TrainerRepository;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrainerServiceImpl extends UserServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    private final SecurityService securityService;
    private final Validator validator;
    private TrainerService trainerService;

    @Autowired
    @Lazy
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Override
    @Transactional
    public TrainerDto create(TrainerDto dto, char[] password) {
        try {
            Trainer trainer = trainerMapper.toEntity(dto);
            trainer.setId(null);
            trainer.setPasswordHash(securityService.getPasswordHash(password));
            setNextUsernameSerial(trainer);
            ValidationUtilities.checkIfValid(validator, trainer);
            Trainer createdTrainer = trainerRepository.save(trainer);
            securityService.clearPassword(password);
            return trainerMapper.toDto(createdTrainer);
        } catch (Exception e) {
            LOGGER.debug("Error creating trainer entity", e);
            throw new EntityCreationException("Error creating trainer entity", e);
        }
    }

    @Override
    public TrainerCreatedResponse create(TrainerDto dto) throws EntityCreationException {
        try {
            char[] password = securityService.getNewPassword();
            TrainerDto createdTrainer = trainerService.create(dto, password);
            return TrainerCreatedResponse.builder().userId(createdTrainer.userId()).userName(createdTrainer.userName())
                    .password(password).build();
        } catch (Exception e) {
            LOGGER.debug("Error creating trainer entity", e);
            throw new EntityCreationException("Error creating trainer entity", e);
        }
    }

    @Override
    @Transactional
    public TrainerDto update(TrainerDto dto) {
        try {
            Trainer trainer = trainerRepository.findById(dto.userId()).orElseThrow(
                    () -> new NoSuchEntityException("No trainer entity with id %d".formatted(dto.userId())));
            Trainer newTrainer = trainerMapper.toEntity(dto);
            newTrainer.setPasswordHash(trainer.getPasswordHash());
            newTrainer.setUserName(trainer.getUserName());
            ValidationUtilities.checkIfValid(validator, newTrainer);
            return trainerMapper.toDto(trainerRepository.save(newTrainer));
        } catch (Exception e) {
            LOGGER.debug("Error updating trainer entity", e);
            throw new EntityUpdateException("Error updating trainer entity", e);
        }
    }

    @Override
    @Transactional
    public TrainerDto updatePassword(Long id, char[] password) {
        try {
            Trainer trainer = trainerRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("No trainer entity with id %d".formatted(id)));
            trainer.setPasswordHash(securityService.getPasswordHash(password));
            ValidationUtilities.checkIfValid(validator, trainer);
            Trainer updatedTrainer = trainerRepository.save(trainerRepository.save(trainer));
            securityService.clearPassword(password);
            return trainerMapper.toDto(updatedTrainer);
        } catch (Exception e) {
            LOGGER.debug("Error updating password for trainer entity", e);
            throw new EntityUpdateException("Error updating password for trainer entity", e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            trainerRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.debug("Error deleting trainer entity", e);
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
            return trainerRepository.findAll().stream().map(trainerMapper::toDto);
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
    @Transactional
    public boolean changeActiveStatus(Long id) {
        try {
            Trainer trainer = trainerRepository.findById(id).map(this::changeUserStatus)
                    .orElseThrow(() -> new NoSuchEntityException("No trainer entity with id %d".formatted(id)));
            trainerRepository.save(trainer);
            return trainer.isActive();
        } catch (Exception e) {
            LOGGER.debug("Error querying trainer entity", e);
            throw new EntityQueryException("Error querying trainer entity", e);
        }
    }

    @Override
    public Stream<TrainingDto> getTrainingListByUserNameDateRangeTraineeName(String trainerUserName, LocalDate fromDate,
            LocalDate toDate, String traineeName) throws EntityQueryException {
        try {
            return trainerRepository
                    .getTrainingListByUserNameDateRangeTraineeNameType(trainerUserName, fromDate, toDate, traineeName)
                    .stream().map(trainingMapper::toDto);
        } catch (Exception e) {
            LOGGER.debug("Error querying trainer entity", e);
            throw new EntityQueryException("Error querying trainer entity", e);
        }
    }

    @Override
    public Stream<TrainerDto> getNonAssignedTrainers(String traineeUserName) {
        return trainerRepository.getNonAssignedTrainers(traineeUserName).stream().map(trainerMapper::toDto);
    }

}
