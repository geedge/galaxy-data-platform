/*
 *  @(#)AESUtil.java 1.0
 *
 * Copyright 2010 NIS, Inc. All rights reserved.
 * 
 */
package com.mesalab.tool.crypt;
    import java.security.Key;  
      
    import javax.crypto.Cipher;  
    import javax.crypto.KeyGenerator;  
    import javax.crypto.SecretKey;  
    import javax.crypto.spec.SecretKeySpec;  
      
    import org.apache.commons.codec.binary.Base64;  
    import org.apache.commons.codec.digest.DigestUtils;  
   
    /**
     *  
     * <p>AES对称加密算法,组件类 </p>
     * @author 中电积至有限公司 darnell
     * @version 1.0 创建时间：2013-07-10
     *
     */
    public abstract class AESUtil {  
      //密钥算法  
      public static final String ALGORITHM="AES";  
      //密钥长度(java默认只能处理128位以内的长度,如果需要处理大于128位可以使用JCE解除密钥长度限制)  
      public static final int KEY_SIZE=128;  
      /** 
       * <p>转换密钥 </p>
       * @param key 二进制密钥 
       * @return Key 密钥 
       * @throws Exception 
       */  
      private static Key toKey(byte[] key) throws Exception   
      {  
          //实例化AES密钥 
          SecretKey secretKey=new SecretKeySpec(key,ALGORITHM);  
          return secretKey;  
      }  
      /** 
       * <p>解密</p>
       * @param data 待解密数据 
       * @param key 密钥 
       * @return byte[] 解密数据 
       * @throws Exception 
       */  
      public static byte[] decrypt(byte[] data,byte[] key) throws Exception   
      {  
          //还原密钥  
          Key k=toKey(key);  
          //实例化  
          Cipher cipher=Cipher.getInstance(ALGORITHM);  
          //初始化,设置为解密模式  
          cipher.init(Cipher.DECRYPT_MODE, k);  
          //执行操作  
          return cipher.doFinal(data);  
      }  
      /** 
       * <p>解密</p> 
       * @param data 待解密数据 
       * @param key 密钥 
       * @return byte[] 解密数据 
       * @throws Exception 
       */  
      public static byte[] decrypt(byte[] data,String key) throws Exception   
      {  
          return decrypt(data,getKey(key));  
      }  
      /** 
       * <p>加密</p>
       * @param data 待加密数据 
       * @param key 密钥 
       * @return byte[] 加密数据 
       * @throws Exception 
       */  
      public static byte[] encrypt(byte[] data,byte[] key) throws Exception   
      {  
          //还原密钥  
          Key k=toKey(key);  
          //实例化  
          Cipher cipher=Cipher.getInstance(ALGORITHM);  
          //初始化,设置为加密模式  
          cipher.init(Cipher.ENCRYPT_MODE, k);  
          //执行操作  
          return cipher.doFinal(data);  
      }  
      /** 
       * <p>加密</p> 
       * @param data 待加密数据 
       * @param key 密钥 
       * @return byte[] 加密数据 
       * @throws Exception 
       */  
      public static byte[] encrypt(byte[] data,String key) throws Exception   
      {  
          return encrypt(data,getKey(key));  
      }  
      /** 
       * <p>生成密钥</p> 
       * @return byte[] 二进制密钥 
       * @throws Exception 
       */  
      public static byte[] initKey() throws Exception   
      {  
          //实例化  
          KeyGenerator kg=KeyGenerator.getInstance(ALGORITHM);  
          //初始化密钥长度  
          kg.init(KEY_SIZE);  
          //生成秘密密钥  
          SecretKey secretKey=kg.generateKey();  
          //获得密钥的二进制编码形式  
          return secretKey.getEncoded();  
      }  
      /** 
       * <p>初始化密钥</p> 
       * @return String Base64编码密钥 
       * @throws Exception 
       */  
      public static String initKeyString() throws Exception   
      {  
          return new String(Base64.encodeBase64(initKey()));  
      }  
      /** 
       * <p>获取密钥</p>  
       * @param key 
       * @return byte[] 密钥 
       * @throws Exception 
       */  
      public static byte[] getKey(String key) throws Exception   
      {  
          return Base64.decodeBase64(key.getBytes());  
      }  
      /** 
       * <p>摘要处理</p> 
       * @param data 待摘要数据 
       * @return String 摘要字符串 
       */  
      public static String shaHex(byte[] data)  
      {  
          return DigestUtils.md5Hex(data);  
      }  
      /** 
       * <p>摘要数据验证</p> 
       * @param data 待摘要数据 
       * @param messageDigest 摘要字符串 
       * @return 验证结果 
       */  
      public static boolean validate(byte[] data,String messageDigest)  
      {  
          return messageDigest.equals(shaHex(data));  
      }  
        
    }  
    