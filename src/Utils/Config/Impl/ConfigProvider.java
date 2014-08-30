package Utils.Config.Impl;

import Utils.Config.IConfigProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Реализация контракта для работы с файлом конфига
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 21:29
 */
public class ConfigProvider implements IConfigProvider
{
    private Properties properties;

    /**
     * Инициализация необходимых переменных и настройка для запуска
     * @param fileName Путь к файлу с настройками
     * @throws IOException Если возникнет ошика ввода-вывода при работе с файлом
     */
    public void Initialize(String fileName) throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(fileName));
    }

    /**
     * Метод для получения свойства из файла настроек по имени
     * @param property Имя свойства
     * @param defaultValue Значение по-умолчанию если свойство не будет найдено в файле
     */
    public String getProperty(String property, String defaultValue) {
        return properties.getProperty(property, defaultValue);
    }

    /**
     * Метод для получения свойства из файла настроек по имени
     * @param property Имя свойства
     */
    public String getProperty(String property) {
        return properties.getProperty(property);
    }
}
