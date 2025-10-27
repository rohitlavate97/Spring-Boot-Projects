package com.alchemist.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.alchemist.model.Customer;
import com.alchemist.util.KafkaConstants;
import org.springframework.kafka.support.serializer.JsonSerializer;

import org.apache.kafka.common.serialization.StringSerializer;

@Configuration
public class KafkaProduceConfig {
	@Bean
	public ProducerFactory<String,Customer> producerFactory(){
		Map<String, Object> configProp = new HashMap<>();
		configProp.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.HOST);
		configProp.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);    //key-->TopicName
		configProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);    //value-->customerdata
		return new DefaultKafkaProducerFactory<>(configProp);
	}
	
	@Bean(name = "KafkaTemplate")
	public KafkaTemplate<String, Customer> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}

}
