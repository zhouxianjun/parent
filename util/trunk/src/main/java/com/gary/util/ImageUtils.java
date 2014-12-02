package com.gary.util;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtils {
	public static File dwindle(InputStream in, double width, double height,
			String outFilePath, boolean proportion) throws Exception {
		File file = new File(outFilePath);
		FileOutputStream tempout = null;
		tempout = new FileOutputStream(file);
		Image img = null;
		Applet app = new Applet();
		MediaTracker mt = new MediaTracker(app);
		img = ImageIO.read(in);
		mt.addImage(img, 0);
		mt.waitForID(0);

		int new_w;
		int new_h;
		if (proportion) { // 判断是否是等比缩放.
		// 为等比缩放计算输出的图片宽度及高度
			double rate1 = ((double) img.getWidth(null)) / width;
			double rate2 = ((double) img.getHeight(null)) / height;
			double rate = rate1 > rate2 ? rate1 : rate2;
			new_w = (int) (((double) img.getWidth(null)) / rate);
			new_h = (int) (((double) img.getHeight(null)) / rate);
		} else {
			new_w = (int) width; // 输出的图片宽度
			new_h = (int) height; // 输出的图片高度
		}
		BufferedImage buffImg = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = buffImg.createGraphics();

		g.setColor(new Color(Color.TRANSLUCENT));
		g.fillRect(0, 0, new_w, new_h);

		g.drawImage(img, 0, 0, new_w, new_h, null);
		g.dispose();

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(tempout);
		encoder.encode(buffImg);
		tempout.close();
		return file;
	}
}
