package XmlManager;

import Entities.BaseEntity;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Контракт для работы с XML
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 19:58
 */
public interface IXMLManager<T extends BaseEntity> {
    void ExportToXmlFile(List<T> entityList, File file) throws IOException, JAXBException;
    Document ImportFromXmlFile(File file)  throws ParserConfigurationException, IOException, SAXException;
}
