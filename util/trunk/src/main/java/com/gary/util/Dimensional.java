package com.gary.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public abstract class Dimensional {
	protected static Logger logger = Logger.getLogger(Dimensional.class);
	public static void baseEncode(String contents, int width, int height,
			String imgPath, ErrorCorrectionLevel level, String enc,
			BarcodeFormat type, OutputStream out) throws Exception {
		String imgType = imgPath == null ? "png" : imgPath.substring(imgPath
				.lastIndexOf(".") + 1);
		Hashtable<EncodeHintType, Object> hints = null;
		if(type != BarcodeFormat.EAN_13 && type != BarcodeFormat.EAN_8){
			hints = new Hashtable<EncodeHintType, Object>();
			// TODO 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			hints.put(EncodeHintType.ERROR_CORRECTION, level);
			// TODO 指定编码格式
			hints.put(EncodeHintType.CHARACTER_SET, enc);
		}
		BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, type,
				width, height, hints);
		if (out != null) {
			MatrixToImageWriter.writeToStream(bitMatrix, imgType, out);
		}
		if (imgPath != null) {
			MatrixToImageWriter.writeToFile(bitMatrix, imgType, new File(
					imgPath));
		}
	}

	public static String baseDecode(File img, String enc) throws Exception {
		BufferedImage image = null;
		Result result = null;
		image = ImageIO.read(img);
		if (image == null) {
			logger.debug("the decode image may be not exit.");
		}
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, enc);

		result = new MultiFormatReader().decode(bitmap, hints);
		return result.getText();
	}
	/**
	 * 解析
	 * @param imgPath
	 * @param enc
	 * @return
	 * @throws Exception
	 */
	public static String decode(String imgPath, String enc) throws Exception { 
		File file = new File(imgPath);
		if(!file.exists())
			return null;
		return baseDecode(file, enc);
    }
	public static String decode(String imgPath) throws Exception { 
		File file = new File(imgPath);
		if(!file.exists())
			return null;
		return baseDecode(file, "UTF-8");
    }
}
