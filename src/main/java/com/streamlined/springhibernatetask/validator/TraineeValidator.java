package com.streamlined.springhibernatetask.validator;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;
import com.streamlined.springhibernatetask.entity.Trainee;
import exception.InvalidEntityDataException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class TraineeValidator extends UserValidator implements Validator<Trainee> {

    public static final int MIN_AGE = 10;
    public static final int MAX_AGE = 200;

    @Override
    public boolean isValid(Trainee trainee) {
        if (!super.isValid(trainee))
            return false;
        try {
            checkDateOfBirth(trainee.getDateOfBirth());
            checkAddress(trainee.getAddress());
            return true;
        } catch (InvalidEntityDataException e) {
            LOGGER.info("Invalid trainee entity: {}", e.getMessage(), e);
            return false;
        }
    }

    private void checkDateOfBirth(LocalDate dateOfBirth) {
        LocalDate presentDate = LocalDate.now();
        if (dateOfBirth.isAfter(presentDate))
            throw new InvalidEntityDataException(
                    "Date of birth should be less than present date %tF".formatted(presentDate));
        Period age = dateOfBirth.until(presentDate);
        if (age.getYears() < MIN_AGE)
            throw new InvalidEntityDataException("Trainee should be at least %d years old".formatted(MIN_AGE));
        if (age.getYears() >= MAX_AGE)
            throw new InvalidEntityDataException(
                    "Trainee age is probably is not valid as it is greater than %d years".formatted(MAX_AGE));
    }

    private void checkAddress(String address) {
        if (!containsValidAddressCharacters(address))
            throw new InvalidEntityDataException(
                    "Address should contain only Latin characters, punctuation marks, or digits");
    }

    private boolean containsValidAddressCharacters(String value) {
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
