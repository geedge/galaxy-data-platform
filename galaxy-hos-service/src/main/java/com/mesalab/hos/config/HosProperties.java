package com.mesalab.hos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by wk on 2020/4/20.
 */
@Configuration
@ConfigurationProperties(prefix = "hos")
public class HosProperties {

    public static String ip;
    public static int maxPoolNumber;
    public static Double smallFileCount=0.0;
    public static Double middleFileCount=0.0;
    public static Double bigFileCount=0.0;
    public static Double errorCount=0.0;
    public static Double successCount=0.0;
    public static Double sumPacket=0.0;

    static {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();//获得本机Ip
        } catch (UnknownHostException e) {
            ip = "";
        }
    }

    private int maxResultLimit;
    private int fileSizeValve;
    private int deleteMultipleNumber;
    private int maxPartNumber;
    private int maxPosition;
    private String coprocessorPath;
    private String metaHeader;
    private int simple;
    private List<String> users;

    @Value("${server.port}")
    private Integer server_port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        HosProperties.ip = ip;
    }

    public int getMaxPoolNumber() {
        return maxPoolNumber;
    }

    public void setMaxPoolNumber(int maxPoolNumber) {
        HosProperties.maxPoolNumber = maxPoolNumber;
    }

    public int getFileSizeValve() {
        return fileSizeValve;
    }

    public void setFileSizeValve(int fileSizeValve) {
        this.fileSizeValve = fileSizeValve;
    }

    public int getDeleteMultipleNumber() {
        return deleteMultipleNumber;
    }

    public void setDeleteMultipleNumber(int deleteMultipleNumber) {
        this.deleteMultipleNumber = deleteMultipleNumber;
    }

    public String getCoprocessorPath() {
        return coprocessorPath;
    }

    public void setCoprocessorPath(String coprocessorPath) {
        this.coprocessorPath = coprocessorPath;
    }

    public int getMaxResultLimit() {
        return maxResultLimit;
    }

    public void setMaxResultLimit(int maxResultLimit) {
        this.maxResultLimit = maxResultLimit;
    }

    public String getMetaHeader() {
        return metaHeader;
    }

    public void setMetaHeader(String metaHeader) {
        this.metaHeader = metaHeader;
    }

    public Integer getServer_port() {
        return server_port;
    }

    public void setServer_port(Integer server_port) {
        this.server_port = server_port;
    }

    public int getMaxPartNumber() {
        return maxPartNumber;
    }

    public void setMaxPartNumber(int maxPartNumber) {
        this.maxPartNumber = maxPartNumber;
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(int maxPosition) {
        this.maxPosition = maxPosition;
    }

    public int getSimple() {
        return simple;
    }

    public void setSimple(int simple) {
        this.simple = simple;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }



}
