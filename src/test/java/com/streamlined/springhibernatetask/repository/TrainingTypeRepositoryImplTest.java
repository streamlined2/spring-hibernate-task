package com.streamlined.springhibernatetask.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.streamlined.springhibernatetask.SpringHibernateTaskApplication;
import com.streamlined.springhibernatetask.dto.TrainingTypeDto;
import com.streamlined.springhibernatetask.entity.TrainingType;
import com.streamlined.springhibernatetask.mapper.TrainingTypeMapper;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-it.properties")
@ContextConfiguration(classes = SpringHibernateTaskApplication.class)
class TrainingTypeRepositoryImplTest {

    @Autowired
    private TrainingTypeRepositoryImpl trainingTypeRepository;
    @Autowired
    private TrainingTypeMapper trainingTypeMapper;

    @Test
    void findByIdShouldReturnTrainingTypeEntity_ifPassedIdExists() {
        Long existingId = 1L;

        Optional<TrainingType> trainingType = trainingTypeRepository.findById(existingId);
        Optional<TrainingTypeDto> trainingTypeDto = trainingType.map(trainingTypeMapper::toDto);

        assertTrue(trainingType.isPresent());
        assertEquals(existingId, trainingType.get().getId());
        assertEquals("Art", trainingType.get().getName().trim());

        assertTrue(trainingTypeDto.isPresent());
        assertEquals(existingId, trainingTypeDto.get().id());
        assertEquals("Art", trainingTypeDto.get().name().trim());
    }

    @Test
    void findByIdShouldReturnEmptyEntity_ifPassedIdDoesNotExist() {
        Long nonExistingId = -1L;

        Optional<TrainingType> trainingType = trainingTypeRepository.findById(nonExistingId);
        Optional<TrainingTypeDto> trainingTypeDto = trainingType.map(trainingTypeMapper::toDto);

        assertTrue(trainingType.isEmpty());
        assertTrue(trainingTypeDto.isEmpty());
    }

    @Test
    void findByIdShouldThrowIllegalArgumentException_ifPassedIdIsNull() {
        Long wrongId = null;

        Exception exc = assertThrows(IllegalArgumentException.class, () -> trainingTypeRepository.findById(wrongId));
        assertEquals("id to load is required for loading", exc.getMessage());
    }

    @Test
    void findAllShouldReturnListOfAllAvailableTrainingTypeEntities_ifSucceeds() {
        List<TrainingType> expectedEntityList = List.of(new TrainingType(1L, "Art"),
                new TrainingType(2L, "Mathematics"), new TrainingType(3L, "Geography"), new TrainingType(4L, "History"),
                new TrainingType(5L, "Computers"), new TrainingType(6L, "Biology"), new TrainingType(7L, "Physics"),
                new TrainingType(8L, "Ethics"), new TrainingType(9L, "Chemistry"), new TrainingType(10L, "Astronomy"),
                new TrainingType(11L, "Mechanics"), new TrainingType(12L, "Medicine"),
                new TrainingType(13L, "Psychology"));

        Iterable<TrainingType> actualEntityList = trainingTypeRepository.findAll();

        assertIterableEquals(expectedEntityList, actualEntityList,
                "expected %s and actual %s training type lists are different".formatted(expectedEntityList.toString(),
                        actualEntityList.toString()));
    }

    @Test
    void findAllByIdShouldReturnListOfTrainingTypeEntitiesWithSpecifiedIds_ifSucceeds() {
        List<TrainingType> expectedEntityList = List.of(new TrainingType(4L, "History"),
                new TrainingType(6L, "Biology"), new TrainingType(7L, "Physics"), new TrainingType(10L, "Astronomy"),
                new TrainingType(12L, "Medicine"));

        Iterable<Long> idList = List.of(4L, 6L, 7L, 10L, 12L);

        Iterable<TrainingType> actualEntityList = trainingTypeRepository.findAllById(idList);

        assertIterableEquals(expectedEntityList, actualEntityList,
                "expected %s and actual %s training type lists are different".formatted(expectedEntityList.toString(),
                        actualEntityList.toString()));
    }

    @Test
    void findAllByIdShouldReturnEmptyListOfTrainingTypeEntities_ifNoneOfPassedIdsPresent() {
        List<TrainingType> expectedEntityList = List.of();

        Iterable<Long> idList = List.of(-4L, -6L, -7L, -10L, -12L);

        Iterable<TrainingType> actualEntityList = trainingTypeRepository.findAllById(idList);

        assertFalse(actualEntityList.iterator().hasNext());
        assertIterableEquals(expectedEntityList, actualEntityList,
                "expected %s and actual %s training type lists are different".formatted(expectedEntityList.toString(),
                        actualEntityList.toString()));
    }

}
