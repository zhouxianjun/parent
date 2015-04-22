package com.gary.util.dto;

import java.awt.Color;
import java.io.Serializable;

public class ImageMark implements Serializable{

	/**
	 * 图片水印实体类
	 * 自定义内容水印
	 * @author Gary,lengreen
	 */
	private static final long serialVersionUID = -1018175191589059804L;
	private String filePath; //需要添加水印的图片的路径
	private String markContent;//水印的文字
	private Color markContentColor;//水印文字的颜色
	private float qualNum;//图片质量
	private String fontName;//字体
	private int fontSize;//字体大小	
	private int fontStyle;//字体样式
	private float alpha;//透明度
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public int getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getMarkContent() {
		return markContent;
	}
	public void setMarkContent(String markContent) {
		this.markContent = markContent;
	}
	public Color getMarkContentColor() {
		return markContentColor;
	}
	public void setMarkContentColor(Color markContentColor) {
		this.markContentColor = markContentColor;
	}
	public float getQualNum() {
		return qualNum;
	}
	public void setQualNum(float qualNum) {
		this.qualNum = qualNum;
	}
}
