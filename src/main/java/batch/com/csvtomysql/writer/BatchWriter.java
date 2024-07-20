package batch.com.csvtomysql.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import batch.com.csvtomysql.entity.Person;
import batch.com.csvtomysql.entity.PersonRepository;

@Component
public class BatchWriter implements ItemWriter<Person> {

	private final PersonRepository personRepository;

	public BatchWriter(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Override
	public void write(List<? extends Person> persons) throws Exception {
		personRepository.saveAll(persons);

	}

	@Bean
	public ItemWriter<Person> pdfWriter() {
		return new ItemWriter<Person>() {
			@Override
			public void write(List<? extends Person> items) throws Exception {
				File outputDir = new File("OUTPUT");
				if (!outputDir.exists()) {
					outputDir.mkdirs();
				}
				Document document = new Document();
				String outputPath = "OUTPUT/report.pdf";
				PdfWriter.getInstance(document, new FileOutputStream(outputPath));
				document.open();
				for (Person person : items) {
					document.add(new Paragraph(person.toString()));
				}
				document.close();

			}
		};
	}

}
