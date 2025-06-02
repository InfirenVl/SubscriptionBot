package dev.infiren.SubscriptionBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableScheduling
@SpringBootApplication
public class SubscriptionBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionBotApplication.class, args);
	}

}
