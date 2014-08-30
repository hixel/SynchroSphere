package Dao;

import Import.Classes.DepartmentHashObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Контракт слоя доступа к данным
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 20:10
 */
public interface IDao {
    void OpenConnection() throws SQLException;
    ResultSet GetData() throws SQLException;
    void Execute(Map<DepartmentHashObject, String> dataMap) throws SQLException;
    void CloseConnection();
}
