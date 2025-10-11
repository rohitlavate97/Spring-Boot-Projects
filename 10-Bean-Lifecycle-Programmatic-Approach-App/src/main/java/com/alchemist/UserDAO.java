package com.alchemist;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class UserDAO implements InitializingBean,DisposableBean{
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Getting db connection.....");
	}
	
	public void getData() {
		System.out.println("Getting the data from thd db...");
	}
	
	public void destroy() throws Exception{
		System.out.println("Closing db connection.....");
	}

}
