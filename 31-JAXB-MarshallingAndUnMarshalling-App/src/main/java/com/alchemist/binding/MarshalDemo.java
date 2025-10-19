package com.alchemist.binding;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class MarshalDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Customer customer = new Customer();
		customer.setId(1);
		customer.setName("Prime");
		customer.setEmail("prime@co.in");
		customer.setPhno((long)9899);
		
		try {
			JAXBContext context = JAXBContext.newInstance(Customer.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(customer,new File("Customer.xml"));
			System.out.println("XML file is created");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
