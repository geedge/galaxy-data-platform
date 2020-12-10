package com.mesalab.hos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by Administrator on 2020/3/10.
 */

@ConfigurationProperties(prefix = "hbase")
public class HbaseProperties {

    private String zookeeper_quorum;

    private String zookeeper_property_clientPort;

    private String zookeeper_znode_parent;

    private String client_pause;

    private String client_retries_number;

    private String rpc_timeout;

    private Integer connect_pool;

    private String time_index_table_prefix;

    private String filename_index_table_prefix;

    private String partfile_index_table_prefix;


    private String region_start_key;

    private String client_write_buffer;
    private String client_keyvalue_maxsize;

    private List<String> filename_head;

    private String hadoopConf;
    private String hbasePath;
    private int standone;
    private List<String> nameNodes;

    public String getZookeeper_quorum() {
        return zookeeper_quorum;
    }

    public void setZookeeper_quorum(String zookeeper_quorum) {
        this.zookeeper_quorum = zookeeper_quorum;
    }

    public String getZookeeper_property_clientPort() {
        return zookeeper_property_clientPort;
    }

    public void setZookeeper_property_clientPort(String zookeeper_property_clientPort) {
        this.zookeeper_property_clientPort = zookeeper_property_clientPort;
    }

    public String getZookeeper_znode_parent() {
        return zookeeper_znode_parent;
    }

    public void setZookeeper_znode_parent(String zookeeper_znode_parent) {
        this.zookeeper_znode_parent = zookeeper_znode_parent;
    }

    public String getClient_pause() {
        return client_pause;
    }

    public void setClient_pause(String client_pause) {
        this.client_pause = client_pause;
    }

    public String getClient_retries_number() {
        return client_retries_number;
    }

    public void setClient_retries_number(String client_retries_number) {
        this.client_retries_number = client_retries_number;
    }

    public String getRpc_timeout() {
        return rpc_timeout;
    }

    public void setRpc_timeout(String rpc_timeout) {
        this.rpc_timeout = rpc_timeout;
    }

    public Integer getConnect_pool() {
        return connect_pool;
    }

    public void setConnect_pool(Integer connect_pool) {
        this.connect_pool = connect_pool;
    }

    public String getTime_index_table_prefix() {
        return time_index_table_prefix;
    }

    public void setTime_index_table_prefix(String time_index_table_prefix) {
        this.time_index_table_prefix = time_index_table_prefix;
    }

    public String getFilename_index_table_prefix() {
        return filename_index_table_prefix;
    }

    public void setFilename_index_table_prefix(String filename_index_table_prefix) {
        this.filename_index_table_prefix = filename_index_table_prefix;
    }

    public String getPartfile_index_table_prefix() {
        return partfile_index_table_prefix;
    }

    public void setPartfile_index_table_prefix(String partfile_index_table_prefix) {
        this.partfile_index_table_prefix = partfile_index_table_prefix;
    }

    public String getRegion_start_key() {
        return region_start_key;
    }

    public void setRegion_start_key(String region_start_key) {
        this.region_start_key = region_start_key;
    }


    public String getClient_write_buffer() {
        return client_write_buffer;
    }

    public void setClient_write_buffer(String client_write_buffer) {
        this.client_write_buffer = client_write_buffer;
    }

    public String getClient_keyvalue_maxsize() {
        return client_keyvalue_maxsize;
    }

    public void setClient_keyvalue_maxsize(String client_keyvalue_maxsize) {
        this.client_keyvalue_maxsize = client_keyvalue_maxsize;
    }

    public List<String> getFilename_head() {
        return filename_head;
    }

    public void setFilename_head(List<String> filename_head) {
        this.filename_head = filename_head;
    }

    public String getHadoopConf() {
        return hadoopConf;
    }

    public void setHadoopConf(String hadoopConf) {
        this.hadoopConf = hadoopConf;
    }

    public String getHbasePath() {
        return hbasePath;
    }

    public void setHbasePath(String hbasePath) {
        this.hbasePath = hbasePath;
    }

    public int getStandone() {
        return standone;
    }

    public void setStandone(int standone) {
        this.standone = standone;
    }

    public List<String> getNameNodes() {
        return nameNodes;
    }

    public void setNameNodes(List<String> nameNodes) {
        this.nameNodes = nameNodes;
    }
}
