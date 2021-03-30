package main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private final String XML_FILE_PATH = "src\\main\\resources\\example.xml";
    private final String FIELD1_NAME = "name";
    private final String FIELD2_NAME = "job";

    private ArrayList<Employee> employeesList = new ArrayList<>();

    private class XMLHandler extends DefaultHandler  {

        private String lastElementName;
        private String name;
        private String job;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            lastElementName = qName;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ( (name != null && !name.isEmpty()) && (job != null && !job.isEmpty()) ) {
                employeesList.add(new Employee(name, job));
                name = null;
                job = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);
            information = information.replace("\n","").trim();
            if (!information.isEmpty()) {
                if (lastElementName.equals(FIELD1_NAME))
                    name = information;
                if (lastElementName.equals(FIELD2_NAME))
                    job = information;
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        // парсим и выводим на экран всех сотрудников
        XMLHandler handler = new XMLHandler();
        parser.parse(new File(XML_FILE_PATH), handler);

        employeesList.forEach(System.out::println);

    }

}