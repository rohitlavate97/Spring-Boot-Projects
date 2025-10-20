package com.alchemist.binding;



import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@JacksonXmlRootElement(localName = "book")
//XmlRootElement
@Data
public class Book {
	private Integer id;
	private String name;
	private Double price;

}
