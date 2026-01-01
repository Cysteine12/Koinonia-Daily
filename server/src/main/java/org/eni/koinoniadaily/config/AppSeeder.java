package org.eni.koinoniadaily.config;

import java.time.LocalDateTime;
import java.util.List;

import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.TeachingType;
import org.eni.koinoniadaily.modules.user.User;
import org.eni.koinoniadaily.modules.user.UserRepository;
import org.eni.koinoniadaily.modules.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Profile("development")
@Component
@RequiredArgsConstructor
public class AppSeeder implements CommandLineRunner {

  private final UserRepository userRepository;
  private final TeachingRepository teachingRepository;
  private final PasswordEncoder passwordEncoder;
  private static final Logger logger = LoggerFactory.getLogger(AppSeeder.class);
  
  @Override
  public void run(String... args) throws Exception {

    if (userRepository.count() == 0) {
      logger.info("User table seeding started");

      List<User> users = List.of(
          User.builder()
            .firstName("Test")
            .lastName("Admin")
            .email("testadmin@gmail.com")
            .password(passwordEncoder.encode("test1234"))
            .isVerified(true)
            .role(UserRole.ADMIN)
            .build(),
          User.builder()
            .firstName("Test")
            .lastName("User")
            .email("testuser@gmail.com")
            .password(passwordEncoder.encode("test1234"))
            .isVerified(true)
            .role(UserRole.USER)
            .build()
      );
      userRepository.saveAll(users);

      logger.info("User table seeding completed");
    }
    
    if (teachingRepository.count() == 0) {
      logger.info("Teaching table seeding started");

      List<Teaching> teachings = List.of(
          Teaching.builder()
            .title("Commanding the Supernatural Part 1")
            .message("This series we are going to look into the dynamics of faith in establishing the ordinances of the kingdom here on earth.")
            .scripturalReferences("Hebrews 11:1-6; Mark 11:22-24; Matthew 17:20")
            .audioUrl("https://koinonia.org/yy78y8t979")
            .videoUrl("https://koinonia.org/yy78y2t839")
            .thumbnailUrl("https://koinonia.org/yy18y8t879")
            .tags("faith,supernatural,kingdom")
            .type(TeachingType.SUNDAY_SERVICE)
            .summary("This series we are going to look into the dynamics of faith in establishing the ordinances of the kingdom here on earth.")
            .taughtAt(LocalDateTime.now().minusDays(14))
            .build(),
          Teaching.builder()
            .title("Commanding the Supernatural Part 2")
            .message("In this message we continue to explore the dynamics of faith in establishing the ordinances of the kingdom here on earth.")
            .scripturalReferences("Hebrews 11:1-6; Mark 11:22-24; Matthew 17:20")
            .audioUrl("https://koinonia.org/aa78y8t979")
            .videoUrl("https://koinonia.org/aa78y2t839")
            .thumbnailUrl("https://koinonia.org/aa18y8t879")
            .tags("faith,supernatural,kingdom")
            .type(TeachingType.SUNDAY_SERVICE)
            .summary("In this message we continue to explore the dynamics of faith in establishing the ordinances of the kingdom here on earth.")
            .taughtAt(LocalDateTime.now().minusDays(7))
            .build()
      );
      teachingRepository.saveAll(teachings);

      logger.info("Teaching table seeding completed");
    }
  }
}
