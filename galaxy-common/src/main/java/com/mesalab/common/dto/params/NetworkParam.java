package com.mesalab.common.dto.params;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author wangwei
 * @version 1.0
 * @date 2020/6/30 1:52 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkParam {

    private String clientId;
    @Valid
    private QueryParam query;
}
