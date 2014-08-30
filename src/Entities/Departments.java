package Entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация экспортера данных из XML
 * @author Семин Игорь
 * Date: 12.01.14
 * Time: 17:21
 */
@XmlRootElement(name = "departments")
@XmlAccessorType(XmlAccessType.FIELD)
public final class Departments
{
    /** Контейнер сущности таблицы отделов */
    @XmlElement(name = "department", type = Department.class)
    private List<Department> departments = new ArrayList<Department>();

    /** Конструктор */
    public Departments() {}

    /**
     * Конструктор с параметрами
     * @param departments Контейнер сущности таблицы отделов
     */
    public Departments(List<Department> departments) {
        this.departments = departments;
    }

    /**
     * Получение контейнера сущности таблицы отделов
     * @return Контейнер сущности таблицы отделов
     */
    public List<Department> getDepartments() {
        return departments;
    }

    /**
     * Задание контейнера сущности таблицы отделов
     * @param departments Контейнер сущности таблицы отделов
     */
    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
