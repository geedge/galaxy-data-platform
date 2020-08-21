package com.mesalab.engine.component.udf;

import com.mesalab.engine.component.bean.SqlQueryBean;

import java.util.List;

public interface UDF {

    Object execute(SqlQueryBean sqlQuerySource, List<Object> results);
}
