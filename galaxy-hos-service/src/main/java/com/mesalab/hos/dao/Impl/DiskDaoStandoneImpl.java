package com.mesalab.hos.dao.Impl;

import com.mesalab.hos.dao.DiskDao;
import com.mesalab.hos.dao.UserDao;
import com.mesalab.hos.entity.ResEntity;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * Created by wk on 2020/4/20.
 */
@Component
@ConditionalOnProperty(value = "hbase.standone", havingValue = "0")
public class DiskDaoStandoneImpl implements DiskDao {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());


    @Override
    public Long getHBaseCurrSize(String path) throws InterruptedException, IOException {

        String result = "";
        Process catCommands = null;
        Long resultsize ;
        catCommands = Runtime.getRuntime()
                .exec(new String[]{"/bin/sh", "-c", "du -s " + path + " | awk '{print $1}'"});
        try (InputStream in = catCommands.getInputStream()) {
            result = org.apache.commons.io.IOUtils.toString(in, StandardCharsets.UTF_8);
            catCommands.waitFor();
            resultsize = Long.parseLong(result.trim()) * 1024L;
        } catch (IOException e) {
            throw e;
        } catch (InterruptedException e) {
            throw e;
        }
        return resultsize;
    }

    @Override
    public Long getHBaseMaxSize(String path) throws IOException, InterruptedException {

        String result = "";
        Process catCommands = null;
        Long resultsize;
        catCommands = Runtime.getRuntime().exec(
                new String[]{"/bin/sh", "-c", "df " + path + " | sed -n '2,2p' | awk '{print $2}'"});
        try (InputStream in = catCommands.getInputStream()) {
            result = IOUtils.toString(in, StandardCharsets.UTF_8);
            catCommands.waitFor();
            resultsize = Long.parseLong(result.trim()) * 1024L;
        } catch (IOException e) {
            throw e;
        } catch (InterruptedException e) {
            throw e;
        }
        return resultsize;
    }
}
