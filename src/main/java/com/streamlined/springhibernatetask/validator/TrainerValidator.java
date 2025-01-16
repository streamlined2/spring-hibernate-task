package com.streamlined.springhibernatetask.validator;

import org.springframework.stereotype.Component;

import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.exception.InvalidEntityDataException;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class TrainerValidator extends UserValidator implements Validator<Trainer> {

    @Override
    public boolean isValid(Trainer trainer) {
        if (!super.isValid(trainer))
            return false;
        try {
            checkSpecialization(trainer.getSpecialization().getName());
            return true;
        } catch (InvalidEntityDataException e) {
            LOGGER.info("Invalid trainer entity: {}", e.getMessage(), e);
            return false;
        }
    }

    private void checkSpecialization(String address) {
        if (!containsValidSpecializationCharacters(address))
            throw new InvalidEntityDataException(
                    "Specialization should contain only Latin characters, punctuation marks, or digits");
    }

    private boolean containsValidSpecializationCharacters(String value) {
        for (int k = 0; k < value.length(); k++) {
            char ch = value.charAt(k);
            if (!punctuationCharSet.contains(ch) && !digitCharSet.contains(ch) && !validCapitalCharSet.contains(ch)
                    && !validLowerCaseCharSet.contains(ch)) {
                return false;
            }
        }
        return true;
    }

}
