package com.alchemist.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.alchemist.AppConfig;
import com.alchemist.beans.ReportService;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		ReportService reportService = context.getBean(ReportService.class);
		reportService.generateReport();
	}

}
