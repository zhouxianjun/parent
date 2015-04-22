package com.gary.compress.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.gary.util.FileUtils;
/**
 * GZIP压缩数据
 * @author Gary
 * 需要gary-core.jar
 */
public class Gzip {
	public static final int BUFFER = 1024;
	public static final String EXT = ".gz";
	/**
	 * 数据压缩
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 压缩
		compress(bais, baos);
		byte[] output = baos.toByteArray();
		baos.flush();
		baos.close();
		bais.close();
		return output;
	}

	/**
	 * 流压缩
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compress(InputStream is, OutputStream os)
			throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			gos.write(data, 0, count);
		}
		gos.finish();
		gos.flush();
		gos.close();
	}
	/**
	 * 文件压缩
	 * @param file 不能是文件夹
	 * @param out
	 * @param name
	 * @param delete
	 * @throws Exception
	 */
	public static String compress(File file, String out, String name, boolean delete) throws Exception {  
        FileInputStream fis = new FileInputStream(file);
        out = out.endsWith(File.separator) ? out : out + File.separator;
        File outfile = new File(out);
        if(!outfile.exists())outfile.mkdirs();
        FileOutputStream fos = new FileOutputStream(out + name + EXT);  
        compress(fis, fos);  
        fis.close();  
        fos.flush();  
        fos.close();  
        if (delete) {  
            file.delete();  
        }
        return name + EXT;
    }
	/**
	 * 文件压缩
	 * @param file 可以是文件夹
	 * @param out
	 * @param delete
	 * @throws Exception
	 */
	public static String compress(File file, String out, boolean delete) throws Exception {
		if(file.isDirectory()){
			List<File> list = new ArrayList<File>();
			FileUtils.recursiveFile(list, file, null, false);
			for (File file2 : list) {
				compress(file2, out, delete);
			}
		}else{
	        FileInputStream fis = new FileInputStream(file);
	        out = out.endsWith(File.separator) ? out : out + File.separator;
	        File outfile = new File(out);
	        if(!outfile.exists())outfile.mkdirs();
	        FileOutputStream fos = new FileOutputStream(out + file.getName() + EXT);  
	        compress(fis, fos);  
	        fis.close();  
	        fos.flush();  
	        fos.close();  
	        if (delete) {  
	            file.delete();  
	        }
		}
		return file.getName() + EXT;
    }
	/**
	 * 文件压缩
	 * @param file 可以是文件夹
	 * @param delete
	 * @throws Exception
	 */
	public static String compress(File file, boolean delete) throws Exception {
		if(file.isDirectory()){
			List<File> list = new ArrayList<File>();
			FileUtils.recursiveFile(list, file, null, false);
			for (File file2 : list) {
				compress(file2, delete);
			}
		}else{
	        FileInputStream fis = new FileInputStream(file);
	        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);  
	        compress(fis, fos);  
	        fis.close();  
	        fos.flush();  
	        fos.close();  
	        if (delete) {  
	            file.delete();  
	        }
        }
		return file.getName() + EXT;
    }
	/**
	 * 数据解压缩
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        // 解压缩  
        decompress(bais, baos);  
        data = baos.toByteArray();  
        baos.flush();  
        baos.close();  
        bais.close();  
        return data;  
    }
	/**
	 * 流解压缩
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os)  
            throws Exception {  
        GZIPInputStream gis = new GZIPInputStream(is);  
        int count;  
        byte data[] = new byte[BUFFER];  
        while ((count = gis.read(data, 0, BUFFER)) != -1) {  
            os.write(data, 0, count);  
        }  
        gis.close();  
    }
	/**
	 * 文件解压缩
	 * @param file 可以是文件夹
	 * @param delete
	 * @throws Exception
	 */
	public static void decompress(File file, boolean delete) throws Exception {
		if(file.isDirectory()){
			List<File> list = new ArrayList<File>();
			FileUtils.recursiveFile(list, file, null, false);
			for (File file2 : list) {
				decompress(file2, delete);
			}
		}else{
	        FileInputStream fis = new FileInputStream(file);  
	        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT,""));  
	        decompress(fis, fos);  
	        fis.close();  
	        fos.flush();  
	        fos.close();  
	        if (delete) {  
	            file.delete();  
	        }
		}
    }
	/**
	 * 文件解压缩
	 * @param file 可以是文件夹
	 * @param out
	 * @param delete
	 * @throws Exception
	 */
	public static void decompress(File file, String out, boolean delete) throws Exception {
		if(file.isDirectory()){
			List<File> list = new ArrayList<File>();
			FileUtils.recursiveFile(list, file, null, false);
			for (File file2 : list) {
				decompress(file2, out, delete);
			}
		}else{
	        FileInputStream fis = new FileInputStream(file);
	        out = out.endsWith(File.separator) ? out : out + File.separator;
	        File outfile = new File(out);
	        if(!outfile.exists())outfile.mkdirs();
	        out = out + file.getName();
	        out = out.endsWith(EXT) ? out.replace(EXT, "") : out;
	        FileOutputStream fos = new FileOutputStream(out);  
	        decompress(fis, fos);  
	        fis.close();  
	        fos.flush();  
	        fos.close();  
	        if (delete) {  
	            file.delete();  
	        }
		}
    }
	/**
	 * 文件解压缩
	 * @param file 不可以是文件夹
	 * @param out
	 * @param name
	 * @param delete
	 * @throws Exception
	 */
	public static void decompress(File file, String out, String name, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        out = out.endsWith(File.separator) ? out : out + File.separator;
        File outfile = new File(out);
        if(!outfile.exists())outfile.mkdirs();
        FileOutputStream fos = new FileOutputStream(out + name);  
        decompress(fis, fos);  
        fis.close();  
        fos.flush();  
        fos.close();  
        if (delete) {  
            file.delete();  
        }
    }
	public static void main(String[] args) throws Exception {
		System.out.println(Gzip.compress(new File("D:\\Gary\\workspace2\\emails\\WebRoot\\plugs\\resources\\css\\bb6.css"), false));
	}
}
