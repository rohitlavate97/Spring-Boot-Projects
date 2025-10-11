package com.alchemist;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		UserDAO dao = context.getBean(UserDAO.class);
		dao.getData();
		/*If we add this much code-->destroy() will not be executed, as in in main method no code to 
		 * to execute-->JVM shutdown i.e main method terminated--->IOC container have not got chance 
		 * to remove the object. so in order to see execution of destroy() add following code */
		ConfigurableApplicationContext ctxt = (ConfigurableApplicationContext)context;
		ctxt.close();

	}

}
