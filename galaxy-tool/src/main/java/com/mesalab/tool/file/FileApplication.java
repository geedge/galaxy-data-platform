package com.mesalab.tool.file;

import com.mesalab.tool.utils.DateUtils;
import com.mesalab.tool.utils.StringUtil;
import com.mesalab.tool.utils.TimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * 
 * <p>该类提供对文件的基本及其路径的处理</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2011-05-31 下午13:28:28
 *
 */
public final class FileApplication  extends org.apache.commons.io.FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileApplication.class);

	private static final FileApplication fileApplication = new FileApplication();

	/**
	 * 
	* <p>过滤掉文件名的特殊字符</p>
	* <pre>
	*   文件名如带有 xiao\xin.txt = xiao xin.txt
	*               xiao/xin.txt = xiao xin.txt
	* </pre>   
	* @param fileName 文件名称
	* @return  返回过滤后文件名称
	 */
	public static String filterFileName(String fileName){
		return fileName.replaceAll("[ \\\\|/|\\? ]+"," ");
	}
	
	/**
	 * 
	 * <p>判断文件或目录是否存在</p>
	 * @param path 文件或目录的路径
	 * @return true or false 文件或目录存在返回<code>true</code>,否则返回<code>false</code>.
	 */
	public  static boolean isExist(String path){
        File file = new File(path);
        return file.exists();
    }
	
	
	/**
	 * 
	 * <p>判断文件是否存在</p>
	 * @param file 文件的路径
	 * @return <code>true</code> 文件存在,<code>false</code>不是文件或文件不存在.
	 */
	public  static boolean fileIsExist(File file){
        return file.isFile() && file.exists();
    }
	

	/**
	 * 
	 * <p>给定文件名获取唯一文件名称
	 * 例如:aa.txt 返回结果aa_20120531101233.txt
	 * </p>
	 * @param fileName 文件名称
	 * @return 唯一文件名称
	 */
	public static String getOnlyFileName(String fileName){
		
		String extendName =  getFileExtendName(fileName);
		if(StringUtil.isBlank(fileName)){
			extendName="";
			fileName = fileName+"_"+ DateUtils.getCurrentDate(TimeConstants.YYYYMMDDHH24MMSS);
		}else {
			extendName = extendName.toLowerCase();
			fileName = fileName.substring(0,fileName.indexOf(extendName)-1)+"_"+DateUtils.getCurrentDate(TimeConstants.YYYYMMDDHH24MMSS)
				+"."+extendName;
		}
		
		return fileName;
	}

	/**
	 *
	 * <p>
	 * 获得文件扩展名
	 * TXT,HTML,HTM,EML,DOC,DOCX,XLS,XLSX,PPT,PPTX,PDF
	 * </p>
	 * @param filePath 文件路径
	 * @return <code>string</code> 扩展名 ,<code>null</code> 时无扩展名
	 */
	public static String getFileExtendName(String filePath) {
		String extendName = null;
		if(filePath.lastIndexOf(".") != -1){
			extendName = filePath.substring(filePath.lastIndexOf(".")+1).trim().toUpperCase();
		}
		return extendName;
	}



    /**
     * 创建单个文件
     *
     * @param descFileName
     *            文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
             logger.debug("文件 " + descFileName + " 已存在!");
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            logger.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                logger.debug("创建文件所在的目录失败!");
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                logger.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                logger.debug(descFileName + " 文件创建失败!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(descFileName + " 文件创建失败!");
            return false;
        }

    }

    /**
     * 创建目录
     *
     * @param descDirName
     *            目录名,包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            logger.debug("目录 " + descDirNames + " 已存在!");
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            logger.debug("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            logger.debug("目录 " + descDirNames + " 创建失败!");
            return false;
        }

    }


    /**
     *
     * 删除文件，可以删除单个文件或文件夹
     *
     * @param fileName
     *            被删除的文件名
     * @return 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            logger.debug(fileName + " 文件不存在!");
            return true;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     *
     * 删除单个文件
     *
     * @param fileName
     *            被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.debug("删除文件 " + fileName + " 成功!");
                return true;
            } else {
                logger.debug("删除文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            logger.debug(fileName + " 文件不存在!");
            return true;
        }
    }

        /**
         *
         * 删除目录及目录下的文件
         *
         * @param dirName
         *            被删除的目录所在的文件路径
         * @return 如果目录删除成功，则返回true，否则返回false
         */
        public static boolean deleteDirectory(String dirName) {
            String dirNames = dirName;
            if (!dirNames.endsWith(File.separator)) {
                dirNames = dirNames + File.separator;
            }
            File dirFile = new File(dirNames);
            if (!dirFile.exists() || !dirFile.isDirectory()) {
                logger.debug(dirNames + " 目录不存在!");
                return true;
            }
            boolean flag = true;
            // 列出全部文件及子目录
            File[] files = dirFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 删除子文件
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    // 如果删除文件失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
                // 删除子目录
                else if (files[i].isDirectory()) {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    // 如果删除子目录失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
            }

            if (!flag) {
                logger.debug("删除目录失败!");
                return false;
            }
            // 删除当前目录
            if (dirFile.delete()) {
                logger.debug("删除目录 " + dirName + " 成功!");
                return true;
            } else {
                logger.debug("删除目录 " + dirName + " 失败!");
                return false;
            }

        }

    /**
     * 写入文件
     *
     * @param fileName 要写入的文件
     * @param content 内容
     * @param append 是否追加 true or false
     */
    public static void writeToFile(String fileName, String content, boolean append) {
        try {
            write(new File(fileName), content, "utf-8", append);
            logger.debug("文件 " + fileName + " 写入成功!");
        } catch (IOException e) {
            logger.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
        }
    }

    /**
     * 写入文件
     *
     * @param fileName 要写入的文件
     * @param content 内容
     * @param encoding 编码
     * @param append 是否追加 true or false
     */
    public static void writeToFile(String fileName, String content, String encoding, boolean append) {
        try {
            write(new File(fileName), content, encoding, append);
            logger.debug("文件 " + fileName + " 写入成功!");
        } catch (IOException e) {
            logger.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
        }
    }


    /**
     * 压缩文件或目录
     *
     * @param srcDirName
     *            压缩的根目录
     * @param fileName
     *            根目录下的待压缩的文件名或文件夹名，其中*或""表示跟目录下的全部文件
     * @param descFileName
     *            目标zip文件
     */
    public static void zipFiles(String srcDirName, String fileName, String descFileName) {
        // 判断目录是否存在
        if (srcDirName == null) {
            logger.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
            return;
        }
        File fileDir = new File(srcDirName);
        if (!fileDir.exists() || !fileDir.isDirectory()) {
            logger.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
            return;
        }
        String dirPath = fileDir.getAbsolutePath();
        File descFile = new File(descFileName);
        try {
            ZipOutputStream zouts = new ZipOutputStream(new FileOutputStream(descFile));
            if ("*".equals(fileName) || "".equals(fileName)) {
                zipDirectoryToZipFile(dirPath, fileDir, zouts);
            } else {
                File file = new File(fileDir, fileName);
                if (file.isFile()) {
                    zipFilesToZipFile(dirPath, file, zouts);
                } else {
                    zipDirectoryToZipFile(dirPath, file, zouts);
                }
            }
            zouts.close();
            logger.debug(descFileName + " 文件压缩成功!");
        } catch (Exception e) {
            logger.debug("文件压缩失败：" + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到descFileName目录下
     *
     * @param zipFileName
     *            需要解压的ZIP文件
     * @param descFileName
     *            目标文件
     */
    public static boolean unZipFiles(String zipFileName, String descFileName) {
        String descFileNames = descFileName;
        if (!descFileNames.endsWith(File.separator)) {
            descFileNames = descFileNames + File.separator;
        }
        try {
            // 根据ZIP文件创建ZipFile对象
            ZipFile zipFile = new ZipFile(zipFileName);
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            int readByte = 0;
            // 获取ZIP文件里所有的entry
            @SuppressWarnings("rawtypes")
            Enumeration enums = zipFile.entries();
            // 遍历所有entry
            while (enums.hasMoreElements()) {
                entry = (ZipEntry) enums.nextElement();
                // 获得entry的名字
                entryName = entry.getName();
                descFileDir = descFileNames + entryName;
                if (entry.isDirectory()) {
                    // 如果entry是一个目录，则创建目录
                    new File(descFileDir).mkdirs();
                    continue;
                } else {
                    // 如果entry是一个文件，则创建父目录
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                // 打开文件输出流
                OutputStream os = new FileOutputStream(file);
                // 从ZipFile对象中打开entry的输入流
                InputStream is = zipFile.getInputStream(entry);
                while ((readByte = is.read(buf)) != -1) {
                    os.write(buf, 0, readByte);
                }
                os.close();
                is.close();
            }
            zipFile.close();
            logger.debug("文件解压成功!");
            return true;
        } catch (Exception e) {
            logger.debug("文件解压失败：" + e.getMessage());
            return false;
        }
    }

    /**
     * 将目录压缩到ZIP输出流
     *
     * @param dirPath
     *            目录路径
     * @param fileDir
     *            文件信息
     * @param zouts
     *            输出流
     */
    public static void zipDirectoryToZipFile(String dirPath, File fileDir, ZipOutputStream zouts) {
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            // 空的文件夹
            if (files.length == 0) {
                // 目录信息
                ZipEntry entry = new ZipEntry(getEntryName(dirPath, fileDir));
                try {
                    zouts.putNextEntry(entry);
                    zouts.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    // 如果是文件，则调用文件压缩方法
                    zipFilesToZipFile(dirPath, files[i], zouts);
                } else {
                    // 如果是目录，则递归调用
                    zipDirectoryToZipFile(dirPath, files[i], zouts);
                }
            }

        }

    }

    /**
     * 将文件压缩到ZIP输出流
     *
     * @param dirPath
     *            目录路径
     * @param file
     *            文件
     * @param zouts
     *            输出流
     */
    public static void zipFilesToZipFile(String dirPath, File file, ZipOutputStream zouts) {
        FileInputStream fin = null;
        ZipEntry entry = null;
        // 创建复制缓冲区
        byte[] buf = new byte[4096];
        int readByte = 0;
        if (file.isFile()) {
            try {
                // 创建一个文件输入流
                fin = new FileInputStream(file);
                // 创建一个ZipEntry
                entry = new ZipEntry(getEntryName(dirPath, file));
                // 存储信息到压缩文件
                zouts.putNextEntry(entry);
                // 复制字节到压缩文件
                while ((readByte = fin.read(buf)) != -1) {
                    zouts.write(buf, 0, readByte);
                }
                zouts.closeEntry();
                fin.close();
                System.out.println("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 获取待压缩文件在ZIP文件中entry的名字，即相对于跟目录的相对路径名
     *
     * @param dirPath
     *            目录名
     * @param file
     *            entry文件名
     * @return
     */
    private static String getEntryName(String dirPath, File file) {
        String dirPaths = dirPath;
        if (!dirPaths.endsWith(File.separator)) {
            dirPaths = dirPaths + File.separator;
        }
        String filePath = file.getAbsolutePath();
        // 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
        if (file.isDirectory()) {
            filePath += "/";
        }
        int index = filePath.indexOf(dirPaths);

        return filePath.substring(index + dirPaths.length());
    }


    /**
     * 修复路径，将 \\ 或 / 等替换为 File.separator
     *
     * @param path
     * @return
     */
    public static String path(String path) {
        String p = StringUtil.replace(path, "\\", "/");
        // p = StringUtils.join(StringUtils.split(p, "/"), "/");
        if (!StringUtil.startsWithAny(p, "/") && StringUtil.startsWithAny(path, "\\", "/")) {
            p += "/";
        }
        if (!StringUtil.endsWithAny(p, "/") && StringUtil.endsWithAny(path, "\\", "/")) {
            p = p + "/";
        }
        return p;
    }



    /**
         * 
         *<p>上传文件.</p>
         * @param srcFile
         * @param dirPath
         * @param bufferSize
         */	
		public static  void upload(File srcFile, String dirPath, int bufferSize) throws FileNotFoundException{
			try {
				
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new BufferedInputStream(new FileInputStream(srcFile),	bufferSize);
					out = new BufferedOutputStream(new FileOutputStream(new File(dirPath)),	bufferSize);
					byte[] buffer = new byte[bufferSize];
					
					
					int length = -1;
					while((length = in.read(buffer)) != -1)
					{
						out.write(buffer, 0, length);
					}
					
					
					out.flush();
				} finally {
					if (null != in) {
						in.close();
					}
					if (null != out) {
						out.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("上传文件时出错！！！");
			}
		}

    /**
     * 获取文件唯一名称
     *
     * @return
     */
    public static String getFileKey() {
        String suffixName = DateUtils.getCurrentDate("hhmmss");
        return StringUtil.createUUID() + suffixName;

    }


    /**
     * 获取文件后缀
     *
     * @param filename
     * @param isDot
     *            true:加“.”
     * @return
     */
    public static String getSuffix(String filename, Boolean isDot) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            if (!isDot) {
                pos = pos + 1;
            }
            suffix = filename.substring(pos);
        }
        return suffix;
    }

	 
	 /**
	  * 
	 * <p>改变文件使其具有写属性.避免文件权限为只读操作.</p>
	 * @param   fileName 文件或目录路径
	 * @return  <code>true</code> 设置文件可写,<code>false</code> 文件不存在或已经具有写属性权限.
	  */
	 public static boolean setFileWritable(String fileName) {
	       
		 File file = new File(fileName);
	        
	        if (file.exists() && file.canRead() && !file.canWrite()) {
	        	return file.setWritable(true);
	        }else {
	          return false;
	        }
	 }
	    

}
