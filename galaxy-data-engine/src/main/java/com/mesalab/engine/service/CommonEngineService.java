package com.mesalab.engine.service;

import com.mesalab.common.model.DSLObject;
import com.mesalab.common.dto.results.BaseResult;
import com.mesalab.common.exception.BusinessException;

/**
 * @Date: 2020-07-20 18:39
 * @Author : liuyongqiang
 * @ClassName : CommonEngineService
 * @Description : 公共查询接口定义
 */
public interface CommonEngineService {


    /**
     * @Description: 单表数据查询
     * @Author: liuyongqiang
     * @Date: 2020/7/21 9:41
     * @param dslObject: DSL查询对象
     * @return: com.mesalab.common.base.BaseResult
     **/
    BaseResult dslQuery(DSLObject dslObject)throws BusinessException;


    /**
     * @Description: 查询数据源下的所有表
     * @Author: liuyongqiang
     * @Date: 2020/7/23 15:16
     * @param dslObject:
     * @return: com.mesalab.common.base.BaseResult
     **/
    BaseResult tablesQuery(DSLObject dslObject)throws BusinessException;


    /**
     * @Description: 查询指定表中的所有字段
     * @Author: liuyongqiang
     * @Date: 2020/7/23 11:08
     * @param dslObject:
     * @return: com.mesalab.common.base.BaseResult
     **/
    BaseResult schemaQuery(DSLObject dslObject)throws BusinessException;


}

