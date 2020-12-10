package com.mesalab.hos.dao.Impl;

import com.mesalab.hos.dao.UserDao;
import com.mesalab.hos.entity.ResEntity;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by wk on 2020/4/20.
 */
@Component
public class UserDaoImpl implements UserDao {

    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Autowired
    private Connection hbaseConnection;

    @Override
    public ResEntity createUser(String user) {
        ResEntity resEntity = new ResEntity();
        resEntity.setCode("");
        resEntity.setIsSuccess(false);
        try (Admin admin = hbaseConnection.getAdmin()) {
            NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
            for (NamespaceDescriptor desc : namespaceDescriptors) {
                if (user.equals(desc.getName())) {
                    resEntity.setCode("UserAlreadyExists");
                } else {
                    NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(user)
                            .build();
                    admin.createNamespace(namespaceDescriptor);
                    resEntity.setIsSuccess(true);
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return resEntity;
    }
}
