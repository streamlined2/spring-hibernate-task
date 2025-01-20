package com.streamlined.springhibernatetask.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.streamlined.springhibernatetask.SpringHibernateTaskApplication;
import com.streamlined.springhibernatetask.dto.TraineeCreatedResponse;
import com.streamlined.springhibernatetask.dto.TraineeDto;
import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.mapper.TraineeMapper;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-it.properties")
@ContextConfiguration(classes = SpringHibernateTaskApplication.class)
class TraineeServiceImplTest {

    @Autowired
    private TraineeServiceImpl traineeService;
    @Autowired
    private TraineeMapper traineeMapper;

    @Test
    void testCreateTraineeDtoCharArray() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testCreateTraineeDto() {
        Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").passwordHash("john").isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA New-York, First Avenue st., 10").build();
        TraineeDto traineeDto = traineeMapper.toDto(trainee);

        TraineeCreatedResponse response = traineeService.create(traineeDto);

        assertEquals("John.Smith", response.userName());
        assertNotNull(response.id());
        assertNotNull(response.password());

        traineeService.deleteById(response.id());
    }

    @Test
    void testUpdate() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testUpdatePassword() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testDeleteById() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testDeleteByUserName() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testFindById() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testFindAll() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testFindByUserName() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testChangeActiveStatus() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testGetTrainingListByUserNameDateRangeTrainerNameType() {
        fail("Not yet implemented"); // TODO
    }

}
