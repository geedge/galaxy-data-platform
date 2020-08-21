package com.mesalab.common.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @Date: 2020-08-17 16:53
 * @Author : liuyongqiang
 * @ClassName : SchemaTypeEnum
 * @Description : SchemaTypeEnum
 */
@Getter
public enum SchemaTypeEnum {

    TABLES("tables"),
    FIELDS("fields");
    private String value;
    SchemaTypeEnum(String value) {this.value = value;}
}
