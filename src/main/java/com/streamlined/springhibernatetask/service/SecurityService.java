package com.streamlined.springhibernatetask.service;

import com.streamlined.springhibernatetask.entity.User;

public interface SecurityService {

    char[] getNewPassword();

    String getPasswordHash(char[] password);

    void clearPassword(char[] password);

    boolean matches(User user, String userName, char[] password);

}
