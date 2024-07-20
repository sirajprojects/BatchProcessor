package batch.com.csvtomysql.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import batch.com.csvtomysql.entity.Person;
@Component
public class BatchProcessor implements ItemProcessor<Person, Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchProcessor.class);
    private static final String FILTER_CITY = "West Joyceport";

    @Override
    public Person process(Person person) throws Exception {
        LOGGER.info("Processing person: {}", person);

        if (FILTER_CITY.equals(person.getCity())) {
            LOGGER.info("Person {} matches filter criteria.", person);
            return person;
        } else {
            LOGGER.info("Person {} does not match filter criteria. Skipping.", person);
            return null;
        }
    }
}
