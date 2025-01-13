package com.streamlined.springhibernatetask;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.parser.Parser;
import com.streamlined.springhibernatetask.validator.TraineeValidator;
import com.streamlined.springhibernatetask.validator.TrainerValidator;
import com.streamlined.springhibernatetask.validator.TrainingValidator;

import lombok.RequiredArgsConstructor;
import repository.TraineeRepository;
import repository.TrainerRepository;
import repository.TrainingRepository;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringcoretaskApplication implements CommandLineRunner {

    private final Parser parser;
    private final TraineeRepository traineeRepository;
    private final @Value("${source.csv.trainee}") String traineeFileName;
    private final TraineeValidator traineeValidator;
    private final TrainerRepository trainerRepository;
    private final @Value("${source.csv.trainer}") String trainerFileName;
    private final TrainerValidator trainerValidator;
    private final TrainingRepository trainingRepository;
    private final @Value("${source.csv.training}") String trainingFileName;
    private final TrainingValidator trainingValidator;

    public static void main(String[] args) {
        SpringApplication.run(SpringcoretaskApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        traineeRepository.deleteAll();
        traineeRepository.saveAll(parser.parse(Trainee.class, traineeFileName, traineeValidator).values());

        trainerRepository.deleteAll();
        trainerRepository.saveAll(parser.parse(Trainer.class, trainerFileName, trainerValidator).values());

        trainingRepository.deleteAll();
        trainingRepository.saveAll(parser.parse(Training.class, trainingFileName, trainingValidator).values());
    }

}
