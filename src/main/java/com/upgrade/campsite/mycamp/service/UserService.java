package com.upgrade.campsite.mycamp.service;

import com.upgrade.campsite.mycamp.model.User;
import com.upgrade.campsite.mycamp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
