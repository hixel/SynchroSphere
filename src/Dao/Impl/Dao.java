package Dao.Impl;

import Dao.IDao;
import Import.Classes.DepartmentHashObject;
import Utils.Config.IConfigProvider;

import java.sql.*;
import java.util.Map;

/**
 * Реализация слоя доступа к данным
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 20:10
 */
public class Dao implements IDao
{
    //region Dependency Injection members

    private IConfigProvider configProvider;

    public void setConfigProvider(IConfigProvider configProvider){
        this.configProvider = configProvider;
    }

    public IConfigProvider getConfigProvider(){
        return configProvider;
    }

    //endregion

    Connection connection;
    Statement statement;

    /**
     * Открывает соединение с СУБД
     * @throws SQLException Если подключение к СУБД невозможно
     */
    public void OpenConnection() throws SQLException
    {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        String url = configProvider.getProperty("connectionstring");
        String user = configProvider.getProperty("dbuser");
        String password = configProvider.getProperty("dbpassword");

        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    /**
     * Получение набора данных через выполнение запроса к СУБД
     * @return Набор данных
     * @throws SQLException Если запрос к СУБД не смог выполниться
     */
    public ResultSet GetData() throws SQLException
    {
        return statement.executeQuery("select DEP_CODE, DEP_JOB, DESCRIPTION from DEPARTMENT");
    }

    /**
     * Выполнение CRUD-операций в БД
     * @param dataMap Список объектов и операций над ними
     * @throws SQLException Если запрос к СУБД не смог выполниться
     */
    public void Execute(Map<DepartmentHashObject, String> dataMap) throws SQLException
    {
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);

            for (Map.Entry<DepartmentHashObject, String> entry : dataMap.entrySet())
            {
                if (entry.getValue().equals("insert"))
                {
                    preparedStatement =
                            connection.prepareStatement("INSERT INTO DEPARTMENT(DEP_CODE, DEP_JOB, DESCRIPTION)" +
                                    "VALUES(?, ?, ?)");

                    DepartmentHashObject newDepartment = entry.getKey();
                    preparedStatement.setString(1, newDepartment.getDepCode());
                    preparedStatement.setString(2, newDepartment.getDepJob());
                    preparedStatement.setString(3, newDepartment.getDescription());

                    preparedStatement.executeUpdate();
                }
                else if (entry.getValue().equals("update"))
                {
                    preparedStatement =
                            connection.prepareStatement("UPDATE DEPARTMENT SET DESCRIPTION = ? " +
                                    "WHERE DEP_CODE = ? AND DEP_JOB = ?");

                    DepartmentHashObject newDepartment = entry.getKey();
                    preparedStatement.setString(1, newDepartment.getDescription());
                    preparedStatement.setString(2, newDepartment.getDepCode());
                    preparedStatement.setString(3, newDepartment.getDepJob());

                    preparedStatement.executeUpdate();
                }
                else if (entry.getValue().equals("delete"))
                {
                    preparedStatement =
                            connection.prepareStatement("DELETE FROM DEPARTMENT " +
                                    "WHERE DEP_CODE = ? AND DEP_JOB = ?");

                    DepartmentHashObject newDepartment = entry.getKey();
                    preparedStatement.setString(1, newDepartment.getDepCode());
                    preparedStatement.setString(2, newDepartment.getDepJob());

                    preparedStatement.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e ) {
            if (connection != null) {
                connection.rollback();
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            connection.setAutoCommit(true);
        }
    }

    /**
     * Закрывает соединение с СУБД
     * @throws SQLException Если соединения по техническим причинам закрыть невозможно
     */
    public void CloseConnection()
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            connection = null;
        }
    }
}
