package Import.Classes;


/**
 * Объект класса сущности отдела для операции синхронизации
 * @author Семин Игорь
 * Date: 14.01.14
 * Time: 18:25
 */
public class DepartmentHashObject {

    private final String depCode;


    private final String depJob;


    private final String description;

    /** Код отдела */
    public String getDepCode()
    {
        return this.depCode;
    }

    /** Название должности в отделе */
    public String getDepJob()
    {
        return this.depJob;
    }

    /** Описание отдела */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Конструктор с параметрами
     * @param depCode Код отдела
     * @param depJob Название должности в отделе
     * @param description Описание отдела
     * */
    public DepartmentHashObject(String depCode, String depJob, String description)
    {
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }

    /**
     * Получение хэш-значения объекта
     * @return Xэш-значение объекта
     */
    @Override
    public int hashCode() {
        return String.format("%s-%s", depCode, depJob).hashCode();
    }

    /**
     * Сравнивает объекты между собой
     * @return True - если объекты или их поля равны.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        {
            return true;
        }

        if (obj == null
            || getClass() != obj.getClass())
        {
            return false;
        }

        // Объекты считаются одинаковыми если натуральный ключ(поля DepCode и DepJob совпадают)
        DepartmentHashObject other = (DepartmentHashObject) obj;
        if (this.depCode.equals(other.depCode)
                && this.depJob.equals(other.depJob))
        {
            return true;
        }

        return false;
    }
}
