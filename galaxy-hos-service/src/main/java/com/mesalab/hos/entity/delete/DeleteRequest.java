package com.mesalab.hos.entity.delete;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wk on 2020/4/23.
 */
// XML文件中的根标识
@XmlRootElement(name = "Delete")
// 控制JAXB 绑定类中属性和字段的排序
public class DeleteRequest {


    private List<DeleteObj> Objects;
    private String Quiet;

    @XmlElement(name = "Object")
    public List<DeleteObj> getObjects() {
        return Objects;
    }

    public void setObjects(List<DeleteObj> objects) {
        Objects = objects;
    }

    @XmlElement(name = "Quiet")
    public String getQuiet() {
        return Quiet;
    }

    public void setQuiet(String quiet) {
        Quiet = quiet;
    }

}
