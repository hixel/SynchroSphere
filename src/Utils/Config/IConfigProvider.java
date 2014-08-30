package Utils.Config;

import java.io.IOException;

/**
 * Контракт для работы с файлом конфига
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 21:18
 */
public interface IConfigProvider {
    void Initialize(String fileName) throws IOException;
    String getProperty(String property, String defaultValue);
    String getProperty(String property);
}
