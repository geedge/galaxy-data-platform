package com.mesalab.tool.file;

import java.io.File;
import java.io.FileFilter;


/**
 * 
 * <p>扩展文件过滤</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2010-11-8 下午04:55:54
 *
 */
public class FileFilterExtension implements FileFilter {

	/**
	 * 是否接受目录
	 */
	private boolean acceptDir;
	
	 /**
     *  扩展名
     */
    private String fileType;
    /**
     *  文件名开始字符
     */
    private String fileNamePattern;
    
    /**
     * 
    *<p>Title:扩展过滤文件构造方法 </p> 
    *<p>Description: 构建对象</p> 
    *@param acceptDir 是否过滤目录 true or false 
    *@param fileNamePattern 文件名
    *@param fileType 文件类型就是文件的扩展名
     */
    
    public FileFilterExtension(boolean acceptDir, String fileNamePattern, String fileType) {
        this.acceptDir = acceptDir;
        this.fileNamePattern = fileNamePattern;
        this.fileType = fileType;
    }

	@Override
	public boolean accept(File pathname) {
		
		if (pathname.isDirectory() && !acceptDir) {
            return false;
        }

        String name = pathname.getName();
        
        int idx = name.lastIndexOf(".");
        
        if (fileNamePattern != null) {
            if (!name.startsWith(fileNamePattern)) {
                return false;
            }
        }
        
        if (fileType != null) {

            if (idx == -1) {
                return false;
            } else{
            	if (idx == name.length() - 1) {
                    return false;
                } else {
                    if (!this.fileType.equals(name.substring(idx + 1))) {
                        return false;
                    }
                }
            }
            	

        }

        return true;
	}
	
	

}
