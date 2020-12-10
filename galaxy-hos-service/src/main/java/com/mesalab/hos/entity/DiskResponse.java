package com.mesalab.hos.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by wk on 2020/4/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)

// XML文件中的根标识
@XmlRootElement(name = "HosSpace")
// 控制JAXB 绑定类中属性和字段的排序

@XmlType(propOrder = {
        "HosCapacity",
        "HosUsed"
})
public class DiskResponse implements Serializable {

    private static final long serialVersionUID = 2L;

    private Long HosCapacity;
    private Long HosUsed;

    public DiskResponse() {
        super();
    }


    public DiskResponse(Long hosCapacity, Long hosUsed) {
        super();
        this.HosCapacity = hosCapacity;
        this.HosUsed = hosUsed;

    }



    public Long getHosCapacity() {
        return HosCapacity;
    }

    public void setHosCapacity(Long hosCapacity) {
        HosCapacity = hosCapacity;
    }

    public Long getHosUsed() {
        return HosUsed;
    }

    public void setHosUsed(Long hosUsed) {
        HosUsed = hosUsed;
    }
}
