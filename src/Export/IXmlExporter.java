package Export;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Контракт экспортера данных из XML
 * @author Семин Игорь
 * Date: 12.01.14
 * Time: 23:48
 */
public interface IXmlExporter {
    void Export(String fileName) throws SQLException, JAXBException, IOException;
}
