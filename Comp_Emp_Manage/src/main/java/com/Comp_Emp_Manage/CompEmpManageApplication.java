package com.Comp_Emp_Manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.Comp_Emp_Manage")
public class CompEmpManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompEmpManageApplication.class, args);
	}

}
