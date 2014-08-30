package Utils.Logging;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Контракт для работы лога
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 19:37
 */
public interface ILogManager {
    void Initialize();
    Logger GetLogger();
}
