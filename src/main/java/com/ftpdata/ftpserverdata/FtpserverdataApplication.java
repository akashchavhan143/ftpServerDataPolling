package com.ftpdata.ftpserverdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FtpserverdataApplication {

	public static void main(String[] args) {
		SpringApplication.run(FtpserverdataApplication.class, args);
	}

}
