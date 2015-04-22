package com.gary.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.gary.compress.gzip.Gzip;
import com.gary.util.FileUtils;
import com.yahoo.platform.yui.compressor.Css;
import com.yahoo.platform.yui.compressor.JavaScript;
/**
 * YUI JS、CSS压缩
 * @author Gary
 * 需要gary-core.jar、yuicompressor.jar
 */
public class YUI {
	private static int linebreak = -1;
	private static boolean nomunge = true;
	private static boolean preserveAllSemiColons = false;
	private static boolean disableOptimizations = false;
	/**
	 * 获取压缩后的css文件源码
	 * @param file 要压缩的文件
	 * @param code 编码
	 * @return
	 * @throws IOException
	 */
	public static String getCompressCSS(File file, String code) throws IOException {
		Reader in = new InputStreamReader(new FileInputStream(file), code);
		Css compressor = new Css(in);
		return compressor.compress(FileUtils.readFile(file, code),linebreak);
	}
	/**
	 * 直接生成压缩后的文件(*.min.css)
	 * @param file 要压缩的文件
	 * @param code 编码
	 * @return
	 * @throws IOException
	 */
	public static String writerCompressCSS(File file, String code) throws IOException {
		Writer out = null;
		String outfilename = file.getAbsolutePath();
		outfilename = outfilename.substring(0,outfilename.lastIndexOf(".css")) + ".min.css";
		out = new OutputStreamWriter(new FileOutputStream(outfilename), code);
		Reader in = new InputStreamReader(new FileInputStream(file), code);
		Css compressor = new Css(in);
		compressor.compress(out,linebreak);
		out.flush();
		out.close();
		return outfilename;
	}
	/**
	 * 直接生成压缩后的文件
	 * @param file 要压缩的文件
	 * @param code 编码
	 * @param outname 输出文件名
	 * @return
	 * @throws IOException
	 */
	public static String writerCompressCSS(File file, String code, String outname) throws IOException {
		Writer out = null;
		String outfilename = file.getAbsolutePath();
		outfilename = outfilename.substring(0,outfilename.lastIndexOf("\\")+1) + outname;
		out = new OutputStreamWriter(new FileOutputStream(outfilename), code);
		Reader in = new InputStreamReader(new FileInputStream(file), code);
		Css compressor = new Css(in);
		compressor.compress(out,linebreak);
		out.flush();
		out.close();
		return outfilename;
	}
	/**
	 * 获取压缩后的js文件源码
	 * @param file 要压缩的文件
	 * @param code 编码
	 * @return
	 * @throws IOException
	 */
	public static String getCompressJs(File file, String code) throws IOException{
		Reader in = new InputStreamReader(new FileInputStream(file), code);
		JavaScript js = getJs(in);
		in.close(); in = null;
		return js.compress(linebreak, nomunge, false, preserveAllSemiColons, disableOptimizations);
	}
	/**
	 * 直接生成压缩后的文件(*.min.js)
	 * @param file 要压缩的文件
	 * @param code 编码
	 * @return
	 * @throws IOException
	 */
	public static String writerCompressJs(File file, String code) throws IOException {
		Writer out = null;
		String outfilename = file.getAbsolutePath();
		outfilename = outfilename.substring(0,outfilename.lastIndexOf(".js")) + ".min.js";
		out = new OutputStreamWriter(new FileOutputStream(outfilename), code);
		Reader in = new InputStreamReader(new FileInputStream(file), code);
		JavaScript js = getJs(in);
		js.compress(out, linebreak, nomunge, false, preserveAllSemiColons, disableOptimizations);
		out.flush();
		out.close();
		return outfilename;
	}
	/**
	 * 直接生成压缩后的文件
	 * @param file 要压缩的文件
	 * @param code 编码
	 * @param outname 输出文件名
	 * @return
	 * @throws IOException
	 */
	public static String writerCompressJs(File file, String code, String outname) throws IOException {
		Writer out = null;
		String outfilename = file.getAbsolutePath();
		outfilename = outfilename.substring(0,outfilename.lastIndexOf("\\")+1) + outname;
		out = new OutputStreamWriter(new FileOutputStream(outfilename), code);
		Reader in = new InputStreamReader(new FileInputStream(file), code);
		JavaScript js = getJs(in);
		js.compress(out, linebreak, nomunge, false, preserveAllSemiColons, disableOptimizations);
		out.flush();
		out.close();
		return outfilename;
	}
	/**
	 * 压缩文件夹下面所以css/js文件
	 * @param root 根目录
	 * @param encoding 编码
	 * @param suffix debug的文件名的后缀(debug = true)
	 * @param debug 是否debug
	 * @throws Exception
	 */
	public static void compressAll(String root,String encoding,String suffix,boolean debug) throws Exception{
		File filedb = new File(root,"compress.db");
		List<File> listdb = new ArrayList<File>();
		List<File> list = new ArrayList<File>();
		FileUtils.recursiveFile(list, new File(root), null, false);
		if(filedb.exists()){
			ObjectInputStream objin = new ObjectInputStream(new FileInputStream(filedb));
			list = (List<File>)objin.readObject();
			objin.close();
		}
		for (File file : list) {
			String string = file.getName();
			if(string.endsWith(".js") || string.endsWith(".css")){
				listdb.add(file);
				if(debug){
					StringBuffer sb = new StringBuffer(string);
					sb.insert(sb.lastIndexOf("."), suffix);
					string = sb.toString();
				}
				StringBuffer name = new StringBuffer(string);
				String yuiname;
				if(string.endsWith(".js")){
					name.insert(name.lastIndexOf(".") + 1, "gz");
					yuiname = YUI.writerCompressJs(file, encoding);
				}else{
					name.insert(name.lastIndexOf(".") + 1, "gzip.");
					yuiname = YUI.writerCompressCSS(file, encoding);
				}
				File rfile = new File(file.getParent(),name.toString());
				String fname = Gzip.compress(new File(yuiname), false);
				File f = new File(file.getParent(),fname);
				if(rfile.exists()){
					rfile.delete();
				}
				f.renameTo(rfile);
			}
		}
		if(!filedb.exists()){
			ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream(filedb));
			objout.writeObject(listdb);
			objout.flush();
			objout.close();
		}
		listdb = null;
		list = null;
	}
	private static JavaScript getJs(Reader in) throws IOException{
		return new JavaScript(in, new ErrorReporter() {

            public void warning(String message, String sourceName,
                    int line, String lineSource, int lineOffset) {
                if (line < 0) {
                    System.err.println("\n[WARNING] " + message);
                } else {
                    System.err.println("\n[WARNING] " + line + ':' + lineOffset + ':' + message);
                }
            }

            public void error(String message, String sourceName,
                    int line, String lineSource, int lineOffset) {
                if (line < 0) {
                    System.err.println("\n[ERROR] " + message);
                } else {
                    System.err.println("\n[ERROR] " + line + ':' + lineOffset + ':' + message);
                }
            }

            public EvaluatorException runtimeError(String message, String sourceName,
                    int line, String lineSource, int lineOffset) {
                error(message, sourceName, line, lineSource, lineOffset);
                return new EvaluatorException(message);
            }
        });
	}
	public static int getLinebreak() {
		return linebreak;
	}
	/**
	 * 设置多少字节换行(默认-1不换行)
	 * @param linebreak
	 */
	public static void setLinebreak(int linebreak) {
		YUI.linebreak = linebreak;
	}
	public static boolean isNomunge() {
		return nomunge;
	}
	/**
	 * 缩小 不要混淆局部符号(默认 true)
	 * @param nomunge
	 */
	public static void setNomunge(boolean nomunge) {
		YUI.nomunge = nomunge;
	}
	public static boolean isPreserveAllSemiColons() {
		return preserveAllSemiColons;
	}
	/**
	 * 保留不必要的分号(默认 false)
	 * @param preserveAllSemiColons
	 */
	public static void setPreserveAllSemiColons(boolean preserveAllSemiColons) {
		YUI.preserveAllSemiColons = preserveAllSemiColons;
	}
	public static boolean isDisableOptimizations() {
		return disableOptimizations;
	}
	/**
	 * 禁用所有内置微型优化(默认 false)
	 * @param disableOptimizations
	 */
	public static void setDisableOptimizations(boolean disableOptimizations) {
		YUI.disableOptimizations = disableOptimizations;
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println(YUI.writerCompressJs(new File("D:\\Flex\\ajaxUtil.js"), "UTF-8","xxxx.js"));
	}
}
