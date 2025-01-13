package com.streamlined.springhibernatetask.service;

public interface SecurityService {

    char[] getNewPassword();

    String getPasswordHash(char[] password);

}
