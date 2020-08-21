package com.mesalab.common.dto.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date: 2020-08-05 14:03
 * @Author : liuyongqiang
 * @ClassName : DemoParam
 * @Description : DemoParam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoResult {

    public String userName;
    public String passWord;
    public String address;

}
