package Main;

import Dao.IDao;
import Enums.TypeXMLOperation;
import Export.IXmlExporter;
import Export.Impl.XmlExporter;
import Helpers.AttributeHelper;
import Import.IXmlImporter;
import Import.Impl.XmlImporter;
import Utils.ArgumentHandler;
import Utils.Config.IConfigProvider;
import Utils.Logging.ILogManager;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;

import javax.swing.*;
import java.sql.*;
import java.util.Locale;

/**
 * Класс-обертка над приложением
 * @author Семин Игорь
 * Date: 12.01.14
 * Time: 23:07
 */
public class Application
{
    //region Dependency Injection members

    private ILogManager logManager;

    public void setLogManager(ILogManager logManager){
        this.logManager = logManager;
    }

    public ILogManager getLogManager(){
        return logManager;
    }

    private IConfigProvider configProvider;

    public void setConfigProvider(IConfigProvider configProvider){
        this.configProvider = configProvider;
    }

    public IConfigProvider getConfigProvider(){
        return configProvider;
    }

    //endregion

    ClassPathXmlApplicationContext context;

    /**
     * Инициализация необходимых переменных и настройка для запуска
     * @param context Контекст приложения для разрешения зависимостей
     */
    public void Initialize(ClassPathXmlApplicationContext context)
    {
        this.context = context;

        try
        {
            logManager.Initialize();
            configProvider.Initialize("config/config.properties");

            // Так как у меня бесплатная ORACLE XE - без этой настройки не получится подключиться
            Locale.setDefault(Locale.ENGLISH);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Ошибка инициализации приложения", JOptionPane.OK_OPTION);
        }
    }

    /**
     * Запуск приложения и обработка аргументов командной строки
     * @param args Аргументы командной строки
     */
    public void Start(String[] args)
    {
        TypeXMLOperation typeXmlOperation;
        String fileName = "";
        try
        {
            AttributeHelper attributeHelper = ArgumentHandler.GetAttributes(args);
            typeXmlOperation = attributeHelper.TypeXMLOperation;
            fileName = attributeHelper.FileName;
        }
        catch (IllegalArgumentException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Ошибка получения аргументов", JOptionPane.OK_OPTION);
            return;
        }

        try {
            logManager.GetLogger().info("Приложение запущено");

            switch (typeXmlOperation)
            {
                case Export:
                    IXmlExporter xmlExporter = (IXmlExporter)context.getBean("xmlExporter");
                    try
                    {
                        System.out.println(String.format("Экспорт данных в файл - %s", fileName));
                        logManager.GetLogger().info(String.format("Экспорт данных в файл - %s", fileName));

                        xmlExporter.Export(fileName);

                        logManager.GetLogger().info("Данные успешно экспортированы");
                    }
                    catch (Exception e)
                    {
                        logManager.GetLogger().error(e);
                    }
                    break;
                case Import:
                    IXmlImporter xmlImporter = (IXmlImporter)context.getBean("xmlImporter");
                    try
                    {
                        System.out.println("Синхронизация данных");
                        logManager.GetLogger().info("Синхронизация данных");

                        xmlImporter.Import(fileName);

                        logManager.GetLogger().info("Синхронизация данных завершена");
                    }
                    catch (DuplicateKeyException e)
                    {
                        System.out.println(e.getMessage());
                        logManager.GetLogger().error(e.getMessage());
                        return;
                    }
                    catch (Exception e)
                    {
                        logManager.GetLogger().error(e);
                    }
                    break;
            }
        } catch (Exception e) {
            logManager.GetLogger().error(e);
        }
    }
}
