package com.alchemist;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component("userDao") //writing the name of the bean
public class UserDao implements InitializingBean,DisposableBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Getting data from db..");
		System.out.println("Storing into the redis..");
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Closing the connections..");
		
	}

}
