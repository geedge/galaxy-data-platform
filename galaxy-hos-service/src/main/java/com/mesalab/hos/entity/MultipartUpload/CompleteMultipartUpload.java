package com.mesalab.hos.entity.MultipartUpload;

import com.mesalab.hos.entity.delete.DeleteObj;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wk on 2020/4/23.
 */
// XML文件中的根标识
@XmlRootElement(name = "CompleteMultipartUpload")
// 控制JAXB 绑定类中属性和字段的排序
public class CompleteMultipartUpload {


    private List<Part> parts;

    @XmlElement(name = "Part")
    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
