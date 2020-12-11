package com.mesalab.tool.utils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>针对CIDR IP范围处理工具类.</p>
 * @author 中电积至有限公司
 * @version 1.0 创建时间：2020-04-21
 *
 */
public class CIDRUtils {
    private InetAddress inetAddress;
    private InetAddress startAddress;
    private InetAddress endAddress;
    private final int prefixLength;
    private final String cidr;

    /**
     * 参数示例:
     * ipv4格式的cidr:221.178.125.0/24
     * ipv6格式的cidr:2c0f:f7a8:9230::/48
     *
     * @param cidr
     * @throws UnknownHostException
     */
    public CIDRUtils(String cidr) throws UnknownHostException {
        this.cidr = cidr;
        /* split CIDR to address and prefix part */
        if (this.cidr.contains("/")) {
            int index = this.cidr.indexOf("/");
            String addressPart = this.cidr.substring(0, index);
            String networkPart = this.cidr.substring(index + 1);
            inetAddress = InetAddress.getByName(addressPart);
            prefixLength = Integer.parseInt(networkPart);
            calculate();
        } else {
            throw new IllegalArgumentException("not an valid CIDR format!");
        }
    }

    public String getNetworkAddress() {
        return this.startAddress.getHostAddress();
    }


    public BigInteger getNetworkAddressNum() {
        return new BigInteger(1, this.startAddress.getAddress());
    }


    public String getBroadcastAddress() {
        return this.endAddress.getHostAddress();
    }

    public BigInteger getBroadcastAddressNUm() {
        return new BigInteger(1, this.endAddress.getAddress());
    }

    /**
     * 判断ip是否在cidr里面
     *
     * @param ipAddress
     * @return
     * @throws UnknownHostException
     */
    public boolean isInRange(String ipAddress) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ipAddress);
        BigInteger start = new BigInteger(1, this.startAddress.getAddress());
        BigInteger end = new BigInteger(1, this.endAddress.getAddress());
        BigInteger target = new BigInteger(1, address.getAddress());
        int st = start.compareTo(target);
        int te = target.compareTo(end);

        return (st == -1 || st == 0) && (te == -1 || te == 0);
    }



    private void calculate() throws UnknownHostException {
        ByteBuffer maskBuffer;
        int targetSize;
        if (inetAddress.getAddress().length == 4) {
            maskBuffer =
                    ByteBuffer
                            .allocate(4)
                            .putInt(-1);
            targetSize = 4;
        } else {
            maskBuffer = ByteBuffer.allocate(16)
                    .putLong(-1L)
                    .putLong(-1L);
            targetSize = 16;
        }

        BigInteger mask = (new BigInteger(1, maskBuffer.array())).not().shiftRight(prefixLength);
        ByteBuffer buffer = ByteBuffer.wrap(inetAddress.getAddress());
        BigInteger ipVal = new BigInteger(1, buffer.array());
        BigInteger startIp = ipVal.and(mask);
        BigInteger endIp = startIp.add(mask.not());
        byte[] startIpArr = toBytes(startIp.toByteArray(), targetSize);
        byte[] endIpArr = toBytes(endIp.toByteArray(), targetSize);
        this.startAddress = InetAddress.getByAddress(startIpArr);
        this.endAddress = InetAddress.getByAddress(endIpArr);
    }






    private static byte[] toBytes(byte[] array, int targetSize) {
        int counter = 0;
        List<Byte> newArr = new ArrayList<Byte>();
        while (counter < targetSize && (array.length - 1 - counter >= 0)) {
            newArr.add(0, array[array.length - 1 - counter]);
            counter++;
        }
        int size = newArr.size();
        for (int i = 0; i < (targetSize - size); i++) {
            newArr.add(0, (byte) 0);
        }
        byte[] ret = new byte[newArr.size()];
        for (int i = 0; i < newArr.size(); i++) {
            ret[i] = newArr.get(i);
        }
        return ret;
    }




}
