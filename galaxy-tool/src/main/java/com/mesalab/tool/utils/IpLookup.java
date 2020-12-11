package com.mesalab.tool.utils;

import com.google.common.base.Joiner;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.mesalab.tool.domain.LocationResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

/**
 * @ClassName IpLookup
 * @Description  对IP定位查找工具类
 * @Author 中电积至有限公司 darnell
 * @Date 2020-05-8 20:01
 * @Version 1.0.2
 **/
public final class IpLookup extends AbstractIpLookup{

    enum ServiceEnum {
        PRIVATE, PUBLIC
    }
    public final static String DEFAULT_DATABASE_PATH = "dat";
    private final static String SEPRATOR = ",";
    private final static String DEFAULT_DB_IP_PUBLIC = "ip.mmdb";
    private final static String DEFAULT_DB_IP_PUBLIC_V4 = "ip_v4.mmdb";
    private final static String DEFAULT_DB_IP_PUBLIC_V6 = "ip_v6.mmdb";
    private final static String DEFAULT_DB_IP_PRIVATE = "ip_private.mmdb";
    private final static String DEFAULT_DB_IP_PRIVATE_V4 = "ip_private_v4.mmdb";
    private final static String DEFAULT_DB_IP_PRIVATE_V6 = "ip_private_v6.mmdb";

    private AsnLookup asnLookup;


    private static GalaxyDataBaseReader ipLocationPublicReader;
    private static GalaxyDataBaseReader ipLocationPublicReaderV4;
    private static GalaxyDataBaseReader ipLocationPublicReaderV6;


    private static GalaxyDataBaseReader ipLocationPrivateReader;
    private static GalaxyDataBaseReader ipLocationPrivateReaderV4;
    private static GalaxyDataBaseReader ipLocationPrivateReaderV6;



    synchronized void init(Builder builder) {
        try {

            if (builder.isDefaultDB) {

                File dbPublicFile = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PUBLIC);

                if (!dbPublicFile.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PUBLIC);
                    if (url != null) {
                        dbPublicFile =  new File(url.getPath());
                    }
                }

                File dbPublicV4File = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PUBLIC_V4);

                if (!dbPublicV4File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PUBLIC_V4);
                    if (url != null) {
                        dbPublicV4File =  new File(url.getPath());
                    }
                }

                File dbPublicV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PUBLIC_V6);
                if (!dbPublicV6File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PUBLIC_V6);
                    if (url != null) {
                        dbPublicV6File =  new File(url.getPath());
                    }
                }

                File dbPrivateFile = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        +DEFAULT_DB_IP_PRIVATE);

                if (!dbPrivateFile.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PRIVATE);
                    if (url != null) {
                        dbPrivateFile =  new File(url.getPath());
                    }
                }


                File dbPrivateV4File = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PRIVATE_V4);

                if (!dbPrivateV4File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PRIVATE_V4);
                    if (url != null) {
                        dbPrivateV4File =  new File(url.getPath());
                    }
                }

                File dbPrivateV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PRIVATE_V6);
                if (!dbPrivateV6File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PRIVATE_V6);
                    if (url != null) {
                        dbPrivateV6File =  new File(url.getPath());
                    }
                }

                if (dbPublicFile.exists()) {
                    ipLocationPublicReader = new GalaxyDataBaseReader.Builder(dbPublicFile).withCache(new CHMCache()).build();
                }
                if (dbPublicV4File.exists()) {
                    ipLocationPublicReaderV4 = new GalaxyDataBaseReader.Builder(dbPublicV4File).withCache(new CHMCache()).build();
                }

                if (dbPublicV6File.exists()) {
                    ipLocationPublicReaderV6 = new GalaxyDataBaseReader.Builder(dbPublicV6File).withCache(new CHMCache()).build();
                }

                if (dbPrivateFile.exists()) {
                    ipLocationPrivateReader = new GalaxyDataBaseReader.Builder(dbPrivateFile).withCache(new CHMCache()).build();
                }

                if (dbPrivateV4File.exists()) {
                    ipLocationPrivateReaderV4 = new GalaxyDataBaseReader.Builder(dbPrivateV4File).withCache(new CHMCache()).build();
                }

                if (dbPrivateV6File.exists()) {
                    ipLocationPrivateReaderV6 = new GalaxyDataBaseReader.Builder(dbPrivateV6File).withCache(new CHMCache()).build();
                }


                asnLookup =  new AsnLookup.Builder(true).build();

            } else {


                if (StringUtil.isNotBlank(builder.ipDatabasePublicFile)) {
                    ipLocationPublicReader = new GalaxyDataBaseReader.Builder(new File(builder.ipDatabasePublicFile))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.ipDatabasePublicFileV4)) {
                    ipLocationPublicReaderV4 = new GalaxyDataBaseReader.Builder(new File(builder.ipDatabasePublicFileV4))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.ipDatabasePublicFileV6)) {
                    ipLocationPublicReaderV6 = new GalaxyDataBaseReader.Builder(new File(builder.ipDatabasePublicFileV6))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.ipDatabasePrivateFile)) {
                    ipLocationPrivateReader = new GalaxyDataBaseReader.Builder(new File(builder.ipDatabasePrivateFile))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.ipDatabasePrivateFileV4)) {
                    ipLocationPrivateReaderV4 = new GalaxyDataBaseReader.Builder(new File(builder.ipDatabasePrivateFileV4))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.ipDatabasePrivateFileV6)) {
                    ipLocationPrivateReaderV6 = new GalaxyDataBaseReader.Builder(new File(builder.ipDatabasePrivateFileV6))
                            .withCache(new CHMCache()).build();
                }


                asnLookup =  new AsnLookup.Builder(false)
                        .loadDataFile(builder.asnDatabasePublicFile)
                        .loadDataFileV4(builder.asnDatabasePublicFileV4)
                        .loadDataFileV6(builder.asnDatabasePublicFileV6)
                        .loadDataFilePrivate(builder.asnDatabasePrivateFile)
                        .loadDataFilePrivateV4(builder.asnDatabasePrivateFileV4)
                        .loadDataFilePrivateV6(builder.asnDatabasePrivateFileV6)
                        .build();


            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Unknow mmdb dat file , please check your dat path!");

        }


    }


    private IpLookup(Builder builder) {
        init(builder);
    }



    /**
     * 加载mmdb数据字典文件，内部生成mmdb动态库。
     *
     * 1.默认记载IP库，使用方式如下:
     * IpLookup ipLookup = new IpLookup.Builder(true).build();
     * 自动加载应用dat目录下的库文件，命名为:all_ip_info_v4.mmdb与all_ip_info_v6.mmdb库。
     *
     * 2.手动加载IP库，使用方式如下：
     * <p>
     *   IpLookup ipLookup = new IpLookup.Builder(false).loadDataFileV4("D:\\galaxy-tool\\dat\\ip.mmdb").
     *   loadDataFileV6("D:\\galaxy-tool\\dat\\all_ip_info_v6.mmdb").build();
     *
     * </p>
     */
    public static final class Builder {
        String ipDatabasePublicFile;
        String ipDatabasePublicFileV4;
        String ipDatabasePublicFileV6;

        String ipDatabasePrivateFile;
        String ipDatabasePrivateFileV4;
        String ipDatabasePrivateFileV6;

        String asnDatabasePublicFile;
        String asnDatabasePublicFileV4;
        String asnDatabasePublicFileV6;

        String asnDatabasePrivateFile;
        String asnDatabasePrivateFileV4;
        String asnDatabasePrivateFileV6;

        boolean isDefaultDB;

        /**
         *
         * @param isDefaultDB <code>false</code> 手动指定IP库路径 <code>true</code> 使用默认路径
         */
        public Builder(boolean isDefaultDB) {
            this.isDefaultDB = isDefaultDB;
        }

        /**
         *
         * @param ipDatabasePublicFile 加载IPv4与IPV6 mmdb库文件
         * @return
         */
        public Builder loadDataFile(String ipDatabasePublicFile) {
            this.ipDatabasePublicFile = ipDatabasePublicFile;
            return this;
        }

        /**
         *
         * @param ipDatabasePublicFileV4 加载IPv4 mmdb库文件
         * @return
         */
        public Builder loadDataFileV4(String ipDatabasePublicFileV4) {
            this.ipDatabasePublicFileV4 = ipDatabasePublicFileV4;
            return this;
        }

        /**
         *
         * @param ipDatabasePublicFileV6 加载IPv6 mmdb库文件
         * @return
         */
        public Builder loadDataFileV6(String ipDatabasePublicFileV6) {
            this.ipDatabasePublicFileV6 = ipDatabasePublicFileV6;
            return this;
        }

        /**
         *
         * @param ipDatabasePrivateFile 加载用户自定义IPv4与IPv6 mmdb库文件
         * @return
         */
        public Builder loadDataFilePrivate(String ipDatabasePrivateFile) {
            this.ipDatabasePrivateFile = ipDatabasePrivateFile;
            return this;
        }

        /**
         *
         * @param ipDatabasePrivateFileV4 加载用户自定义IPv4 mmdb库文件
         * @return
         */
        public Builder loadDataFilePrivateV4(String ipDatabasePrivateFileV4) {
            this.ipDatabasePrivateFileV4 = ipDatabasePrivateFileV4;
            return this;
        }

        /**
         *
         * @param ipDatabasePrivateFileV6 加载用户自定义IPv6 mmdb库文件
         * @return
         */
        public Builder loadDataFilePrivateV6(String ipDatabasePrivateFileV6) {
            this.ipDatabasePrivateFileV6 = ipDatabasePrivateFileV6;
            return this;
        }



        /**
         *
         * @param asnDatabasePublicFile 加载asn IPv4与IPv6库
         * @return
         */
        public Builder loadAsnDataFile(String asnDatabasePublicFile) {
            this.asnDatabasePublicFile = asnDatabasePublicFile;
            return this;
        }


        /**
         *
         * @param asnDatabasePublicFileV4 加载asn IPv4 mmdb库文件
         * @return
         */
        public Builder loadAsnDataFileV4(String asnDatabasePublicFileV4) {
            this.asnDatabasePublicFileV4 = asnDatabasePublicFileV4;
            return this;
        }


        /**
         *
         * @param asnDatabasePublicFileV6 加载asn IPv6 mmdb库文件
         * @return
         */
        public Builder loadAsnDataFileV6(String asnDatabasePublicFileV6) {
            this.asnDatabasePublicFileV6 = asnDatabasePublicFileV6;
            return this;
        }


        /**
         *
         * @param asnDatabasePrivateFile 加载asn用户自定义 IPv4与IPv6库
         * @return
         */
        public Builder loadAsnDataFilePrivate(String asnDatabasePrivateFile) {
            this.asnDatabasePrivateFile = asnDatabasePrivateFile;
            return this;
        }

        /**
         *
         * @param asnDatabasePrivateFileV4 加载asn用户自定义 IPv4 mmdb库文件
         * @return
         */
        public Builder loadAsnDataFilePrivateV4(String asnDatabasePrivateFileV4) {
            this.asnDatabasePrivateFileV4 = asnDatabasePrivateFileV4;
            return this;
        }



        /**
         *
         * @param asnDatabasePrivateFileV6 加载用户自定义asn IPv6 mmdb库文件
         * @return
         */
        public Builder loadAsnDataFilePrivateV6(String asnDatabasePrivateFileV6) {
            this.asnDatabasePrivateFileV6 = asnDatabasePrivateFileV6;
            return this;
        }

        /**
         * 构建对象
         * @return
         * @throws IOException
         */
        public IpLookup build() {
            return new IpLookup(this);
        }

    }

    @Override
    public  String countryLookup(String ip) {
        String result = "";

        LocationResponse response = getResponse(ip);

        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.setDefaultIfEmpty(response.getCountry(), "").toString() ;
        }

        return StringUtil.setDefaultIfEmpty(result, UNKNOW).toString();
    }

    @Override
    public String cityLookupDetail(String ip) {

        String result = "";
        LocationResponse response = getResponse(ip);
        if (StringUtil.isNotEmpty(response)) {
            result = Joiner.on(SEPRATOR).skipNulls().join(response.getCity(), response.getProvince(), response.getCountry());
        }

        return StringUtil.setDefaultIfEmpty(result, UNKNOW).toString();
    }

    @Override
    public String cityLookup(String ip) {
        String result = "";
        LocationResponse response = getResponse(ip);

        if (StringUtil.isNotEmpty(response)) {
            if(StringUtil.isNotBlank(response.getCity())) {
                result = response.getCity();
            }
        }

        return StringUtil.setDefaultIfEmpty(result, UNKNOW).toString();

    }

    @Override
    public String provinceLookup(String ip) {
        String result = "";
        LocationResponse response = getResponse(ip);
        if (StringUtil.isNotEmpty(response)) {
            if (StringUtil.isNotBlank(response.getProvince())) {
                result = response.getProvince();
            }
        }

        return StringUtil.setDefaultIfEmpty(result, UNKNOW).toString();
    }


    @Override
    public  String latLngLookup(String ip) {

        String result = "";
        try {
            LocationResponse response = getResponse(ip);
            if (StringUtil.isNotEmpty(response)
                && StringUtil.isNotBlank(response.getLatitude())
                && StringUtil.isNotBlank(response.getLongitude())) {


                result = Joiner.on(SEPRATOR).skipNulls().join(response.getLatitude(), response.getLongitude());

            } else {
                result = Joiner.on(SEPRATOR).join(UNKNOW, UNKNOW);
            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result;

    }


    @Override
    public  String cityLatLngLookup(String ip) {
        String result = "";
        try {

            result = Joiner.on(SEPRATOR).skipNulls().join(cityLookup(ip), latLngLookup(ip));

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return StringUtil.setDefaultIfEmpty(result, UNKNOW).toString();

    }

    @Override
    public String asnLookup(String ip) {
       return asnLookup.asnLookup(ip);

    }


    @Override
    public String asnLookupInfo(String ip) {
        return asnLookup.asnLookupInfo(ip);
    }

    @Override
    public String asnLookupDetail(String ip) {

        return asnLookup.asnLookupDetail(ip);


    }

    private LocationResponse getResponse(String ip) {

        LocationResponse response = null;

        if (!IPUtil.isIP(ip) && !IPUtil.isIPv6Address(ip)) {
            throw new IllegalArgumentException("unknown ip format :" + ip );
        }

        response = new Context(new PrivateReader()).executeStragey(ip);

        if (StringUtil.isEmpty(response)) {
            response =  new Context(new PublicReader()).executeStragey(ip);
        }

        if (StringUtil.isEmpty(response)) {
            response =  new Context(new InnerReader()).executeStragey(ip);
        }

        return response;
    }





    private GalaxyDataBaseReader getIpDataBaseReaderV4(String service) {

        if (service.equalsIgnoreCase(ServiceEnum.PRIVATE.name())) {
            if (StringUtil.isNotEmpty(ipLocationPrivateReaderV4)) {
                return ipLocationPrivateReaderV4;
            } else {
                return ipLocationPrivateReader;
            }
        } else  {
            if (StringUtil.isNotEmpty(ipLocationPublicReaderV4)) {
                return ipLocationPublicReaderV4;
            } else {
                return ipLocationPublicReader;
            }
        }

    }

    private GalaxyDataBaseReader getIpDataBaseReaderV6(String service) {

        if (service.equalsIgnoreCase(ServiceEnum.PRIVATE.name())) {
            if (StringUtil.isNotEmpty(ipLocationPrivateReaderV6)) {
                return ipLocationPrivateReaderV6;
            } else {
                return ipLocationPrivateReader;
            }
        } else  {
            if (StringUtil.isNotEmpty(ipLocationPublicReaderV6)) {
                return ipLocationPublicReaderV6;
            } else {
                return ipLocationPublicReader;
            }
        }

    }







    interface Strategy {
        LocationResponse getResponse(String ip);
    }

    /**
     * 用户自定义IP库的执行策略
     */
    class PrivateReader implements  Strategy {
        @Override
        public LocationResponse getResponse(String ip) {
            GalaxyDataBaseReader dataBaseReader = null;
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                if (IPUtil.isIP(ip)) {
                    dataBaseReader = getIpDataBaseReaderV4(ServiceEnum.PRIVATE.name());
                } else if (IPUtil.isIPv6Address(ip)) {
                    dataBaseReader = getIpDataBaseReaderV6(ServiceEnum.PRIVATE.name());
                }

                if (StringUtil.isNotEmpty(dataBaseReader)) {
                    return dataBaseReader.location(ipAddress);
                }

            } catch(AddressNotFoundException addressNotFoundException) {
               logger.debug("Address not found ,ip is :" + ip);
            } catch (Exception e) {
                throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
            }
            return null;
        }
    }
    /**
     * 第三方服务库的执行策略，例如maxmind、IPDAT
     */
    class PublicReader implements  Strategy {
        @Override
        public LocationResponse getResponse(String ip) {
            GalaxyDataBaseReader dataBaseReader = null;
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                if (IPUtil.isIP(ip)) {
                    dataBaseReader = getIpDataBaseReaderV4(ServiceEnum.PUBLIC.name());

                } else if (IPUtil.isIPv6Address(ip)) {
                    dataBaseReader = getIpDataBaseReaderV6(ServiceEnum.PUBLIC.name());

                }

                if (StringUtil.isNotEmpty(dataBaseReader)) {
                    return dataBaseReader.location(ipAddress);
                }

            } catch(AddressNotFoundException addressNotFoundException) {
                logger.debug("Address not found ,ip is :" + ip);
            } catch (Exception e) {
                throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
            }
            return null;


        }
    }

    /**
     * 工具类自定义规则库的执行策略
     */
    class InnerReader implements  Strategy {
        @Override
        public LocationResponse getResponse(String ip) {
            LocationResponse response = null;
            if (IPUtil.isIP(ip) && IPUtil.internalIp(ip)) {
                response = new LocationResponse();
                response.setCountry("Private IP");
                response.setCity(null);
                response.setProvince(null);
                response.setAsn(null);
                response.setIsp(null);
                response.setLatitude(null);
                response.setLongitude(null);
                response.setAreaCode(null);
                return  response;
            } else {
                return null;
            }

        }
    }


    class Context {
        private Strategy strategy;

        public Context(Strategy strategy) {
            this.strategy = strategy;
        }

        public LocationResponse executeStragey(String ip) {
            return strategy.getResponse(ip);
        }

    }




}











