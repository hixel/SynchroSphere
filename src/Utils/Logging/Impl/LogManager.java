package Utils.Logging.Impl;

import Utils.Logging.ILogManager;
import org.apache.log4j.Logger;

/**
 * Реализация контракта для работы лога
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 19:58
 */
public class LogManager implements ILogManager {
    private Logger log;

    /**
     * Инициализация необходимых переменных и задание основного логгера приложения
     */
    public void Initialize()
    {
        log = Logger.getLogger(LogManager.class);
    }

    /**
     * Инициализация необходимых переменных и задание основного логгера приложения
     * @return Logger Реализация логгера
     */
    public Logger GetLogger()
    {
        return this.log;
    }
}
