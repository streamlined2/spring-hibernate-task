package com.streamlined.springhibernatetask.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.streamlined.springhibernatetask.entity.User;
import com.streamlined.springhibernatetask.repository.UserRepository;

public abstract class UserServiceImpl {

    protected UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected void setNextUsernameSerial(User user) {
        Optional<String> serial = getMaxUsernameSerial(user);
        String nextSerial = getNextSerial(serial);
        setUsernameSerial(user, nextSerial);
    }

    private Optional<String> getMaxUsernameSerial(User user) {
        return userRepository.getMaxUsername(user.getFirstName(), user.getLastName())
                .map(userName -> getUsernameSerial(user, userName));
    }

    private String getNextSerial(Optional<String> serial) {
        return serial.map(value -> value.isBlank() ? "1" : Long.toString(Long.parseLong(value) + 1)).orElse("");
    }

    private void setUsernameSerial(User user, String serial) {
        user.setUserName(getInitialUsername(user) + serial);
    }

    private String getInitialUsername(User user) {
        return user.getFirstName() + "." + user.getLastName();
    }

    private String getUsernameSerial(User user, String userName) {
        return userName.substring(getInitialUsername(user).length());
    }

    protected <T extends User> T changeUserStatus(T user) {
        user.setActive(!user.isActive());
        return user;
    }

}
