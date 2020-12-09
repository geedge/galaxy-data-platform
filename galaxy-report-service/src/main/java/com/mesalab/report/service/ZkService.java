package com.mesalab.report.service;


import org.springframework.stereotype.Service;

import java.net.InetAddress;

/**
 * Created by wk1 on 2020/1/6.
 */
@Service
public interface ZkService {


     boolean isMaster();
}