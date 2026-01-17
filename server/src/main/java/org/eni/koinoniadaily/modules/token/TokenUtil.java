package org.eni.koinoniadaily.modules.token;

import java.security.SecureRandom;

public final class TokenUtil {

  private static final SecureRandom random = new SecureRandom();

  public static String generateOtp() {
    
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i <= 5; i++) {
      int randomIndex = random.nextInt(10);
      sb.append(randomIndex);
    }

    return sb.toString();
  }
}
