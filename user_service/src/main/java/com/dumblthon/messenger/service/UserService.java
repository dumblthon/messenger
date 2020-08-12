package com.dumblthon.messenger.service;

import com.dumblthon.messenger.repository.UserRepository;
import com.dumblthon.messenger.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


}
