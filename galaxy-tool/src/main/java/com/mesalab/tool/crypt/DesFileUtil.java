package com.mesalab.tool.crypt;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

/**
 *  
 * <p>3des加密算法，针对文件进行加密的工具类 </p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2013-07-10
 *
 */
public abstract class  DesFileUtil {
	
	/**
	 * <p>把成生的一对密钥保存到DesKey.xml文件中</p>
	 * @param keyFilePath 密钥文件，如果密钥文件不存在，将会建立该文件
	 * @throws FileNotFoundException
	 */
	public static void saveDesKey(String keyFilePath) throws FileNotFoundException {		
		try {
			File keyFile = new File(keyFilePath);
			if(keyFile.isFile()&&!keyFile.exists()){
				keyFile.createNewFile();
			}
			SecureRandom sr = new SecureRandom();
			//为我们选择的DES算法生成一个KeyGenerator对象
			KeyGenerator kg = KeyGenerator.getInstance ("DESede");
			kg.init (sr);
			FileOutputStream fos = new FileOutputStream(keyFile);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
			//生成密钥
			Key key = kg.generateKey();
		    oos.writeObject(key);
		    oos.close();
		}catch (FileNotFoundException fe) {
			throw new FileNotFoundException("keyFile is not found");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* <p>获得DES加密的密钥</p>
	* <pre>
	* 在交易处理的过程中应该定时更
	* 换密钥。需要JCE的支持，如果jdk版本低于1.4，则需要
	* 安装jce-1_2_2才能正常使用。
	* </pre>
	*  @param keyFilePath 已存在密钥文件
	 * @return   Key  返回对称密钥
	 * @throws FileNotFoundException 
	 */
		public static Key getKey(String keyFilePath) throws FileNotFoundException {
			Key kp = null;
			try {
				InputStream is = new FileInputStream(new File(keyFilePath));
				ObjectInputStream oos = new ObjectInputStream(is);
				kp = (Key) oos.readObject();
				oos.close();
			}catch (FileNotFoundException fe) {
				throw new FileNotFoundException("keyFile is not found");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return kp;
		}
		
		/**
		 * <p>文件file进行加密并保存目标文件destFile中</p>
		 * @param file 要加密的文件 如c:/test/srcFile.txt
		 * @param destFile 加密后存放的文件名 如c:/加密后文件.txt
		 * @param keyFile 密钥文件
		 * @throws Exception
		 */
		public static void encrypt(String file, String destFile,String  keyFile) throws Exception {
				Cipher cipher = Cipher.getInstance("DESede");
				cipher.init(Cipher.ENCRYPT_MODE, getKey(keyFile));
				InputStream is = new FileInputStream(file);
				OutputStream out = new FileOutputStream(destFile);
				CipherInputStream cis = new CipherInputStream(is, cipher);
				byte[] buffer = new byte[1024];
				int r;
				while ((r = cis.read(buffer)) > 0) {
					out.write(buffer, 0, r);
				}
				cis.close();
				is.close();
				out.close();
		}
		
		/**
		 * <p>文件file进行加密并保存目标文件destFile中</p>
		 * @param file 已加密的文件 如c:/加密后文件.txt
		 * @param dest  解密后存放的文件名 如c:/ test/解密后文件.txt
		 * @param keyFile 密钥文件
		 * @throws Exception
		 */
		public static void decrypt(String file, String dest,String keyFile) throws Exception {
				Cipher cipher = Cipher.getInstance("DESede");
				cipher.init(Cipher.DECRYPT_MODE, getKey(keyFile));
				InputStream is = new FileInputStream(file);
				OutputStream out = new FileOutputStream(dest);
				CipherOutputStream cos = new CipherOutputStream(out, cipher);
				byte[] buffer = new byte[1024];
				int r;
				while ((r = is.read(buffer)) >= 0) {
					cos.write(buffer, 0, r);
				}
				cos.close();
				out.close();
				is.close();
			}





}
