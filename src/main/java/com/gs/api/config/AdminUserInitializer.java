package com.gs.api.config;

import com.gs.api.enums.Role;
import com.gs.api.model.User;
import com.gs.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@admin.com")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("123456aB!"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("user admin criado");
        } else {
            log.info("user admin ja existe");
        }
    }
}
