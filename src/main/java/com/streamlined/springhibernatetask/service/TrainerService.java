package com.streamlined.springhibernatetask.service;

import java.util.Optional;
import java.util.stream.Stream;

import dto.TrainerDto;
import exception.EntityCreationException;
import exception.EntityDeletionException;
import exception.EntityQueryException;
import exception.EntityUpdateException;

public interface TrainerService {

    void create(TrainerDto dto, char[] password) throws EntityCreationException;

    void update(TrainerDto dto) throws EntityUpdateException;

    void updatePassword(Long id, char[] password) throws EntityUpdateException;

    void deleteById(Long id) throws EntityDeletionException;

    Optional<TrainerDto> findById(Long id) throws EntityQueryException;

    Optional<TrainerDto> findByUserName(String userName) throws EntityQueryException;

    Stream<TrainerDto> findAll() throws EntityQueryException;

}
