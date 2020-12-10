package com.mesalab.hos.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2020/3/10.
 */
@Configuration
@EnableConfigurationProperties(HbaseProperties.class)
public class HbaseFactory {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    private final HbaseProperties hbproperties;

    @Autowired
    public HbaseFactory(HbaseProperties hbproperties) {
        this.hbproperties = hbproperties;
    }

    @Bean(name = "hbaseConfiguration")
    public org.apache.hadoop.conf.Configuration getConfiguration() {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", hbproperties.getZookeeper_quorum());
        conf.set("hbase.zookeeper.property.clientPort", hbproperties.getZookeeper_property_clientPort());
        conf.set("zookeeper.znode.parent", hbproperties.getZookeeper_znode_parent());
        conf.set("hbase.client.retries.number", hbproperties.getClient_retries_number());
        conf.set("hbase.rpc.timeout", hbproperties.getRpc_timeout());
        conf.set("hbase.client.keyvalue.maxsize", hbproperties.getClient_keyvalue_maxsize());
        conf.set("hbase.client.write.buffer", hbproperties.getClient_write_buffer());
        conf.set("hbase.client.ipc.pool.size", hbproperties.getConnect_pool().toString());
        return conf;
    }

    @Bean(name = "hbaseConnection")
    public Connection getConnection(@Qualifier("hbaseConfiguration") org.apache.hadoop.conf.Configuration conf) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.createConnection(conf);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return conn;
    }

    @Bean(name = "hbaseExceptionListener")
    public BufferedMutator.ExceptionListener getLishtener(@Qualifier("hbaseConnection") Connection conn) {

        BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {

            public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator mutator) {
                for (int i = 0; i < e.getNumExceptions(); i++) {
                    logger.error("Failed to sent put " + e.getRow(i) + ".");
                }
            }
        };
        return listener;
    }

}
