package com.streamlined.springhibernatetask;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.streamlined.springhibernatetask.repository.TrainingTypeRepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringHibernateTaskApplication implements CommandLineRunner {

    private final TrainingTypeRepository trainingTypeRepository;

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
        SpringApplication.run(SpringHibernateTaskApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        trainingTypeRepository.findAll().forEach(System.out::println);

        System.out.println(trainingTypeRepository.existsById(1L));
        System.out.println(trainingTypeRepository.existsById(100L));

        System.out.println(trainingTypeRepository.findById(2L));
        System.out.println(trainingTypeRepository.findById(200L));
        
        System.out.println(trainingTypeRepository.count());

        List<Long> ids=List.of(5L, 6L, 7L, 8L, 9L);
        System.out.println(trainingTypeRepository.findAllById(ids));
    }

    /*
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
     * trainingValidator).values());
     */

    @Bean
    CsvMapper csvMapper() {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        return csvMapper;
    }

}
