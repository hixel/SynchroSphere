package Import.Impl;

import Dao.IDao;
import Enums.TypeColumnName;
import Import.Classes.DepartmentHashObject;
import Import.IXmlImporter;
import XmlManager.IXMLManager;
import org.springframework.dao.DuplicateKeyException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Контракт для работы с файлом конфига
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 21:18
 */
public class XmlImporter implements IXmlImporter {
    private IDao dao;

    private IXMLManager xmlManager;

    /**
     * Конструктор с параметрами
     * @param dao Объект слоя доступа к данным
     */
    public XmlImporter(IDao dao, IXMLManager xmlManager)
    {
        this.dao = dao;
        this.xmlManager = xmlManager;
    }

    /**
     * Конструктор с параметрами
     * @param fileName Файл из которого берутся данные для синхронизации
     * @throws ParserConfigurationException Если произошел сбой при чтении и парсинге данных файла
     * @throws SAXException Если произошел сбой при чтении и парсинге данных файла
     * @throws IOException Если запись в файл завершилось с ошибкой
     * @throws SQLException Если подключение к СУБД невозможно или при операции над БД произошла ошибка
     */
    @Override
    public void Import(String fileName) throws ParserConfigurationException, SAXException,
            IOException, SQLException {
        // Получаем набор данных из файла
        Set<DepartmentHashObject> setFromFile = GetDataFromFile(fileName);

        // Получаем данные из БД
        Set<DepartmentHashObject> setFromDb = GetDataFromDb();

        // Список на CRUD-операции с объектами
        Map<DepartmentHashObject, String> objectsForSynchronization =
                new HashMap<DepartmentHashObject, String>();

        // Сравниваем два набора для последующей синхронизации

        // Формируем список на изменение и удаление
        for (Iterator<DepartmentHashObject> it = setFromDb.iterator(); it.hasNext(); ) {
            DepartmentHashObject tempObject = it.next();
            DepartmentHashObject objFromFile = GetElement(setFromFile, tempObject);
            if (setFromFile.contains(tempObject))
            {
                // Проверяем был ли изменен объект
                if (!objFromFile.getDescription().equals(tempObject.getDescription()))
                {
                    // Сохраняем в список на изменение
                    objectsForSynchronization.put(objFromFile, "update");
                }
            }
            else
            {
                // Если элемента нет в файле заносим в список на удаление
                objectsForSynchronization.put(tempObject, "delete");
            }
        }

         // Формируем список на добавление записей
        setFromDb.retainAll(setFromFile);
        setFromFile.removeAll(setFromDb);
        for (Iterator<DepartmentHashObject> it = setFromFile.iterator(); it.hasNext(); ) {
            DepartmentHashObject tempObject = it.next();

            objectsForSynchronization.put(tempObject, "insert");
        }

        dao.OpenConnection();
        dao.Execute(objectsForSynchronization);
    }

    private DepartmentHashObject GetDepartment(Node departmentNode)
    {
        String depCode = "";
        String depJob = "";
        String description = "";

        NodeList departChildNodes = departmentNode.getChildNodes();
        for (int i = 0; i < departChildNodes.getLength(); i++)
        {
            Node fieldNode = departChildNodes.item(i);
            if (fieldNode.getNodeType() == Node.ELEMENT_NODE)
            {
                String nodeName = fieldNode.getNodeName();
                if (nodeName.equals("DepCode"))
                {
                    depCode = fieldNode.getTextContent();
                }
                else if (nodeName.equals("DepJob"))
                {
                    depJob = fieldNode.getTextContent();
                }
                else if (nodeName.equals("Description"))
                {
                    description = fieldNode.getTextContent();
                }
            }
        }

        return new DepartmentHashObject(depCode, depJob, description);
    }

    private Set<DepartmentHashObject> GetDataFromDb() throws SQLException
    {
        Set<DepartmentHashObject> result = new HashSet<DepartmentHashObject>();

        try
        {
            dao.OpenConnection();

            ResultSet resultSet = dao.GetData();

            ResultSetMetaData metaData  = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();

            String columnName;
            String value;
            String depCode = "";
            String depJob = "";
            String description = "";

            while (resultSet.next())
            {
                for (int i = 1; i <= colCount; i++)
                {
                    columnName = metaData.getColumnName(i);
                    Object objectValue = resultSet.getObject(i);
                    value = objectValue == null ? "" : objectValue.toString();

                    TypeColumnName column = TypeColumnName.valueOf(columnName);
                    switch (column)
                    {
                        case DEP_CODE:
                            depCode = value;
                            break;
                        case DEP_JOB:
                            depJob = value;
                            break;
                        case DESCRIPTION:
                            description = value;
                            break;
                    }
                }

                result.add(new DepartmentHashObject(depCode, depJob, description));
            }
        }
        finally {
            dao.CloseConnection();
        }

        return result;
    }

    private Set<DepartmentHashObject> GetDataFromFile(String fileName)
            throws ParserConfigurationException, SAXException, IOException
    {
        Document doc = xmlManager.ImportFromXmlFile(new File(fileName));

        Set<DepartmentHashObject> result = new HashSet<DepartmentHashObject>();

        NodeList departmentNodes = doc.getChildNodes().item(0).getChildNodes();
        for (int i = 0; i < departmentNodes.getLength(); i++) {
            Node departmentNode = departmentNodes.item(i);
            if (departmentNode.getNodeType() == Node.ELEMENT_NODE) {
                DepartmentHashObject hashObject = this.GetDepartment(departmentNode);

                if (result.contains(hashObject))
                {
                    // Проверяем на коллизию
                    for (Iterator<DepartmentHashObject> it = result.iterator(); it.hasNext(); ) {
                        DepartmentHashObject tempObject = it.next();
                        if (tempObject.equals(hashObject))
                        {
                            throw new DuplicateKeyException("В исходном наборе существует более одной " +
                                    "записи с одинаковым натуральным ключом");
                        }
                    }
                }

                result.add(hashObject);
            }
        }

        return result;
    }

    private DepartmentHashObject GetElement(Set<DepartmentHashObject> set,
                                            DepartmentHashObject object)
    {
        for (Iterator<DepartmentHashObject> it = set.iterator(); it.hasNext(); ) {
            DepartmentHashObject tempObject = it.next();
            if (tempObject.equals(object))
            {
                return tempObject;
            }
        }

        return null;
    }
}
