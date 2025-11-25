package org.eni.koinonia_daily;

import org.eni.koinonia_daily.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class KoinoniaDailyApplication {

	public static void main(String[] args) {
		SpringApplication.run(KoinoniaDailyApplication.class, args);
	}

}
