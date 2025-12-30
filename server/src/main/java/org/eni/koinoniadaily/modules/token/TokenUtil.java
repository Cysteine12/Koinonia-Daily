package org.eni.koinoniadaily.modules.token;

import java.util.Random;

import org.springframework.stereotype.Component;

import java.lang.StringBuilder;

@Component
public class TokenUtil {

  public String generateOtp() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();

    char[] combinations = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    for (int i = 0; i <= 5; i++) {
      int randomIndex = random.nextInt(10);
      sb.append(combinations[randomIndex]);
    }

    return sb.toString();
  }
}
