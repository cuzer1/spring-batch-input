package com.cuzer.springbatchinput.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cuzer.springbatchinput.domain.Customer;
import com.cuzer.springbatchinput.domain.CustomerRowMapper;

@Configuration
public class DatabaseReadJobConfiguration {

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public DataSource dataSource;

//	@Bean
//	public JdbcCursorItemReader<Customer> jdbcCursorItemReader() {
//		JdbcCursorItemReader<Customer> reader = new JdbcCursorItemReader<>();
//
//		reader.setSql("select id, firstName, lastName, birthdate from customer order by lastName, firstName");
//		reader.setDataSource(this.dataSource);
//		reader.setRowMapper(new CustomerRowMapper());
//
//		return reader;
//	}
	
	
	@Bean
	public JdbcPagingItemReader<Customer> pagingItemReader() {
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(this.dataSource);
		reader.setFetchSize(10);
		reader.setRowMapper(new CustomerRowMapper());

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, firstName, lastName, birthdate");
		queryProvider.setFromClause("from customer");

		Map<String, Order> sortKeys = new HashMap<>(1);

		sortKeys.put("id", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);

		reader.setQueryProvider(queryProvider);

		return reader;
	}

	@Bean
	public ItemWriter<Customer> itemWriter() {
		return items -> {
			for (Customer item : items) {
				System.out.println(item.toString());
			}
		};
	}

//	@Bean
//	public Step step1() {
//		return stepBuilderFactory.get("step1").<Customer, Customer>chunk(10).reader(pagingItemReader())
//				.writer(itemWriter()).build();
//	}
//
//	@Bean
//	public Job job1() {
//		return jobBuilderFactory.get("dbJob2").start(step1()).build();
//	}

}
