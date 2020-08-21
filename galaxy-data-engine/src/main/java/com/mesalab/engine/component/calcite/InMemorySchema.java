package com.mesalab.engine.component.calcite;

import com.mesalab.engine.component.calcite.storage.Storage;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.HashMap;
import java.util.Map;

public class InMemorySchema extends AbstractSchema {

    private String dbName;
    private Map<String, Object> operand;

    public InMemorySchema(String name, Map<String, Object> operand) {
        this.operand = operand;
        this.dbName = name;
    }

    @Override
    public Map<String, Table> getTableMap() {

        Map<String, Table> tables = new HashMap<String, Table>();

        Storage.getTables().forEach(it -> {
            tables.put(it.getName(), new InMemoryTable(it.getName(), it));
        });

        return tables;
    }

}
