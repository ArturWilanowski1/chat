package pl.wilanowskiartur.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableAsync;

@EntityScan(
		basePackageClasses = {ServerchatApplication.class, Jsr310JpaConverters.class}
)
@EnableAsync
@SpringBootApplication
public class ServerchatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerchatApplication.class, args);
	}
}
