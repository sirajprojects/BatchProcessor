package batch.com.csvtomysql.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import batch.com.csvtomysql.entity.Person;

@Component
public class BatchReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchReader.class);

	@Bean
	public FlatFileItemReader<Person> reader() {
		LOGGER.info("Starting to read CSV file.");

		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new ClassPathResource("customers.csv"));
		reader.setLineMapper(new DefaultLineMapper<Person>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("firstName", "lastName", "city");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
					{
						setTargetType(Person.class);
					}
				});
			}
		});

		LOGGER.info("Finished setting up CSV reader.");
		return reader;
	}

}
