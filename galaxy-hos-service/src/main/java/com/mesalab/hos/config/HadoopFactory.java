package com.mesalab.hos.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2020/3/10.
 */
@Configuration
@EnableConfigurationProperties(com.mesalab.hos.config.HbaseProperties.class)
@ConditionalOnProperty(value = "hbase.standone", havingValue = "1")
public class HadoopFactory {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    private final com.mesalab.hos.config.HbaseProperties hbproperties;

    @Autowired
    public HadoopFactory(com.mesalab.hos.config.HbaseProperties hbproperties) {
        this.hbproperties = hbproperties;
    }

    @Bean(name = "hadoopConfiguration")
    public org.apache.hadoop.conf.Configuration getHadoopConfiguration() {
        org.apache.hadoop.conf.Configuration config = new org.apache.hadoop.conf.Configuration();
        config.set("fs.defaultFS","hdfs://ns1");
        config.set("hadoop.proxyuser.root.hosts","*");
        config.set("hadoop.proxyuser.root.groups","*");
        config.set("ha.zookeeper.quorum",hbproperties.getZookeeper_quorum()+":"+hbproperties.getZookeeper_property_clientPort());
        config.set("dfs.nameservices","ns1");
        config.set("dfs.ha.namenodes.ns1","nn1,nn2");
        config.set("dfs.namenode.rpc-address.ns1.nn1",hbproperties.getNameNodes().get(0)+":9000");
        config.set("dfs.namenode.rpc-address.ns1.nn2",hbproperties.getNameNodes().get(1)+":9000");
        config.set("dfs.namenode.http-address.ns1.nn1",hbproperties.getNameNodes().get(0)+":50070");
        config.set("dfs.namenode.http-address.ns1.nn2",hbproperties.getNameNodes().get(1)+":50070");
        config.set("dfs.client.failover.proxy.provider.ns1","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        return config;
    }



}
