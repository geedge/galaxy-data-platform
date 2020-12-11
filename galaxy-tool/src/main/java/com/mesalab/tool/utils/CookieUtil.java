/*
 * @(#)CookieUtil.java	1.0
 *
 * Copyright 2010 NIS, Inc. All rights reserved.
 * 
 */
package com.mesalab.tool.utils;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * 
 * <p>web 开发中用cookie管理工具类.</p>
 * <p>
 * 该工具类需有javaee.jar 包支持下才可使用.
 * </p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2010-11-8 下午04:52:22
 * @see javax.servlet.http.Cookie
 *
 */
public final class CookieUtil extends Cookie {
	/**
	 * 常量：一天的秒数
	 */
	public static final int SECOND_PER_DAY= 60*60*24;
	
	/**
	 * 常量：一周的秒数
	 */
	public static final int SECOND_PER_WEEK=60*60*24*7;
	/**
	 * 常量：一个月的秒数
	 */
	public static final int SECOND_PER_MONTH=60*60*24*30;
	/**
	 * 常量：一年的秒数
	 */
	public static final int SECOND_PER_YEAR=60*60*24*365;
	
	/**
	 * 常量：默认值为7天
	 */
	public static final int DEFAULT_TIME=60*60*24*7;
	
	
    
   /**
	*<p>Title:Cookie 构造器 </p> 
	*<p>Description:实现Cookie类的构造器 </p> 
	*@param name Cookie 名称
	*@param value Cookie名称所对应的值，必须为字符串类型
	*/
	private CookieUtil(String name, String value) {
			super(name, value);
		
	}
	
 
   /**
	* 
	* <p>增加cookie，需要增加cookie保留状态</p>
	* @param response  http的{@link HttpServletResponse}响应头类
	* @param name  cookie 名称
	* @param value Cookie名称所对应的值，必须为字符串类型
	* @param state 通过web页面获取该值确定cookie失效时间
	* @throws UnsupportedEncodingException 抛出设置编码不支持，默认设置编码为utf-8
	*/
	public static void addCookie(HttpServletResponse response,String name,String value,String state) throws UnsupportedEncodingException{
			value=URLEncoder.encode(value, "utf-8");
			CookieUtil cookie = new CookieUtil(name,value);
			if("year".equalsIgnoreCase(state)){
				cookie.setMaxAge(SECOND_PER_YEAR);
			} else if("month".equalsIgnoreCase(state)){
				cookie.setMaxAge(SECOND_PER_MONTH);
			 } else if("week".equalsIgnoreCase(state)){
				 cookie.setMaxAge(SECOND_PER_WEEK);
			 }else if("day".equalsIgnoreCase(state)){
				cookie.setMaxAge(SECOND_PER_DAY);
			} else{
				cookie.setMaxAge(DEFAULT_TIME);
			}
			//设置cookie 存放路径
			cookie.setPath("/");
			response.addCookie(cookie);
			
	}
	
   /**
	* 
	* <p>增加cookie，需设置cookie失效时间值.</p>
	* @param response http的{@link HttpServletResponse}响应头类.
	* @param name  cookie 名称.
	* @param value Cookie名称所对应的值，必须为字符串类型
	* @param time 失效时间值必须为整型，单位(秒).
	* @throws UnsupportedEncodingException 抛出设置编码不支持，默认设置编码为utf-8
	*/
	public static void addCookie(HttpServletResponse response,String name,String value,int time) throws UnsupportedEncodingException{
			value=URLEncoder.encode(value,"utf-8");
			CookieUtil cookie = new CookieUtil(name,value);
			cookie.setMaxAge(time);
			cookie.setPath("/");
			response.addCookie(cookie);
	}
		
		
   /**
	* 
	* <p>增加cookie,不设置失效状态或时间值默认为1周.</p>
	* @param response http的{@link HttpServletResponse}响应头类
	* @param name cookie 名称
	* @param value Cookie名称所对应的值，必须为字符串类型
	* @throws UnsupportedEncodingException 抛出设置编码不支持，默认设置编码为utf-8
	*/
	public static void addCookie(HttpServletResponse response,String name,String value) throws UnsupportedEncodingException{
			value=URLEncoder.encode(value,"utf-8");
			CookieUtil cookie = new CookieUtil(name,value);
			cookie.setMaxAge(DEFAULT_TIME);
			cookie.setPath("/");
			response.addCookie(cookie);
	}
	
   /**
	* 
	* <p>删除cookie，将浏览器中保存的cookie通过name值找到并将该cookie设置失效时间为0.</p>
	* @param request http的{@link HttpServletRequest} 请求头信息类.
	* @param response http的{@link HttpServletResponse}响应头类.
	* @param name   cookie 名称
	*/
	public static void deleteCookie(HttpServletRequest request,HttpServletResponse response,String name){
			Cookie cookie=getCookie(request, name);
			if(cookie!=null){
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			
	}
		
	private static Cookie getCookie(ServletRequest request,String cookiename){
			HttpServletRequest req = (HttpServletRequest)request;
				Cookie[] cookies = req.getCookies();
			
				if(cookies!=null){
					for(int i=0;i<cookies.length;i++){
						if(cookies[i].getName().equals(cookiename)){
							
							return cookies[i];
						}
						
					}
				}
			
			
				return null;
	 }
		
	   /**
		* <p>根据Cookie 名称获取Cookie值</p>
		* @param request http的{@link HttpServletRequest} 请求头信息类.
		* @param cookiename cookie的名称，字符串类型.
		* @return 返回cookie值
		* @throws UnsupportedEncodingException 抛出设置编码不支持，默认设置编码为utf-8
		*/
		public static String getValue(ServletRequest request,String cookiename) throws UnsupportedEncodingException{
				HttpServletRequest req = (HttpServletRequest)request;
					Cookie[] cookies = req.getCookies();
				
					if(cookies!=null){
						for(int i=0;i<cookies.length;i++){
							if(cookies[i].getName().equals(cookiename)){
								
								return URLDecoder.decode(cookies[i].getValue(), "utf-8");
							}
							
						}
					}
				
				
					return null;
			}

}
