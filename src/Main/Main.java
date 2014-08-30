package Main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Класс точки входа в приложение
 * @author Семин Игорь
 * Date: 12.01.14
 * Time: 23:55
 */
public class Main {
    public static void main(String[] args)
    {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(new String[] {"spring-config.xml"});

        Application application = (Application)context.getBean("application");

        application.Initialize(context);
        application.Start(args);
    }
}
