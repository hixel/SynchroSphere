package Export.Impl;

import Dao.IDao;
import Entities.Department;
import Enums.TypeColumnName;
import Export.IXmlExporter;
import XmlManager.IXMLManager;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация экспортера данных из XML
 * @author Семин Игорь
 * Date: 12.01.14
 * Time: 17:21
 */
public class XmlExporter implements IXmlExporter {
    private IDao dao;

    private IXMLManager xmlManager;

    public XmlExporter(IDao dao, IXMLManager xmlManager)
    {
        this.dao = dao;
        this.xmlManager = xmlManager;
    }

    /**
     * Экспорт данных из XML
     * @param fileName Выходной файл, в который будет записан набор данных
     * @throws SQLException Если подключение к СУБД невозможно
     * @throws JAXBException Если преобразование данных в XML завершилось с ошибкой
     * @throws IOException Если запись в файл завершилось с ошибкой
     */
    public void Export(String fileName) throws SQLException, JAXBException, IOException
    {
        try
        {
            dao.OpenConnection();

            ResultSet resultSet = dao.GetData();

            ResultSetMetaData metaData  = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();

            String columnName;
            String value;
            List<Department> departments = new ArrayList<Department>();
            while (resultSet.next())
            {
                Department department = new Department();

                for (int i = 1; i <= colCount; i++)
                {
                    columnName = metaData.getColumnName(i);
                    Object objectValue = resultSet.getObject(i);
                    value = objectValue == null ? "" : objectValue.toString();

                    TypeColumnName column = TypeColumnName.valueOf(columnName);
                    switch (column)
                    {
                        case DEP_CODE:
                            department.DepCode = value;
                            break;
                        case DEP_JOB:
                            department.DepJob = value;
                            break;
                        case DESCRIPTION:
                            department.Description = value;
                            break;
                    }
                }

                departments.add(department);
            }

            if (departments.size() > 0)
            {
                xmlManager.ExportToXmlFile(departments, new File(fileName));
            }
        }
        finally {
            dao.CloseConnection();
        }
    }
}
