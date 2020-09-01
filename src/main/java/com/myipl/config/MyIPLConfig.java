/*
 * package com.myipl.config;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.ComponentScan; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.jdbc.core.JdbcTemplate; import
 * org.springframework.jdbc.datasource.DriverManagerDataSource; import
 * org.springframework.scheduling.annotation.EnableScheduling; import
 * org.springframework.web.servlet.config.annotation.EnableWebMvc;
 * 
 * @Configuration
 * 
 * @EnableScheduling
 * 
 * @EnableWebMvc
 * 
 * @ComponentScan(basePackages = "com.myipl") public class MyIPLConfig {
 * 
 * 
 * @Bean public DataSource dataSource() { DriverManagerDataSource dataSource =
 * new DriverManagerDataSource();
 * dataSource.setDriverClassName("com.mysql.jdbc.Driver");
 * dataSource.setUrl("jdbc:mysql://localhost:3306/myipl?useSSL=true");
 * dataSource.setUsername("root"); dataSource.setPassword("Awes@123");
 * 
 * return dataSource; }
 * 
 * 
 * @Bean public DataSource dataSource() { DriverManagerDataSource dataSource =
 * new DriverManagerDataSource(); String url =
 * "jdbc:mysql://google/myipl?cloudSqlInstance=myipl-199419:asia-south1:myipl&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=Awes@123&useSSL=false";
 * dataSource.setDriverClassName("com.mysql.jdbc.Driver");
 * dataSource.setUrl(url); return dataSource; }
 * 
 * @Bean public JdbcTemplate jdbcTemplate() { JdbcTemplate jdbcTemplate = new
 * JdbcTemplate(); jdbcTemplate.setDataSource(dataSource()); return
 * jdbcTemplate; }
 * 
 * }
 */