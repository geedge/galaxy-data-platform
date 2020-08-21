package com.mesalab.engine.component.strategy;

import com.mesalab.common.knowledge.KnowDslObject;

/**
 * @Description: 提供查询结果
 */
public interface QueryProvider {

    Object query(KnowDslObject.DSLQuery dslQeruy);

}
