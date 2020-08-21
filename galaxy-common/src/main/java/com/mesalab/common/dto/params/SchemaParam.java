package com.mesalab.common.dto.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date: 2020-08-17 16:45
 * @Author : liuyongqiang
 * @ClassName : SchemaParam
 * @Description : SchemaParam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaParam {

    private String type;

    private String name;

    private String tableName;

    private String key;

    public SchemaParam(String type, String name) {
        this.type = type;
        this.name = name;
    }


}
