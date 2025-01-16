package com.streamlined.springhibernatetask.validator;

import com.streamlined.springhibernatetask.entity.User;
import com.streamlined.springhibernatetask.exception.InvalidEntityDataException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class UserValidator extends EntityValidator {

    protected boolean isValid(User entity) {
        try {
            checkUserId(entity.getUserId());
            checkFirstName(entity.getFirstName());
            checkLastName(entity.getLastName());
            checkUserName(entity.getUserName(), entity.getFirstName(), entity.getLastName());
            return true;
        } catch (InvalidEntityDataException e) {
            LOGGER.info("Invalid entity: {}", e.getMessage(), e);
            return false;
        }
    }

    private void checkFirstName(String firstName) {
        if (!containsValidNameCharacters(firstName))
            throw new InvalidEntityDataException(
                    "First name should contain only Latin characters and start with capital letter");
    }

    private void checkLastName(String lastName) {
        if (!containsValidNameCharacters(lastName))
            throw new InvalidEntityDataException(
                    "Last name should contain only Latin characters and start with capital letter");
    }

    private void checkUserName(String userName, String firstName, String lastName) {
        String userNamePrefix = firstName + "." + lastName;
        if (!userName.startsWith(userNamePrefix))
            throw new InvalidEntityDataException(
                    "User name should start with first name followed by dot and last name of the user");
        String serialNumberSuffix = userName.substring(userNamePrefix.length());
        if (serialNumberSuffix.isBlank())
            return;
        try {
            long serialNumber = Long.parseLong(serialNumberSuffix);
            if (serialNumber <= 0)
                throw new InvalidEntityDataException("User name serial number should be positive number");
        } catch (NumberFormatException e) {
            throw new InvalidEntityDataException("User name serial number should be valid number", e);
        }
    }

}
