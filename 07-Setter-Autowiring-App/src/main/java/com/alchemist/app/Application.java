package com.alchemist.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.alchemist.AppConfig;
import com.alchemist.beans.ReportService;

public class Application {
	public static void main(String[] args) {
		System.out.println("Spring Qualifier Annotation Example");
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		ReportService service=context.getBean(ReportService.class);
		service.generateReport();
		System.out.println("Done");
	}

}
