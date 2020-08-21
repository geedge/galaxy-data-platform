package com.mesalab.common.dto.results;

import com.mesalab.common.enums.ResultStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 响应结果
 * @author dazzlzy
 * @date 2018/3/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResult<T> implements Serializable {

    private Integer status;

    private String code;

    private boolean success;

    private String message;

    private Map<String, Object> statistics;

    private String formatType;

    private T meta;

    private T data;


    /**
     * 判断是否是成功结果
     * JsonIgnore使之不在json序列化结果当中
     *
     * @return 是否为成功结果
     */
    public boolean isSuccess() {
        return ResultStatusEnum.SUCCESS.getCode() == this.status;
    }

}