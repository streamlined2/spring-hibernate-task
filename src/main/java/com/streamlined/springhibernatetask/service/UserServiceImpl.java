package com.streamlined.springhibernatetask.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.streamlined.springhibernatetask.entity.User;

import repository.TraineeRepository;
import repository.TrainerRepository;

public abstract class UserServiceImpl {

    protected TraineeRepository traineeRepository;
    protected TrainerRepository trainerRepository;

    @Autowired
    void setTraineeRepository(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Autowired
    void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    protected void setNextUsernameSerial(User user) {
        Optional<String> serial = getMaxUsernameSerial(user.getFirstName(), user.getLastName());
        String nextSerial = serial.map(value -> value.isBlank() ? "1" : Long.toString(Long.parseLong(value) + 1))
                .orElse("");
        user.setUsernameSerial(nextSerial);
    }

    private Optional<String> getMaxUsernameSerial(String firstName, String lastName) {
        Optional<String> traineeSerial = traineeRepository.getMaxUsernameSerial(firstName, lastName);
        Optional<String> trainerSerial = trainerRepository.getMaxUsernameSerial(firstName, lastName);
        return getMaxSerial(traineeSerial, trainerSerial);
    }

    private Optional<String> getMaxSerial(Optional<String> value1, Optional<String> value2) {
        if (value1.isEmpty())
            return value2;
        if (value2.isEmpty())
            return value1;
        String serial1 = value1.get();
        String serial2 = value2.get();
        if (serial1.isBlank())
            return value2;
        if (serial2.isBlank())
            return value1;
        long numeric1 = Long.parseLong(serial1);
        long numeric2 = Long.parseLong(serial2);
        return numeric1 > numeric2 ? value1 : value2;
    }

}
