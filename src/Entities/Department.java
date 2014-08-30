package Entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Сущность таблицы отдела
 * @author Семин Игорь
 * Date: 12.01.14
 * Time: 16:43
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Department extends BaseEntity {
    /** Код отдела */
    @XmlElement
    public String DepCode;

    /** Название должности в отделе */
    @XmlElement
    public String DepJob;

    /** Описание отдела */
    @XmlElement
    public String Description;
}
