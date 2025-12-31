package org.eni.koinoniadaily.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Profile("!production")
@Component
@RequiredArgsConstructor
public class AppSeeder implements CommandLineRunner {

  public final TeachingRepository teachingRepository;
  public final ObjectMapper objectMapper;
  
  @Override
  public void run(String... args) throws Exception {

    System.out.println("Started seeding data");
    if (teachingRepository.count() < 1) {
      List<Teaching> teachings = readData("/data/teachings.json");

      teachingRepository.saveAll(teachings);
    } else {
      System.out.println("Data already present");
    }
    
    System.out.println("Data seeding completed");
  }

  private <T> List<T> readData(String resourceLocation) {

    try (InputStream inputStream = TypeReference.class.getResourceAsStream(resourceLocation)) {

        return objectMapper.readValue(inputStream, new TypeReference<List<T>>(){});
        
      } catch (IOException e) {
        throw new RuntimeException("Error seeding data: " + e.getMessage());
      }
  }
}
