package cz.dan.fetcher;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "cz.dan")
@ConfigurationPropertiesScan
@EnableEncryptableProperties
@EnableFeignClients
public class FetcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetcherApplication.class, args);
	}

}
