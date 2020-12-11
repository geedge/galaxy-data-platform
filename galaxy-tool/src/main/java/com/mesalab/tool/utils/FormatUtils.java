package com.mesalab.tool.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.InternetDomainName;

public class FormatUtils {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 将List&lt;HashMap&lt;String,Object&gt;&gt;中HashMap的key全部转化为变量形式，<br>
	 * 如{"BRH_ID":"1234","BRH_NAME":"机构"}改为{"brhId":"1234","brhName":"机构"}
	 *
	 * @param list
	 * @return List<HashMap<String, Object>>
	 */
	public static List<HashMap<String, Object>> formatHashMapKeyInList(List<HashMap<String, Object>> list) {
		List result = new ArrayList();
		// 遍历list后进行格式化列表元素
		if (list != null) {
			for (HashMap<String, Object> h : list) {
				// 将hashMap的key转化为驼峰格式，如{"BRH_ID":"1234"}改为{"brhId":"1234"}
				result.add(formatHashMapKey(h));
			}
		}
		return result;
	}

	/**
	 * 将hashMap的key转化为驼峰格式，如{"BRH_ID":"1234","BRH_NAME":"机构"}改为{"brhId":"1234",
	 * "brhName":"机构"}
	 *
	 * @param hashMap
	 * @return HashMap<String, Object>
	 */
	public static HashMap<String, Object> formatHashMapKey(HashMap<String, Object> hashMap) {
		HashMap result = new HashMap();
		String key = null;
		// 遍历map
		for (Entry<String, Object> e : (Set<Entry<String, Object>>) hashMap.entrySet()) {
			key = (String) e.getKey();
			// 将hashMap的key转化为驼峰格式
			key = formatDBNameToVarName(key);
			// 封装为新的map
			result.put(key, e.getValue());
		}
		// 返回格式化后的map
		return result;
	}

	/**
	 * 数据库列名转化为属性名,如DEAL_ID=dealId; <br>
	 * 不能保证完全正确，如DBUTIL不会智能的转化为DBUtil,而会转化为dbutil, <br>
	 * 规则为全部转化为单词，然后首字母小写
	 *
	 * @param DBName
	 * @return String
	 */
	public static String formatDBNameToVarName(String DBName) {
		StringBuilder result = new StringBuilder("");
		// 以"_"分割
		String[] DBNameArr = DBName.split("_");
		for (int i = 0, j = DBNameArr.length; i < j; i++) {
			// 获取以"_"分割后的字符数组的每个元素的第一个字母，
			result.append(DBNameArr[i].charAt(0));
			// 将其他字符转换成小写
			result.append(DBNameArr[i].substring(1).toLowerCase());
		}
		char c0 = result.charAt(0);
		if (c0 >= 'A' && c0 <= 'Z') {
			c0 = (char) (c0 + 'a' - 'A');
		}
		result.setCharAt(0, c0);
		return result.toString();
	}

	private FormatUtils(Builder builder) {
		init(builder);
	}

	private synchronized void init(Builder builder) {
		try {

			if (builder.isDefalult) {
				logger.info("FormatUtils begin with default PZ.");
			} else {
				logger.info("FormatUtils begin with user-defined PZ.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final class Builder {
		boolean isDefalult;

		public Builder(boolean isDefalult) {
			this.isDefalult = isDefalult;
		}

		public FormatUtils build() {
			return new FormatUtils(this);
		}

	}

	/**
	 * 获取域名的顶级私有域名
	 * 例如：getTopPrivateDomain('www.google.com.hk') ,返回 google.com.hk
	 * @param domain 网站域名
	 * @return 顶级私有域名
	 */
	public static String getTopPrivateDomain(String domain) {

		domain = getDomain(domain);

		if (StringUtil.isBlank(domain) || !InternetDomainName.isValid(domain)) {
			return StringUtil.EMPTY;
		}

		InternetDomainName internetDomainName = InternetDomainName.from(domain);

		if (internetDomainName.isUnderPublicSuffix()) {
			return internetDomainName.topPrivateDomain().toString();
		} else {
			return StringUtil.EMPTY;

		}

	}

	/**
	 * 根据邮箱发件人或收件人地址获取域名信息
	 * @param email 发件人或收件人地址
	 * @return
	 */
	public static String getEmailDomain(String email) {
		if (StringUtil.isBlank(email)) {
			return StringUtil.EMPTY;
		}

		if (email.indexOf("@") < 0) {
			return StringUtil.EMPTY;
		}


		return email.substring(email.indexOf("@") + 1);
	}



	/**
	 *
	 * <p>通过给定url获取域名地址.</p>
	 * <p>
	 * 获得域名信息需去除 http/https/ftp 与 www 一些头信息,获取有效地址.
	 * </p>
	 * <pre>
	 * StringUtil.getDomain("http://www.baidu.com") = "baidu.com"
	 * </pre>
	 * @param  url 地址
	 * @return  返回截取后的域名地址
	 */
	public static String getDomain(String url) {

		if(StringUtil.isBlank(url)){
			return StringUtil.EMPTY;
		}

		if (url.indexOf("http://") >= 0) {
			url = url.substring("http://".length(), url.length());
		} else if (url.indexOf("https://", 0) >= 0) {
			url = url.substring("https://".length(), url.length());
		} else if (url.indexOf("ftp://", 0) >= 0) {
			url = url.substring("ftp://".length(), url.length());
		} else {
			url = url;
		}

		if (url.indexOf("www.") == 0) {
			url = url.substring(url.indexOf(".") + 1, url.length());
		}

		if (url.indexOf(":") >= 0) {
			url = url.substring(0, url.indexOf(":"));
		}

		if (url.indexOf("/", 0) >= 0) {
			url = url.substring(0, url.indexOf("/", 1));
		}

		if (url.indexOf("?") >= 0) {
			url = url.substring(0, url.indexOf("?"));
		}


		return url;
	}

	/**
	 * 雪花模型生成id 
	 * @param workerId
	 * @param dataCenterId
	 * @return 全局唯一id
	 */
	public long getSnowflakeId(long workerId, long dataCenterId) {
		return SnowflakeId.generateId(workerId, dataCenterId);
	}
	
	/**
	 * 雪花模型生成id 
	 * 依赖于zookeeper
	 * @param zookeeperIp
	 * @param dataCenterId
	 * @return 全局唯一id
	 */
	public long getSnowflakeId(String zookeeperIp, long dataCenterId) {

		return SnowflakeId.generateId(zookeeperIp, dataCenterId);
	}

}
