package com.streamlined.springhibernatetask.validator;

import java.util.HashSet;
import java.util.Set;

import com.streamlined.springhibernatetask.exception.InvalidEntityDataException;

public abstract class EntityValidator {

    protected final Set<Character> validCapitalCharSet;
    protected final Set<Character> validLowerCaseCharSet;
    protected final Set<Character> punctuationCharSet;
    protected final Set<Character> digitCharSet;

    protected EntityValidator() {
        validCapitalCharSet = new HashSet<>();
        validLowerCaseCharSet = new HashSet<>();
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            validCapitalCharSet.add(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            validLowerCaseCharSet.add(ch);
        }
        punctuationCharSet = Set.of(',', '.', '!', '?', '(', ')', '-', '/', '\\', ' ');
        digitCharSet = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    }

    protected void checkUserId(Long userId) {
        if (userId == null)
            throw new InvalidEntityDataException("User id can't be null");
        if (userId.longValue() <= 0)
            throw new InvalidEntityDataException("User id should be positive value");
    }

    protected boolean containsValidNameCharacters(String value) {
        if (!validCapitalCharSet.contains(value.charAt(0)))
            return false;
        for (int k = 1; k < value.length(); k++) {
            char ch = value.charAt(k);
            if (!validLowerCaseCharSet.contains(ch) && !punctuationCharSet.contains(ch)) {
                return false;
            }
        }
        return true;
    }

}
