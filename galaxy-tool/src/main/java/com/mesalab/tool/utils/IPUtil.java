package com.mesalab.tool.utils;


import com.mesalab.tool.domain.Nets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * <p>对IP进行处理工具类</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2018-08-30
 *
 */
public class IPUtil {

    private static Logger logger = LoggerFactory.getLogger(IPUtil.class);
    private static byte pos []= new byte []{(byte)128,64,32,16,8,4,2,1};
    private static final Pattern IPV4Pattern = Pattern.
            compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    private static final Pattern IPV6_STD_PATTERN =
            Pattern.compile(
                    "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
            Pattern.compile(
                    "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");


    /**
     *
     * <p>将10进制IP转为字符串，网络序。IP((0-255).(0-255).(0-255).(0-255))格式.
     * ip10为字符串形式能给处理带来方便.
     * </p>
     * <pre>
     * IpUtil.getIpString("3232235777") = "192.168.1.1"
     * </pre>
     * @see  #getIpDesimal(String)
     * @param ip10 10进制IP
     * @return 返回如<code>0.0.0.0</code>格式IP
     */
    public static String getIpString(String ip10) {
        StringBuffer sb = new StringBuffer("");
        if (ip10.indexOf("-") > -1) {

            Integer intIP = Integer.parseInt(setStrEmpty(ip10));

            sb.append(String.valueOf(intIP&0x000000FF));
            sb.append(".");
            sb.append(String.valueOf((intIP&0x0000FFFF)>>>8));
            sb.append(".");//将高8位置0，然后右移16位
            sb.append(String.valueOf((intIP&0x00FFFFFF)>>>16));
            sb.append(".");
            sb.append(String.valueOf(intIP>>>24&0xFF));//直接右移24位
            return sb.toString();
        } else {

            Long longIP = Long.parseLong(setStrEmpty(ip10));
            sb.append(String.valueOf(longIP&0x000000FF));
            sb.append(".");
            sb.append(String.valueOf((longIP&0x0000FFFF)>>>8));
            sb.append(".");//将高8位置0，然后右移16位
            sb.append(String.valueOf((longIP&0x00FFFFFF)>>>16));
            sb.append(".");
            sb.append(String.valueOf(longIP>>>24&0xFF));//直接右移24位
            return sb.toString();
        }
    }

    /**
     *
     * <p>将10进制IP转为字符串，主机序。IP((0-255).(0-255).(0-255).(0-255))格式.
     * ip10为字符串形式能给处理带来方便.
     * </p>
     * <pre>
     * IpUtil.getIpHostString("3232235777") = "192.168.1.1"
     * </pre>
     * @see  #getIpDesimal(String)
     * @param ip10 10进制IP
     * @return 返回如<code>0.0.0.0</code>格式IP
     */
    //将10进制整数形式转换成127.0.0.1形式的IP地址，按网络序
    public static String getIpHostString(String ip10) {
        StringBuffer sb = new StringBuffer(32);
        if (ip10.indexOf("-") > -1) {

            Integer intIP = Integer.parseInt(setStrEmpty(ip10));

            sb.append(String.valueOf(intIP >>> 24));// 直接右移24位
            sb.append(".");
            sb.append(String.valueOf((intIP & 0x00FFFFFF) >>> 16)); // 将高8位置0，然后右移16位
            sb.append(".");
            sb.append(String.valueOf((intIP & 0x0000FFFF) >>> 8));
            sb.append(".");
            sb.append(String.valueOf(intIP & 0x000000FF));
            return sb.toString();
        } else {
            Long longIP = Long.parseLong(setStrEmpty(ip10));

            sb.append(String.valueOf(longIP >>> 24));// 直接右移24位
            sb.append(".");
            sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16)); // 将高8位置0，然后右移16位
            sb.append(".");
            sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
            sb.append(".");
            sb.append(String.valueOf(longIP & 0x000000FF));

            return sb.toString();
        }
    }


    /**
     * 判断一个IP地址是否属于某一个子网
     *
     * @param ip IP地址
     * @param prefix 子网地址
     * @param prefixLen 掩码长度
     * @return true or false
     */
    public static boolean inPrefix(String ip, String prefix, int prefixLen) {
        String binPrefix = toBinaryString(prefix);
        if (binPrefix.length() > prefixLen) {
            binPrefix = binPrefix.substring(0,prefixLen);
        }
        String binIP = toBinaryString(ip);
        binIP = binIP.substring(0,prefixLen);
        if (binIP.equals(binPrefix)) {
            return true;
        } else {
            return false;
        }
    }











    /**
     * 获得两个IP间的范围，如192.168.0.128-192.168.2.256。
     * 注意：本方法不做合法性检查！
     *
     * @param startIp 起始IP
     * @param endIp 中止IP
     * @return 范围内的IP地址数组
     */
    public static List<String> getIPRange(String startIp, String endIp) {

        List<String> list = new ArrayList<String>();

        try{

            long start = getIpHostDesimal(startIp);
            long end = getIpHostDesimal(endIp);

            for (long i = start; i <= end; i++) {
                list.add(getIpHostString(String.valueOf(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



    /**
     * 验证IP为保留三个IP区域私有地址
     * @param ip IPV4
     * @return boolean true 私有地址 false 公有地址
     */
    public static boolean internalIp(String ip) {
        byte[] addr = getAddress(ip);
        return internalIp(addr);
    }

    private static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * 获得子网的所有IP地址；如：192.168.0.1/24。
     * 注意：本方法不做合法性检查！
     * @param prefix 子网所属IP地址
     * @param prefixLen 子网掩码
     * @return List<String>
     */
    public static List<String> getIPRange(String prefix, int prefixLen) {
        String binPrefix = toBinaryString(prefix);
        if (binPrefix.length() > prefixLen) {
            binPrefix = binPrefix.substring(0,prefixLen);
        }
        StringBuffer ip1 = new StringBuffer(binPrefix);
        StringBuffer ip2 = new StringBuffer(binPrefix);
        for (int i = ip1.length(); i < 32; i++) {
            ip1.append('0');
        }
        for (int i = ip2.length(); i < 32; i++) {
            ip2.append('1');
        }

        return getIPRange(toIPString(ip1.toString()),toIPString(ip2.toString()));
    }


    /**
     *
     * <p>根据标准ip段获取，该段子网掩码
     * </p>
     * @param startIP 起始ip，endIP 结束ip
     * @return 返回如<code>子网掩码</code>格式IP
     */
    public static String getMask(String startIP,String endIP) {
        byte start [] = getAddress(startIP);
        byte end   [] = getAddress(endIP);
        byte mask  [] = new byte [start.length];
        boolean flag=false;
        for(int i=0; i<start.length; i++){

            mask[i]=(byte)~(start[i]^end[i]);

            if (flag) {
                mask[i]=0;
            }
            if(mask[i]!=-1){
                mask[i]=getMask(mask[i]);
                flag=true;
            }
        }
        return toString(mask);
    }



    private static byte getMask(byte b) {
        if (b==0) {
            return b;
        }
        byte p = pos[0];
        for(int i=0;i<8;i++){
            if ((b&pos[i])==0) {
                break;
            }
            p=(byte)(p>>1);
        }
        p=(byte)(p<<1);
        return (byte)(b&p);
    }


    private static byte[] getAddress(String address) {
        String subStr [] = address.split("\\.");
        if(subStr.length!=4) {
            throw new IllegalArgumentException("所传入的IP地址不符合IPv4的规范");
        }
        byte b [] = new byte [4];
        for (int i=0;i<b.length;i++) {
            b [i]=(byte)Integer.parseInt(subStr[i]);
        }
        return b;
    }


    /**
     * 将IPv4形式掩码形式转为，整型掩码位数
     * @param netmarks
     * @return
     */
    public static int getNetMask(String netmarks)
    {
        if(!isMask(netmarks)){
            throw(new RuntimeException("掩码格式错误！"));
        }
        StringBuffer sbf;
        String str;
        int inetmask=0,count=0;
        String[] ipList=netmarks.split("\\.");
        for(int n=0;n<ipList.length;n++)
        {
            sbf = toBin(Integer.parseInt(ipList[n]));
            str=sbf.reverse().toString();
            count=0;
            for(int i=0;i<str.length();i++){
                i=str.indexOf('1',i);
                if(i==-1){break;}
                count++;
            }
            inetmask+=count;
        }
        return inetmask;
    }

    /**
     * 计算子网大小
     * @param netmask
     * @return
     */
    public static int getPoolMax(int netmask)
    {
        if(netmask<=0||netmask>=32)
        {
            return 0;
        }
        int bits=32-netmask;
        return (int) Math.pow(2,bits) -2;
    }

    private static StringBuffer toBin(int x)
    {
        StringBuffer result=new StringBuffer();
        result.append(x%2);
        x/=2;
        while(x>0){
            result.append(x%2);
            x/=2;
        }
        return result;
    }





    /**
     * 根据起始IP地址和子网掩码计算终止IP
     * @return String 子网的结束IP
     */
    public static String getEndIP(String StartIP,int netmask)
    {
        return getEndIP(StartIP,convertMask(netmask));
    }
    /**
     * 根据起始IP地址和子网掩码计算终止IP
     * @return String 子网的结束IP
     */
    public static String getEndIP(String StartIP, String netmask)
    {
        Nets nets = new Nets();
        String[] start=Negation(StartIP,netmask).split("\\.");
        nets.setStartIP(start[0]+"."+start[1]+"."+start[2]+"."+(Integer.valueOf(start[3])+1));
        nets.setEndIP(TaskOR(Negation(StartIP,netmask),netmask));
        nets.setNetMask(netmask);
        return nets.getEndIP();
    }

    /**
     * temp1根据temp2取反
     */
    private static String Negation(String StartIP,String netmask)
    {
        String[] temp1=StartIP.trim().split("\\.");
        String[] temp2=netmask.trim().split("\\.");
        int[] rets=new int[4];
        for (int i =0;i<4;i++) {
            rets[i]=Integer.parseInt(temp1[i])&Integer.parseInt(temp2[i]);
        }
        return rets[0]+"."+rets[1]+"."+rets[2]+"."+rets[3];
    }
    /**
     * temp1根据temp2取或
     */
    private static String TaskOR(String StartIP,String netmask)
    {
        String[] temp1=StartIP.trim().split("\\.");
        String[] temp2=netmask.trim().split("\\.");
        int[] rets=new int[4];
        for (int i =0;i<4;i++) {
            rets[i]=255-(Integer.parseInt(temp1[i])^Integer.parseInt(temp2[i]));
        }
        //return rets[0]+"."+rets[1]+"."+rets[2]+"."+(rets[3]-1);
        return rets[0]+"."+rets[1]+"."+rets[2]+"."+(rets[3]);
    }



    /**
     * <p>将整型的掩码转换成IPv4型(30 -> 255.255.255.252).</p>
     *
     * @param mask 整型掩码.
     * @return IPv4型掩码.
     * @exception RuntimeException 掩码错误.
     */
    public static String convertMask(int mask)  {
        if (mask > 32 || mask < 8) {
            throw(new RuntimeException("掩码错误！应为8-32的整数。"));
        }
        return toIPString(maskToBinaryString(mask));
    }


    /**
     * <p>将整型的掩码转换成反向IPv4型(30 -> 255.255.255.252).</p>
     *
     * @param mask 整型掩码.
     * @return IPv4型反向掩码.
     * @exception Exception 掩码错误.
     */
    public static String convertReverseMask(int mask) throws Exception {
        if (mask > 32 || mask < 8) {
            throw(new Exception("掩码错误！应为8-32的整数。"));
        }
        return toIPString(reverseMaskToBinaryString(mask));
    }



    /**
     * 将整型掩码转换成二进制字符串
     * @param mask 整型掩码
     * @return 二进制型掩码.
     */
    private static String maskToBinaryString(int mask) {
        StringBuffer str = new StringBuffer();

        for(int i=1;i<=32;i++) {
            if (i <= mask) {
                str.append('1');
            } else {
                str.append('0');
            }
        }
        return str.toString();
    }


    /**
     * 将整型掩码转换成二进制字符串
     * @param mask 整型掩码
     * @return 反向二进制型掩码.
     */
    private static String reverseMaskToBinaryString(int mask) {
        StringBuffer str = new StringBuffer();

        for(int i=1;i<=32;i++) {
            if (i <= mask) {
                str.append('0');
            } else {
                str.append('1');
            }
        }
        return str.toString();
    }


    /**
     * 将二进制字符串转换成IPv4型字符串
     * @param binary 二进制字符串
     * @return IPv4型IP.
     */
    private static String toIPString(String binary) {
        if (binary.length() < 32) {
            for (int i=binary.length();i<32;i++) {
                binary = "0" + binary;
            }
        }

        String part1 = binary.substring(0,8);
        String part2 = binary.substring(8,16);
        String part3 = binary.substring(16,24);
        String part4 = binary.substring(24);
        return Integer.parseInt(part1,2) + "."
                + Integer.parseInt(part2,2) + "."
                + Integer.parseInt(part3,2) + "."
                + Integer.parseInt(part4,2);
    }
    /**
     * 获取子网的网络地址.
     *
     * @param ip IP地址.
     * @param mask 掩码.
     * @return 网络地址.
     * @exception Exception IP地址错误或掩码错误.
     */
    public static String getSubNetIP(String ip, int mask) throws Exception {
        if (!isIP(ip)) {
            throw(new Exception("IP地址不合法！"));
        }
        String s1 = toBinaryString(ip);
        String s2 = maskToBinaryString(mask);
        return toIPString(Long.toBinaryString(Long.parseLong(s1,2)&Long.parseLong(s2,2)));
    }

    /**
     * <p>将整型掩码转换成IPv4型的反码(30 -> 0.0.0.3).</p>
     *
     * @param mask 整型掩码.
     * @return IPv4型反码.
     * @exception Exception 掩码错误.
     */
    public static String reverseMask(int mask) throws Exception {
        if (mask > 32 || mask < 8) {
            throw(new Exception("掩码错误！应为8-32的整数。"));
        }
        String str = maskToBinaryString(mask);
        str = str.replace('0','2');
        str = str.replace('1','0');
        str = str.replace('2','1');
        return toIPString(str);
    }

    /**
     * 将IPv4型字符串转换成长度为32的二进制字符串
     * 需要加验证是否为IP
     */
    public static String toBinaryString(String ip) {
        String[] array = ip.split("[.]");
        String str = "";
        for (int i=0; i<array.length; i++) {
            String s = Integer.toBinaryString(Integer.parseInt(array[i]));
            if (s.length() < 8) {
                for (int j=s.length();j<8;j++) {
                    s = "0" + s;
                }
            }
            str += s;
        }
        return str;
    }


    /**
     *
     * <p>验证Ip是否符合规则.给定字符串,判断是否符合正则验证的ip格式.</p>
     * <pre>例子说明:</pre>
     * @param ip  ip字符
     * @return <code>true</code> ip符合验证规则, <code>false</code> ip不符合验证规则.
     */
    public static boolean isIP(String ip) {
        if (ip == null) {
            return false;
        }
        Matcher mat = IPV4Pattern.matcher(ip);
        return mat.matches();
    }


    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    /**
     *
     * <p>在web方式下获取客户端IP地址.</p>
     * <p>
     * 使用该方法,需要在有javaee.jar扩展包前提下.
     * </p>
     * @param request HttpServletRequest请求对象
     * @return  返回字符串IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     *
     * <p>验证mask是否符合规则.给定字符串,判断是否符合正则验证的mask格式.</p>
     * <pre>例子说明:</pre>
     * @param mask  mask字符
     * @return <code>true</code> mask符合验证规则, <code>false</code> mask不符合验证规则.
     */
    public static boolean isMask(String mask) {
        if (mask == null) {
            return false;
        }
        if(!isIP(mask)){
            return false;
        }
        String[] ips = mask.split("\\.");
        String binaryVal = "";
        for (int i = 0; i < ips.length; i++)
        {
            String binaryStr = Integer.toBinaryString(Integer.parseInt(ips[i]));
            Integer times = 8 - binaryStr.length();

            for(int j = 0; j < times; j++)
            {
                binaryStr = "0" + binaryStr;
            }
            binaryVal += binaryStr;
        }
        if(binaryVal.indexOf("01")!=-1){
            return false;
        }
        return true;
    }


    /**
     *
     * <p>将IP进制转换成十进制网络字节序</p>
     * <p>
     * IP是一个字符串，符合IP的规则(0-255).(0-255).(0-255).(0-255).
     * 将ip转换成长整型的原因是：处理时间类型简单；入库节省空间,查询提高效率.
     * </p>
     * <pre>
     * IpUtil.getIpDesimal("192.168.1.1") = 3232235777
     * IpUtil.getIpDesimal("192.168.1.256") = -1 //不符合Ip规则
     * </pre>
     * @see #getIpString(String)
     * @param ip 符合IP规则的字符串
     * @return   <code>-1</code> 验证ip不合法,<code>ip</code> 计算ip返回长整型.
     */
    public static long getIpDesimal(String ip) {
        long ip10 = 0;
        if (!isIP(ip)) {
            return -1; // ip 不合法
        }
        String[] ss = ip.trim().split("\\.");
        for (int i = 0; i < 4; i++) {
            ip10 += Math.pow(256, i) * Long.parseLong(ss[i]);
        }

        return ip10;
    }


    /**
     *
     * <p>将IP进制转换成十进制主机字节序</p>
     * <p>
     * IP是一个字符串，符合IP的规则(0-255).(0-255).(0-255).(0-255).
     * 将ip转换成长整型的原因是：处理时间类型简单；入库节省空间,查询提高效率.
     * </p>
     * <pre>
     * IpUtil.getIpHostDesimal("192.168.1.1") = 3232235777
     * IpUtil.getIpHostDesimal("192.168.1.256") = -1 //不符合Ip规则
     * </pre>
     * @see #getIpString(String)
     * @param ip 符合IP规则的字符串
     * @return   <code>-1</code> 验证ip不合法,<code>ip</code> 计算ip返回长整型.
     */

    public static long getIpHostDesimal(String ip) {
        long ip10 = 0;
        if (!isIP(ip)) {
            return -1; // ip 不合法
        }
        String[] ss = ip.trim().split("\\.");
        for (int i = 0; i < 4; i++) {
            ip10 += Math.pow(256, 3 - i) * Long.parseLong(ss[i]);
        }

        return ip10;
    }



    /**
     *
     * <p>获取IP/mask 包含IP数量，适合完整IPw网段</p>
     * <p>
     *
     *
     * </p>
     * <pre>
     *
     *
     * </pre>
     * @param ip 符合IP规则的字符串
     * @param mask 掩码格式
     * @return   <code>-1</code> 验证ip不合法,<code>ip</code> 计算ip返回长整型.
     */
    public static long getIpNum(String ip, int mask) {
        long num = 0;

        if (isIP(ip)) {
            String endIp = getEndIP(ip, mask);
            num = getIpHostDesimal(endIp) -  getIpHostDesimal(ip) +1;

        } else {
            throw new IllegalArgumentException("所传入的IP地址不符合IPV4规范");
        }

        return num;
    }




    // 此方法用于判断数据为空就置为0
    private static String setStrEmpty(String str) {
        if ("".equals(str) || str == null) {
            str = "0";
        }
        return str;
    }


    private static String toString(byte[] address) {
        StringWriter sw = new StringWriter(16);
        sw.write(Integer.toString(address[0]&0xFF));
        for(int i=1;i<address.length;i++){
            sw.write(".");
            sw.write(Integer.toString(address[i]&0xFF));
        }
        return sw.toString();
    }


}
