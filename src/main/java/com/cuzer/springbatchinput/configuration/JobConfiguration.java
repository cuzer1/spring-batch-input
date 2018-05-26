package com.cuzer.springbatchinput.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cuzer.springbatchinput.reader.StatelessItemReader;

@Configuration
public class JobConfiguration {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public StatelessItemReader statelessItemReader() {
		List<String> data = new ArrayList<>(3);
		
		data.add("Cengiz");
		data.add("Cigdem");
		data.add("Ada");	
		return new StatelessItemReader(data);
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<String, String>chunk(4)
				.reader(statelessItemReader())
				.writer(
						list -> {
							for (String curItem : list) {
								System.out.println("current Item= " + curItem);
							}
						}
						)
				.build();
	}
	
	
	@Bean
	public Job Job1() {
		return jobBuilderFactory.get("FirstInputJob2")
				.start(step1())
				.build();
	}

}
