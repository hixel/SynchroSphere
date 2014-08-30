package Utils;

import Enums.TypeXMLOperation;
import Helpers.AttributeHelper;

import javax.swing.*;

/**
 * Класс для обработки аргументов запуска приложения
 * @author Семин Игорь
 * Date: 13.01.14
 * Time: 20:06
 */
public class ArgumentHandler {
    /**
     * Класс для обработки аргументов запуска приложения
     * @param args Аргументы запуска приложения
     * @return  AttributeHelper Объект с необходимыми аргументами
     */
    public static AttributeHelper GetAttributes(String[] args)
    {
        if (args.length == 0)
        {
            throw new IllegalArgumentException("Для запуска приложения необходимо задать атрибуты");
        }

        if (args.length < 2)
        {
            throw new IllegalArgumentException("Не заданы все необходимые атрибуты");
        }

        AttributeHelper attributeHelper = new AttributeHelper();
        for (int i = 0; i <= args.length - 1; i++)
        {
            switch (i)
            {
                case 0:
                    if (args[i].equals("import"))
                    {
                        attributeHelper.TypeXMLOperation = TypeXMLOperation.Import;
                    }
                    else if (args[i].equals("export"))
                    {
                        attributeHelper.TypeXMLOperation = TypeXMLOperation.Export;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Неизвестный атрибут \"Тип операции\"");
                    }
                    break;
                case 1:
                    if (args[i].isEmpty())
                    {
                        throw new IllegalArgumentException("Необходимо задать имя файла");
                    }
                    attributeHelper.FileName = args[i];
                    break;
            }
        }

        return attributeHelper;
    }
}
