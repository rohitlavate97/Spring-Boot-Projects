package com.alchemist.binding;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class UnMarshalDemo {
	public static void main(String[] args) {
		File f = new File("Customer.xml");
		try {
			JAXBContext context = JAXBContext.newInstance(Customer.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Object unmarshal = unmarshaller.unmarshal(f);
			Customer c = (Customer)unmarshal;
			System.out.println(c);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
