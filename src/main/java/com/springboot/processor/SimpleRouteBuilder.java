package com.springboot.processor;

import javax.jms.ConnectionFactory;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

import com.springboot.model.Employee;

public class SimpleRouteBuilder extends RouteBuilder {


	
	
	@Override
	public void configure() throws Exception {
		
		JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Employee.class);
		
		from("timer://test?period=90000").setHeader(Exchange.HTTP_METHOD, simple("GET"))
		.to("http://localhost:2019/employee?id=5").process(new MyProcessor());

		
		from("timer://test?period=50000").process(new CreateEmployeeProcessor()).marshal(jsonDataFormat)
				.setHeader(Exchange.HTTP_METHOD, simple("POST"))
				.setHeader(Exchange.CONTENT_TYPE, constant("application/json")).to("http://localhost:2019/employee")
				.process(new MyProcessor());
		
	}

	}

