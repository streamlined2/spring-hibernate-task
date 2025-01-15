package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.mapper.TrainerMapper;
import com.streamlined.springhibernatetask.mapper.TrainingMapper;

import dto.TrainerCreatedResponse;
import dto.TrainerDto;
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
public class TrainerServiceImpl extends UserServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    private final SecurityService securityService;
    private final TrainerService trainerService;

    public TrainerServiceImpl(TrainerMapper trainerMapper, TrainingMapper trainingMapper,
            SecurityService securityService, TrainerService trainerService) {
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
        this.securityService = securityService;
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
