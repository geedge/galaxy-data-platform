package com.mesalab.hos.entity.delete;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wk on 2020/4/23.
 */
// XML文件中的根标识
@XmlRootElement(name = "DeleteResult")
// 控制JAXB 绑定类中属性和字段的排序
public class DeleteResponse {


    private List<DeleteError> errors;

    @XmlElement(name = "Error")
    public List<DeleteError> getErrors() {
        return errors;
    }

    public void setErrors(List<DeleteError> errors) {
        this.errors = errors;
    }


}
