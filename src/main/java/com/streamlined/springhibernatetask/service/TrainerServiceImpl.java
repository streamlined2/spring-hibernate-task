package com.streamlined.springhibernatetask.service;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.mapper.TrainerMapper;

import dto.TrainerDto;
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
    private final SecurityService securityService;

    public TrainerServiceImpl(TrainerMapper trainerMapper, SecurityService securityService) {
        this.trainerMapper = trainerMapper;
        this.securityService = securityService;
    }

    @Override
    @Transactional
    public void create(TrainerDto dto, char[] password) {
        try {
            Trainer trainer = trainerMapper.toEntity(dto);
            trainer.setPasswordHash(securityService.getPasswordHash(password));
            setNextUsernameSerial(trainer);
            trainerRepository.save(trainer);
        } catch (Exception e) {
            LOGGER.debug("Error creating trainer entity", e);
            throw new EntityCreationException("Error creating trainer entity", e);
        }
    }

    @Override
    @Transactional
    public void update(TrainerDto dto) {
        try {
            Trainer trainer = trainerRepository.findById(dto.userId()).orElseThrow(
                    () -> new NoSuchEntityException("No trainer entity with id %d".formatted(dto.userId())));
            Trainer newTrainer = trainerMapper.toEntity(dto);
            newTrainer.setPasswordHash(trainer.getPasswordHash());
            newTrainer.setUserName(trainer.getUserName());
            trainerRepository.save(newTrainer);
        } catch (Exception e) {
            LOGGER.debug("Error updating trainer entity", e);
            throw new EntityUpdateException("Error updating trainer entity", e);
        }
    }

    @Override
    @Transactional
    public void updatePassword(Long id, char[] password) {
        try {
            Trainer trainer = trainerRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("No trainer entity with id %d".formatted(id)));
            trainer.setPasswordHash(securityService.getPasswordHash(password));
            trainerRepository.save(trainer);
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

}
