package com.cyberlanting.Assignments;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSwagger2Doc
@SpringBootApplication
public class AssignmentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentsApplication.class, args);
	}

}
