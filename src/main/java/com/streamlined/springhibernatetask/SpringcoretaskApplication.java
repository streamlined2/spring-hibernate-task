package com.streamlined.springhibernatetask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/* @RequiredArgsConstructor */
public class SpringcoretaskApplication/* implements CommandLineRunner */ {

    /*
     * private final Parser parser;
     * 
     * private final TraineeRepository traineeRepository; private
     * final @Value("${source.csv.trainee}") String traineeFileName; private final
     * TraineeValidator traineeValidator;
     * 
     * private final TrainerRepository trainerRepository; private
     * final @Value("${source.csv.trainer}") String trainerFileName; private final
     * TrainerValidator trainerValidator;
     * 
     * private final TrainingRepository trainingRepository; private
     * final @Value("${source.csv.training}") String trainingFileName; private final
     * TrainingValidator trainingValidator;
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringcoretaskApplication.class, args);
    }

    /*
     * @Override public void run(String... args) throws Exception {
     * traineeRepository.deleteAll();
     * traineeRepository.saveAll(parser.parse(Trainee.class, traineeFileName,
     * traineeValidator).values());
     * 
     * trainerRepository.deleteAll();
     * trainerRepository.saveAll(parser.parse(Trainer.class, trainerFileName,
     * trainerValidator).values());
     * 
     * trainingRepository.deleteAll();
     * trainingRepository.saveAll(parser.parse(Training.class, trainingFileName,
     * trainingValidator).values()); }
     */
}
