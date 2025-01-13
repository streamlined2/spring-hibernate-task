package com.streamlined.springhibernatetask.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final PasswordEncoder passwordEncoder;
    private final Random random;
    private final int passwordLength;
    private final char startPasswordChar;
    private final char endPasswordChar;
    private final int saltOffset;

    public SecurityServiceImpl(PasswordEncoder passwordEncoder, Random random,
            @Value("${password.length}") int passwordLength, @Value("${password.startchar}") char startPasswordChar,
            @Value("${password.endchar}") char endPasswordChar, @Value("${password.saltoffset}") int saltOffset) {
        this.passwordEncoder = passwordEncoder;
        this.random = random;
        this.passwordLength = passwordLength;
        this.startPasswordChar = startPasswordChar;
        this.endPasswordChar = endPasswordChar;
        this.saltOffset = saltOffset;
    }

    @Override
    public char[] getNewPassword() {
        char[] password = new char[passwordLength];
        for (int k = 0; k < passwordLength; k++) {
            password[k] = (char) random.nextInt(startPasswordChar, endPasswordChar);
        }
        return password;
    }

    @Override
    public String getPasswordHash(char[] password) {
        StringBuilder builder = new StringBuilder();
        builder.append(password);
        for (int k = 0; k < builder.length(); k++) {
            char c = (char) (builder.charAt(k) + saltOffset);
            builder.setCharAt(k, c);
        }
        return passwordEncoder.encode(builder);
    }

}
