package com.OrgComm.OrgComm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication(scanBasePackages = "com.OrgComm.OrgComm")
public class OrgCommApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(OrgCommApplication.class, args);
	}

}
