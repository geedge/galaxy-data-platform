package com.mesalab.tool.utils;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.mesalab.tool.domain.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

/**
 * @ClassName AsnLookup
 * @Description  IP查询AS号
 * @Author 中电积至有限公司 darnell
 * @Date 2020-05-09 13:45
 * @Version 1.0.3
 **/
public class AsnLookup {
    enum ServiceEnum {
        PRIVATE, PUBLIC
    }
    protected static final String UNKNOW  =  "N/A";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final static String DEFAULT_DATABASE_PATH = "dat";
    private final static String DEFAULT_DB_ASN_PUBLIC = "asn.mmdb";
    private final static String DEFAULT_DB_ASN_PUBLIC_V4 = "asn_v4_201901.mmdb";
    private final static String DEFAULT_DB_ASN_PUBLIC_V6 = "asn_v6_201901.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE = "asn_private.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE_V4 = "asn_private_v4.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE_V6 = "asn_private_v6.mmdb";


    private static GalaxyDataBaseReader asnLocationPublicReader;
    private static GalaxyDataBaseReader asnLocationPublicReaderV4;
    private static GalaxyDataBaseReader asnLocationPublicReaderV6;

    private static GalaxyDataBaseReader asnLocationPrivateReader;
    private static GalaxyDataBaseReader asnLocationPrivateReaderV4;
    private static GalaxyDataBaseReader asnLocationPrivateReaderV6;


    synchronized void init(AsnLookup.Builder builder) {
        try {

            if (builder.isDefaultDB) {

                File asnDbPublicFile =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PUBLIC);
                if (!asnDbPublicFile.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PUBLIC);
                    if (url != null) {
                        asnDbPublicFile =  new File(url.getPath());
                    }

                }


                File asnDbPublicV4File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PUBLIC_V4);
                if (!asnDbPublicV4File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PUBLIC_V4);
                    if (url != null) {
                        asnDbPublicV4File =  new File(url.getPath());
                    }

                }

                File asnDbPublicV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PUBLIC_V6);
                if (!asnDbPublicV6File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PUBLIC_V6);
                    if (url != null) {
                        asnDbPublicV6File =  new File(url.getPath());
                    }
                }

                File asnDbPrivateFile =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PRIVATE);
                if (!asnDbPrivateFile.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PRIVATE);
                    if (url != null) {
                        asnDbPrivateFile =  new File(url.getPath());
                    }

                }

                File asnDbPrivateV4File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PRIVATE_V4);
                if (!asnDbPrivateV4File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PRIVATE_V4);
                    if (url != null) {
                        asnDbPrivateV4File =  new File(url.getPath());
                    }

                }


                File asnDbPrivateV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PRIVATE_V6);
                if (!asnDbPrivateV6File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PRIVATE_V6);
                    if (url != null) {
                        asnDbPrivateV6File =  new File(url.getPath());
                    }
                }

                if (asnDbPublicFile.exists()) {
                    asnLocationPublicReader = new GalaxyDataBaseReader.Builder(asnDbPublicFile).withCache(new CHMCache()).build();
                }
                if (asnDbPublicV4File.exists()) {
                    asnLocationPublicReaderV4 = new GalaxyDataBaseReader.Builder(asnDbPublicV4File).withCache(new CHMCache()).build();
                }

                if (asnDbPublicV6File.exists()) {
                    asnLocationPublicReaderV6 = new GalaxyDataBaseReader.Builder(asnDbPublicV6File).withCache(new CHMCache()).build();
                }
                if (asnDbPrivateFile.exists()) {
                    asnLocationPrivateReader = new GalaxyDataBaseReader.Builder(asnDbPrivateFile).withCache(new CHMCache()).build();
                }
                if (asnDbPrivateV4File.exists()) {
                    asnLocationPrivateReaderV4 = new GalaxyDataBaseReader.Builder(asnDbPrivateV4File).withCache(new CHMCache()).build();
                }

                if (asnDbPrivateV6File.exists()) {
                    asnLocationPrivateReaderV6 = new GalaxyDataBaseReader.Builder(asnDbPrivateV6File).withCache(new CHMCache()).build();
                }

            } else {


                if (StringUtil.isNotBlank(builder.asnDatabasePublicFile)) {
                    asnLocationPublicReader = new GalaxyDataBaseReader.Builder(new File(builder.asnDatabasePublicFile))
                            .withCache(new CHMCache()).build();
                }
                if (StringUtil.isNotBlank(builder.asnDatabasePublicFileV4)) {
                    asnLocationPublicReaderV4 = new GalaxyDataBaseReader.Builder(new File(builder.asnDatabasePublicFileV4))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.asnDatabasePublicFileV6)) {
                    asnLocationPublicReaderV6 = new GalaxyDataBaseReader.Builder(new File(builder.asnDatabasePublicFileV6))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.asnDatabasePrivateFile)) {
                    asnLocationPrivateReader = new GalaxyDataBaseReader.Builder(new File(builder.asnDatabasePrivateFile))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.asnDatabasePrivateFileV4)) {
                    asnLocationPrivateReaderV4 = new GalaxyDataBaseReader.Builder(new File(builder.asnDatabasePrivateFileV4))
                            .withCache(new CHMCache()).build();
                }

                if (StringUtil.isNotBlank(builder.asnDatabasePrivateFileV6)) {
                    asnLocationPrivateReaderV6 = new GalaxyDataBaseReader.Builder(new File(builder.asnDatabasePrivateFileV6))
                            .withCache(new CHMCache()).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Unknow mmdb dat file , please check your dat path!");

        }


    }



    private AsnLookup(AsnLookup.Builder builder) {
        init(builder);
    }


    /**
     * 加载mmdb数据字典文件，内部生成mmdb动态库。
     *
     * 1.默认记载ASN库，使用方式如下:
     * AsnLookup asnLookup = new AsnLookup.Builder(true).build();
     * 自动加载应用dat目录下的库文件，命名为:asn_v4.asn_v6.mmdb库。
     *
     * 2.手动加载IP库，使用方式如下：
     * <p>
     *   AsnLookup asnLookup = new AsnLookup.Builder(false).loadDataFileV4("D:\\galaxy-tool\\dat\\asn_v4_201901.mmdb").
     *   loadDataFileV6("D:\\galaxy-tool\\dat\\asn_v6_201901.mmdb").build();
     *
     * </p>
     */
    public static final class Builder {
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
         * @param asnDatabasePublicFile 加载IPv4与IPv6 ASN库
         * @return
         */
        public Builder loadDataFile(String asnDatabasePublicFile) {
            this.asnDatabasePublicFile = asnDatabasePublicFile;
            return this;
        }

        /**
         *
         * @param asnDatabasePublicFileV4 加载IPv4 ASN库
         * @return
         */
        public Builder loadDataFileV4(String asnDatabasePublicFileV4) {
            this.asnDatabasePublicFileV4 = asnDatabasePublicFileV4;
            return this;
        }

        /**
         *
         * @param asnDatabasePublicFileV6 加载IPv6 ASN库
         * @return
         */
        public Builder loadDataFileV6(String asnDatabasePublicFileV6) {
            this.asnDatabasePublicFileV6 = asnDatabasePublicFileV6;
            return this;
        }

        /**
         *
         * @param asnDatabasePrivateFile 加载IPv4与IPv6 用户自定义ASN库
         * @return
         */
        public Builder loadDataFilePrivate(String asnDatabasePrivateFile) {
            this.asnDatabasePrivateFile = asnDatabasePrivateFile;
            return this;
        }

        /**
         *
         * @param asnDatabasePrivateFileV4 加载IPv4 用户自定义ASN库
         * @return
         */
        public Builder loadDataFilePrivateV4(String asnDatabasePrivateFileV4) {
            this.asnDatabasePrivateFileV4 = asnDatabasePrivateFileV4;
            return this;
        }

        /**
         *
         * @param asnDatabasePrivateFileV6 加载IPv6 用户自定义ASN库
         * @return
         */
        public Builder loadDataFilePrivateV6(String asnDatabasePrivateFileV6) {
            this.asnDatabasePrivateFileV6 = asnDatabasePrivateFileV6;
            return this;
        }



        /**
         * 构建对象
         * @return
         * @throws IOException
         */
        public AsnLookup build() {
            return new AsnLookup(this);
        }



    }



    public String asnLookup(String ip) {
        String result = UNKNOW;
        LocationResponse response = getResponse(ip);


        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.isNotBlank(response.getAsn()) ? response.getAsn() : UNKNOW;
        }

        return result;

    }

    public String asnLookupInfo(String ip) {
        String result = UNKNOW;
        LocationResponse response = getResponse(ip);


        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.setDefaultIfEmpty(response.getOrganization(), UNKNOW)
                    +"," + StringUtil.setDefaultIfEmpty(response.getCountryCode(), UNKNOW);
        }

        return result;

    }



    public String asnLookupDetail(String ip) {
        String result = UNKNOW;
        LocationResponse response = getResponse(ip);

        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.setDefaultIfEmpty(response.getAsn(), UNKNOW) + "\t"
                    + StringUtil.setDefaultIfEmpty(response.getOrganization(), UNKNOW)
                    +"," + StringUtil.setDefaultIfEmpty(response.getCountryCode(), UNKNOW);
        }


        return result;
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


        return response;
    }




    private GalaxyDataBaseReader getAsnDataBaseReaderV4(String service) {
        if (service.equalsIgnoreCase(ServiceEnum.PRIVATE.name())) {
            if (StringUtil.isNotEmpty(asnLocationPrivateReaderV4)) {
                return asnLocationPrivateReaderV4;
            } else  {
                return asnLocationPrivateReader;
            }
        } else  {
            if (StringUtil.isNotEmpty(asnLocationPublicReaderV4)) {
                return asnLocationPublicReaderV4;
            } else{
                return asnLocationPublicReader;
            }
        }
    }

    private GalaxyDataBaseReader getAsnDataBaseReaderV6(String service) {
        if (service.equalsIgnoreCase(ServiceEnum.PRIVATE.name())) {
            if (StringUtil.isNotEmpty(asnLocationPrivateReaderV6)) {
                return asnLocationPrivateReaderV6;
            } else  {
                return asnLocationPrivateReader;
            }
        } else  {
            if (StringUtil.isNotEmpty(asnLocationPublicReaderV6)) {
                return asnLocationPublicReaderV6;
            } else{
                return asnLocationPublicReader;
            }
        }
    }



    interface Strategy {
        LocationResponse getResponse(String ip);
    }

    /**
     * 用户自定义IP库的执行策略
     */
    class PrivateReader implements Strategy {
        @Override
        public LocationResponse getResponse(String ip) {
            GalaxyDataBaseReader dataBaseReader = null;
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                if (IPUtil.isIP(ip)) {
                    dataBaseReader = getAsnDataBaseReaderV4(ServiceEnum.PRIVATE.name());
                } else if (IPUtil.isIPv6Address(ip)) {
                    dataBaseReader = getAsnDataBaseReaderV6(ServiceEnum.PRIVATE.name());
                }

                if (StringUtil.isNotEmpty(dataBaseReader)) {
                    return dataBaseReader.location(ipAddress);
                }

            } catch(AddressNotFoundException addressNotFoundException) {
                logger.warn("Address not found ,ip is :" + ip);
            } catch (Exception e) {
                throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
            }
            return null;
        }
    }
    /**
     * 第三方服务库的执行策略，例如maxmind、IPDAT
     */
    class PublicReader implements Strategy {
        @Override
        public LocationResponse getResponse(String ip) {
            GalaxyDataBaseReader dataBaseReader = null;
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                if (IPUtil.isIP(ip)) {
                    dataBaseReader = getAsnDataBaseReaderV4(ServiceEnum.PUBLIC.name());

                } else if (IPUtil.isIPv6Address(ip)) {
                    dataBaseReader = getAsnDataBaseReaderV6(ServiceEnum.PUBLIC.name());

                }

                if (StringUtil.isNotEmpty(dataBaseReader)) {
                    return dataBaseReader.location(ipAddress);
                }

            } catch(AddressNotFoundException addressNotFoundException) {
                logger.warn("Address not found ,ip is :" + ip);
            } catch (Exception e) {
                throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
            }
            return null;


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
