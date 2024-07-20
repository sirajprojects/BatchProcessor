package batch.com.csvtomysql.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import batch.com.csvtomysql.entity.Person;
import batch.com.csvtomysql.processor.BatchProcessor;
import batch.com.csvtomysql.reader.BatchReader;
import batch.com.csvtomysql.writer.BatchWriter;
import batch.com.csvtomysql.writer.PdfItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private BatchReader reader;

	@Autowired
	private BatchProcessor processor;

	@Autowired
	private BatchWriter writer;

	@Autowired
	private DataSource dataSource;

	@Bean
	public Job importUserJob() {
		LOGGER.info("Setting up job.");

		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).flow(step1())
				.next(generatePdfStep()).end().build();
	}

	@Bean
	public Step step1() {
		LOGGER.info("Setting up step.");

		return stepBuilderFactory.get("step1").<Person, Person>chunk(10000).reader(reader.reader())// .processor(processor)
				.writer(writer).build();
	}

	@Bean
	public Step generatePdfStep() {
		return stepBuilderFactory.get("generatePdfStep").<Person, Person>chunk(10000).reader(dbReader(dataSource))
				.writer(pdfItemWriter()).build();
	}

	@Bean
	public JdbcCursorItemReader<Person> dbReader(DataSource dataSource) {
		LOGGER.info("Initializing JdbcCursorItemReader with SQL query to fetch Person data.");
		JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT first_name, last_name, city FROM Person");
		reader.setRowMapper(new BeanPropertyRowMapper<>(Person.class));
		LOGGER.info("JdbcCursorItemReader initialized with SQL: {}", reader.getSql());

		return reader;
	}

	@Bean
	public PdfItemWriter pdfItemWriter() {
		return new PdfItemWriter();
	}

}
