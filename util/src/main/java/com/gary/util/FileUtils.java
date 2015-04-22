package com.gary.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.gary.util.dto.ImageMark;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 文件工具类
 * 
 * @author Gary
 * 
 */
@SuppressWarnings("restriction")
public abstract class FileUtils {
	protected static Logger logger = Logger.getLogger(FileUtils.class);
	
	public static void byte2File(File file, byte[] b) throws Exception{
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
		stream.write(b);
		stream.flush();
		stream.close();
	}
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param sPath
	 * @return
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				files[i].delete();
			} // 删除子目录
			else {
				deleteDirectory(files[i].getAbsolutePath());
			}
		}
		// 删除当前目录
		return dirFile.delete();
	}

	/**
	 * 移动单个文件
	 * 
	 * @param path
	 * @param newPath
	 */
	public static void moveFile(String path, String newPath) {
		// 文件原地址
		File oldFile = new File(path);
		// new一个新文件夹
		File fnewpath = new File(newPath);
		// 判断文件夹是否存在
		if (!fnewpath.exists())
			fnewpath.mkdirs();
		// 将文件移到新文件里
		File fnew = new File(newPath + oldFile.getName());
		oldFile.renameTo(fnew);
	}

	/**
	 * 递归文件夹
	 * 
	 * @param root
	 *            根文件夹 File
	 * @param suffix
	 *            如果isDirectory=true 文件夹名字包含suffix 否则 文件后缀suffix
	 * @param isDirectory
	 *            boolean true = 是文件夹 false = 是文件
	 * @param list
	 *            存结果集的集合
	 * @return List<File>
	 */
	public static List<File> recursiveFile(List<File> list, File root,
			String suffix, boolean isDirectory) {
		String[] files = root.list();
		for (String string : files) {
			File file = new File(root, string);
			if (!isDirectory) {
				if (suffix == null) {
					list.add(file);
				} else {
					if (string.endsWith(suffix)) {
						list.add(file);
					}
				}
			}
			if (file.isDirectory()) {
				recursiveFile(list, file, suffix, isDirectory);
				if (isDirectory) {
					if (suffix == null) {
						list.add(file);
					} else {
						if (string.indexOf(suffix) > -1) {
							list.add(file);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 判断path文件中是否包含str字符
	 * 
	 * @param path
	 *            文件File
	 * @param readCode
	 *            文件读取编码 NULL则为系统默认
	 * @param str
	 *            要判断包含的字符
	 * @return boolean
	 */
	public static boolean readIsFileHaveStr(File file, String readCode,
			String str) {
		if (file.exists() && !file.isDirectory()) {
			try {
				FileReader fr = new FileReader(file.getAbsolutePath());// 创建FileReader对象，用来读取字符流
				BufferedReader br = readCode != null ? new BufferedReader(
						new InputStreamReader(new FileInputStream(file),
								readCode)) : new BufferedReader(fr); // 缓冲指定文件的输入
				while (br.ready()) {
					String myreadline = br.readLine();// 读取一行
					if (myreadline != null && myreadline.indexOf(str) > -1) {
						br.close();
						br.close();
						fr.close();
						return true;
					}
				}
				br.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return false;
			}
		}
		return false;
	}

	/**
	 * 查找根文件夹里面的哪些文件是否包含了str字符
	 * 
	 * @param root
	 *            根文件夹 File
	 * @param suffix
	 *            = isDirectory==true ? 文件夹名字包含 : 文件后缀
	 * @param readCode
	 *            文件读取编码 NULL则为系统默认
	 * @param str
	 *            要判断包含的字符
	 * @return List<File>
	 */
	public static List<File> getFileHaveStr(File root, String suffix,
			String readCode, String str) {
		List<File> listfile = new ArrayList<File>();
		List<File> list = new ArrayList<File>();
		recursiveFile(list, root, suffix, false);
		for (File file : list) {
			if (readIsFileHaveStr(file, readCode, str)) {
				listfile.add(file);
			}
		}
		return listfile;
	}

	/**
	 * 写入文件
	 * 
	 * @param path
	 *            文件夹路径注意：最后面要加\\或者/
	 * @param name
	 *            文件名字
	 * @param type
	 *            文件后缀".txt"
	 * @param str
	 *            内容
	 * @param writer
	 *            是否在文件里面继续写入
	 * @return boolean
	 */
	public static boolean write(String path, String name, String type,
			String str, boolean writer) {
		try {
			String filePath = path + name + type;
			File fileDirectory = new File(path);// 文件夹的路径
			if (!fileDirectory.exists()) {
				fileDirectory.mkdirs();
			}// 文件夹不存在就创建
			File file = new File(filePath);// 文件的路径
			if (!file.exists()) {
				file.createNewFile();
			}// 文件不存在就创建
			FileWriter fw = new FileWriter(file.getPath(), writer);// 创建FileWriter对象，用来写入字符流
			BufferedWriter bw = new BufferedWriter(fw); // 将缓冲对文件的输出
			bw.write(str); // 写入文件
			bw.newLine();
			bw.flush(); // 刷新该流的缓冲
			bw.close();
			fw.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * 写入文件
	 * 
	 * @param path
	 *            文件夹路径注意：最后面要加\\或者/
	 * @param name
	 *            文件名字
	 * @param type
	 *            文件后缀".txt"
	 * @param str
	 *            内容
	 * @param writer
	 *            是否在文件里面继续写入
	 * @return boolean
	 */
	public static boolean write(String path, String name, String str,
			boolean writer) {
		try {
			String filePath = path + name;
			File fileDirectory = new File(path);// 文件夹的路径
			if (!fileDirectory.exists()) {
				fileDirectory.mkdirs();
			}// 文件夹不存在就创建
			File file = new File(filePath);// 文件的路径
			if (!file.exists()) {
				file.createNewFile();
			}// 文件不存在就创建
			FileWriter fw = new FileWriter(file.getPath(), writer);// 创建FileWriter对象，用来写入字符流
			BufferedWriter bw = new BufferedWriter(fw); // 将缓冲对文件的输出
			bw.write(str); // 写入文件
			bw.newLine();
			bw.flush(); // 刷新该流的缓冲
			bw.close();
			fw.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * 给图片添加水印
	 * 
	 * @return
	 */
	public static boolean createMark(ImageMark img) {
		ImageIcon imgIcon = new ImageIcon(img.getFilePath());
		Image theImg = imgIcon.getImage();
		int width = theImg.getWidth(null);
		int height = theImg.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bimage.createGraphics();
		g.setColor(img.getMarkContentColor());
		g.setBackground(Color.white);
		g.drawImage(theImg, 0, 0, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				img.getAlpha()));
		// g.drawImage(waterImg, width*2, height, null );
		if (width < 350) {
			g.setFont(new Font(img.getFontName(), img.getFontStyle(),
					(width - width / 5) / (img.getFontSize() + 10)));
			g.drawString(img.getMarkContent(), width - width / 5, height
					- height / 18); // 添加水印的文字和设置水印文字出现的内容
		} else if (width > 350 && width < 800) {
			g.setFont(new Font(img.getFontName(), img.getFontStyle(),
					(width - width / 8) / (img.getFontSize() + 20)));
			g.drawString(img.getMarkContent(), width - width / 8, height
					- height / 18); // 添加水印的文字和设置水印文字出现的内容
		} else {
			g.setFont(new Font(img.getFontName(), img.getFontStyle(),
					(width - width / 11) / (img.getFontSize() + 30)));
			g.drawString(img.getMarkContent(), width - width / 11, height
					- height / 18); // 添加水印的文字和设置水印文字出现的内容
		}
		g.dispose();
		try {
			FileOutputStream out = new FileOutputStream(img.getFilePath());
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
			param.setQuality(img.getQualNum(), true);
			encoder.encode(bimage, param);
			out.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean createMark(File oImage, String newImageName,
			String waterText, Color color, Font font, float alpha, boolean isCenter)
			throws IOException {
		BufferedImage originalImage = ImageIO.read(oImage);
		int originalWidth = originalImage.getWidth(null);
		int originalHeight = originalImage.getHeight(null);
		if (originalWidth < 50 || originalHeight < 50) {
			return false;
		}
		if (waterText == null || waterText.trim().equals("")) {
			return false;
		}
		if (newImageName == null)
			return false;
		String fileType = newImageName
				.substring(newImageName.lastIndexOf(".") + 1);
		int len = waterText.length();
		BufferedImage newImage = new BufferedImage(originalWidth,
				originalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(originalImage, 0, 0, originalWidth, originalHeight, null);
		g.setColor(color);
		g.setFont(font);
		g.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_ATOP, alpha));
		if (!isCenter) {
			g.drawString(waterText, originalWidth - len * font.getSize() / 2
					- 6, originalHeight - 6);
		} else {
			g.drawString(waterText,
					(originalWidth - (len * font.getSize())) / 2,
					(originalHeight - font.getSize()) / 2);
		}
		g.dispose();
		ImageIO.write(newImage, fileType, new FileOutputStream(newImageName));// 存盘
		return true;
	}
	
	public static boolean createMark(File oImage, String newImageName,
			String waterText, Color color, Font font, float alpha, int left, int right)
			throws IOException {
		BufferedImage originalImage = ImageIO.read(oImage);
		int originalWidth = originalImage.getWidth(null);
		int originalHeight = originalImage.getHeight(null);
		if (originalWidth < 50 || originalHeight < 50) {
			return false;
		}
		if (waterText == null || waterText.trim().equals("")) {
			return false;
		}
		if (newImageName == null)
			return false;
		String fileType = newImageName
				.substring(newImageName.lastIndexOf(".") + 1);
		BufferedImage newImage = new BufferedImage(originalWidth,
				originalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(originalImage, 0, 0, originalWidth, originalHeight, null);
		g.setColor(color);
		g.setFont(font);
		g.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_ATOP, alpha));
		g.drawString(waterText, left, right);
		g.dispose();
		ImageIO.write(newImage, fileType, new FileOutputStream(newImageName));// 存盘
		return true;
	}

	public static boolean createMark(File oImage, String newImageName,
			String logoPath, float alpha, boolean isCenter) throws IOException {
		File waterMarkImage = new File(logoPath);
		if (!waterMarkImage.exists()) {
			return false;
		}
		if (newImageName == null)
			return false;
		String fileType = newImageName
				.substring(newImageName.lastIndexOf(".") + 1);
		BufferedImage originalImage = ImageIO.read(oImage);
		BufferedImage watermarkImage = ImageIO.read(waterMarkImage);
		int originalWidth = originalImage.getWidth(null);
		int originalHeight = originalImage.getHeight(null);
		int watermarkWidth = watermarkImage.getWidth(null);
		int watermarkHeight = watermarkImage.getHeight(null);
		if (originalWidth <= watermarkWidth
				|| originalHeight <= watermarkHeight || originalWidth < 50
				|| originalHeight < 50) {
			return false;
		}
		BufferedImage newImage = new BufferedImage(originalWidth,
				originalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(originalImage, 0, 0, originalWidth, originalHeight, null);
		g.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_ATOP, alpha));
		if (!isCenter) {
			g.drawImage(watermarkImage, originalWidth - watermarkWidth - 10,
					originalHeight - watermarkHeight - 10, watermarkWidth,
					watermarkHeight, null);
		} else {
			g.drawImage(watermarkImage, (originalWidth - watermarkWidth) / 2,
					(originalHeight - watermarkHeight) / 2, watermarkWidth,
					watermarkHeight, null);
		}
		g.dispose();
		ImageIO.write(newImage, fileType, new FileOutputStream(newImageName));// 存盘
		return true;
	}

	/**
	 * 用于将上传文件写入硬盘的
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean copy(File src, File dst, int size) {
		boolean result = false;
		InputStream in = null;
		OutputStream out = null;
		try {
			if (!dst.exists()) {
				dst.createNewFile();
			}
			in = new BufferedInputStream(new FileInputStream(src), size);
			out = new BufferedOutputStream(new FileOutputStream(dst), size);
			byte[] buffer = new byte[size];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return result;
	}

	public static void get(List<File> list, File root, String suffix,
			boolean isDirectory) {
		String[] files = root.list();
		for (String string : files) {
			File file = new File(root, string);
			if (string.endsWith(suffix)) {
				list.add(file);
			}
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file
	 * @param readCode
	 * @return String
	 */
	public static String readFile(File file, String readCode) {
		StringBuffer re = new StringBuffer();
		if (file.exists() && !file.isDirectory()) {
			try {
				FileReader fr = new FileReader(file.getAbsolutePath());// 创建FileReader对象，用来读取字符流
				BufferedReader br = readCode != null ? new BufferedReader(
						new InputStreamReader(new FileInputStream(file),
								readCode)) : new BufferedReader(fr); // 缓冲指定文件的输入
				while (br.ready()) {
					re.append(br.readLine()).append("\n");// 读取一行
				}
				br.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				return null;
			}
		}
		return re.toString();
	}

	public static void main(String[] args) throws IOException {
		/*
		 * List<File> list=recursiveFile(new File("F:\\Gary\\JavaScript素材"),
		 * ".java", false); System.out.println(list.size()); for (File file :
		 * list) { System.out.println(file.getName()); }
		 */
		// System.out.println(readIsFileHaveStr(new
		// File("F:\\Gary\\JavaScript素材\\JavaScript\\p1.htm"), null, "等于"));
		/*
		 * List<File> list=getFileHaveStr(new File("F:\\Gary\\JavaScript素材"),
		 * null, null, "System.out.println"); for (File file : list) {
		 * System.out.println(file.getPath()); write("f:\\fffff\\sss\\", "gary",
		 * ".txt", file.getPath(), true); }
		 */
		/*
		 * System.out.println("user".endsWith("user")); String path =
		 * "F:\\apache-tomcat-6.0.32\\apache-tomcat-6.0.32\\webapps\\Ucs";
		 * List<File> list = new ArrayList<File>(); System.out.println(path);
		 * recursiveFile(list,new File(path), "user", true); path =
		 * list.get(0).getAbsolutePath(); if(path!=null&&!path.equals("")){ list
		 * = new ArrayList<File>(); list=recursiveFile(list,new File(path),
		 * ".js", false); for (File file : list) {
		 * System.out.println(file.getName()); } }
		 */
		/*
		 * List<File> list = getFileHaveStr( new File(
		 * "F:\\Gary\\work_bak\\op\\server\\Synergy.ServerAPI\\WEB-INF\\classes\\"
		 * ), null, "GBK", "localhost:3306/xtzx"); for (File file : list) {
		 * System.out.println(file.getAbsolutePath()); }
		 */
		createMark(new File("E:\\Gary\\code.png"), "E:\\Gary\\new1.png", 
				"Gary", Color.RED, new Font("宋体", Font.PLAIN, 13), 0.5f,false);
		createMark(new File("E:\\Gary\\code.png"), "E:\\Gary\\new.png",
				"C:\\Program Files\\图片处理（Gary）\\MinLogo.png", 1.0f,true);
	}
}
