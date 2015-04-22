package com.gary.compress.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
/**
 * ZIP操作
 * @author Gary
 * 需要apache-ant.jar
 */
public class Zip {
	/**
	 * ZIP打包压缩
	 * @param strPaths 需要打包的路径
	 * @param destFile 存放路径
	 * @param gl 过滤 例如：*.jpg
	 */
	public static void compress(String strPaths, String destFile,String gl) {
		Project pj = new Project();
		org.apache.tools.ant.taskdefs.Zip zip = new org.apache.tools.ant.taskdefs.Zip();
		zip.setProject(pj);
		zip.setDestFile(new File(destFile));// 打包完的目标文件
		FileSet fileSet = new FileSet();
		fileSet.setProject(pj);
		 fileSet.setDir(new File(strPaths));//需要打包的路径
		 if(gl !=null)
			 fileSet.setIncludes(gl);//文件过滤
		zip.addFileset(fileSet);
		zip.execute();
	}
	/** 
     * 使用 org.apache.tools.zip.ZipFile 解压文件，它与 java 类库中的 
     * java.util.zip.ZipFile 使用方式是一新的，只不过多了设置编码方式的 
     * 接口。 
     *  
     * 注，apache 没有提供 ZipInputStream 类，所以只能使用它提供的ZipFile 
     * 来读取压缩文件。 
     * @param archive 压缩包路径 
     * @param decompressDir 解压路径 
     * @param enc 编码
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ZipException 
     */  
    public static void readByApacheZipFile(String archive, String decompressDir,String enc)  
            throws IOException, FileNotFoundException, ZipException {  
        BufferedInputStream bi = null;  
  
        ZipFile zf = new ZipFile(archive, enc);//支持中文   
  
        Enumeration<?> e = zf.getEntries();
        while (e.hasMoreElements()) {  
            ZipEntry ze2 = (ZipEntry) e.nextElement();  
            String entryName = ze2.getName();  
            String path = decompressDir + "/" + entryName;  
            if (ze2.isDirectory()) {  
                File decompressDirFile = new File(path);  
                if (!decompressDirFile.exists()) {  
                    decompressDirFile.mkdirs();  
                }  
            } else {  
                String fileDir = path.substring(0, path.lastIndexOf("/"));  
                File fileDirFile = new File(fileDir);  
                if (!fileDirFile.exists()) {  
                    fileDirFile.mkdirs();  
                }  
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(  
                        decompressDir + "/" + entryName));  
  
                bi = new BufferedInputStream(zf.getInputStream(ze2));  
                byte[] readContent = new byte[1024];  
                int readCount = bi.read(readContent);  
                while (readCount != -1) {  
                    bos.write(readContent, 0, readCount);  
                    readCount = bi.read(readContent);  
                }  
                bos.close();  
            }  
        }  
        if(bi != null)bi.close();
        zf.close();  
    }  
    public static void main(String[] args) {
		compress("E:\\Gary\\新建文件夹\\aiwan\\aiwan", "E:\\Gary\\xx.zip", null);
	}
}
