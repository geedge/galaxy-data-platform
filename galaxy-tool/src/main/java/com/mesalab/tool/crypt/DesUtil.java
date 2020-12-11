package com.mesalab.tool.crypt;

import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


/**
 *  
 * <p>3des加密算法，针对消息进行加密的工具类 </p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2013-07-10
 *
 */
public abstract class DesUtil {
	
	/**
	 * 
	 * <p>生成key</p>
	 * @return 返回密钥
	 */
	public static SecretKey generateKey(){
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		KeyGenerator keyGenerator=null;
		try {
			keyGenerator = KeyGenerator.getInstance("DESede");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return keyGenerator.generateKey();
	}
	
	/**
	 * <p>对信息通过3des进行加密</p>
	 * @param srcMsg 明文信息
	 * @param key 生成对称密钥
	 * @return 密文
	 */
	public static String encrypt(String srcMsg,SecretKey key){
		byte[] enc = null;
		try {
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] src = srcMsg.getBytes();
			
			enc = cipher.doFinal(src);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		return Base64Utils.encode(enc);
	}
	/**
	 * <p>对信息通过3des进行解密</p>
	 * @param encMsg 密文信息
	 * @param key 对称密钥
	 * @return 明文信息
	 */
	public  static String decrypt(String encMsg,SecretKey key){
		byte[] dec = null;
		try {
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.DECRYPT_MODE, key);
			dec = cipher.doFinal(Base64Utils.decode(encMsg.toCharArray()));
		
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		return new String(dec);
	}
	
	static class Base64Utils {

		static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
		static private byte[] codes = new byte[256];
		static {
			for (int i = 0; i < 256; i++)
				codes[i] = -1;
			for (int i = 'A'; i <= 'Z'; i++)
				codes[i] = (byte) (i - 'A');
			for (int i = 'a'; i <= 'z'; i++)
				codes[i] = (byte) (26 + i - 'a');
			for (int i = '0'; i <= '9'; i++)
				codes[i] = (byte) (52 + i - '0');
			codes['+'] = 62;
			codes['/'] = 63;
		}
		
		/**
		 * 将原始数据编码为base64编码
		 */
		static public String encode(byte[] data) {
			char[] out = new char[((data.length + 2) / 3) * 4];
			for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
				boolean quad = false;
				boolean trip = false;
				int val = (0xFF & (int) data[i]);
				val <<= 8;
				if ((i + 1) < data.length) {
					val |= (0xFF & (int) data[i + 1]);
					trip = true;
				}
				val <<= 8;
				if ((i + 2) < data.length) {
					val |= (0xFF & (int) data[i + 2]);
					quad = true;
				}
				out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
				val >>= 6;
				out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
				val >>= 6;
				out[index + 1] = alphabet[val & 0x3F];
				val >>= 6;
				out[index + 0] = alphabet[val & 0x3F];
			}
			
			return new String(out);
		}

		/**
		 * 将base64编码的数据解码成原始数据
		 */
		static public byte[] decode(char[] data) {
			int len = ((data.length + 3) / 4) * 3;
			if (data.length > 0 && data[data.length - 1] == '=')
				--len;
			if (data.length > 1 && data[data.length - 2] == '=')
				--len;
			byte[] out = new byte[len];
			int shift = 0;
			int accum = 0;
			int index = 0;
			for (int ix = 0; ix < data.length; ix++) {
				int value = codes[data[ix] & 0xFF];
				if (value >= 0) {
					accum <<= 6;
					shift += 6;
					accum |= value;
					if (shift >= 8) {
						shift -= 8;
						out[index++] = (byte) ((accum >> shift) & 0xff);
					}
				}
			}
			if (index != out.length)
				throw new Error("miscalculated data length!");
			return out;
		}
	}

	

}
