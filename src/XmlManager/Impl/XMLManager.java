package XmlManager.Impl;

import Entities.BaseEntity;
import Entities.Department;
import Entities.Departments;
import XmlManager.IXMLManager;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

/**
 * Реализация контракта для работы с XML
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 21:47
 */
public class XMLManager<E extends BaseEntity> implements IXMLManager<E> {

    /**
     * Экспорт данных в файл в виде XML
     * @param entityList Набор данных для сохранения
     * @param file Путь к файлу для сохранения
     * @throws IOException Если запись в файл завершилось с ошибкой
     * @throws JAXBException Если преобразование данных в XML завершилось с ошибкой
     */
    @Override
    public void ExportToXmlFile(List<E> entityList, File file) throws IOException, JAXBException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        JAXBContext context = JAXBContext.newInstance(Departments.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(new Departments((List<Department>)entityList), writer);

        writer.close();
    }

    /**
     * Импорт данных из файла в виде XML
     * @param file Путь к файлу для сохранения
     * @throws ParserConfigurationException Если получение данных завершилось с ошибкой
     * @throws IOException Если возникнет ошика ввода-вывода при работе с файлом
     * @throws SAXException Если произошел сбой при чтении и парсинге данных файла
     */
    @Override
    public Document ImportFromXmlFile(File file) throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        return builder.parse(file);
    }
}
