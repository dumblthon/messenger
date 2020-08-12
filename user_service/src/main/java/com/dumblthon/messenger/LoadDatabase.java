package com.dumblthon.messenger;

import com.dumblthon.messenger.entities.User;
import com.dumblthon.messenger.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            User u = new User();
            u.setName("foo");
            u.setEmail("foo@co.com");
            repository.save(u);
        };
    }
}
