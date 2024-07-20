package batch.com.csvtomysql.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import batch.com.csvtomysql.entity.Person;

public class PdfItemWriter implements ItemWriter<Person> {

	private static final String OUTPUT_PATH = "OUTPUT/output.pdf";

	@Override
	public void write(List<? extends Person> items) throws Exception {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_PATH));
			document.open();
			for (Person person : items) {
				document.add(new Paragraph(person.toString()));
			}
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating PDF", e);
		} finally {
			document.close();
		}
	}
}
