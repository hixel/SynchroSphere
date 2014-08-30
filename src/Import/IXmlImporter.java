package Import;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Контракт импортера данных из XML
 * @author Семин Игорь
 * Date: 14.01.14
 * Time: 23:25
 */
public interface IXmlImporter {
    void Import(String fileName) throws ParserConfigurationException,
            SAXException, IOException, SQLException;
}
