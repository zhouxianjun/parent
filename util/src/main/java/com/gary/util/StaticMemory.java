package com.gary.util;

import java.util.Properties;

import com.gary.util.dto.DataMap;

public class StaticMemory {
	private StaticMemory(){};
	private Properties mail;
	public static DataMap<String, Object> data = new DataMap<String, Object>();
	public interface Lang {
		/** 阿尔巴尼亚语 */
		public final String SQ = "sq";
		/** 阿拉伯语 */
		public final String AR = "ar";
		/** 阿塞拜疆语 */
		public final String AZ = "az";
		/** 爱尔兰语 */
		public final String GA = "ga";
		/** 爱沙尼亚语 */
		public final String ET = "et";
		/** 巴斯克语 */
		public final String EU = "eu";
		/** 白俄罗斯语 */
		public final String BE = "be";
		/** 保加利亚语 */
		public final String BG = "bg";
		/** 冰岛语 */
		public final String IS = "is";
		/** 波兰语 */
		public final String PL = "pl";
		/** 波斯尼亚语 */
		public final String BS = "bs";
		/** 波斯语 */
		public final String FA = "fa";
		/** 布尔语(南非荷兰语) */
		public final String AF = "af";
		/** 丹麦语 */
		public final String DA = "da";
		/** 德语 */
		public final String DE = "de";
		/** 俄语 */
		public final String RU = "ru";
		/** 法语 */
		public final String FR = "fr";
		/** 菲律宾语 */
		public final String TL = "tl";
		/** 芬兰语 */
		public final String FI = "fi";
		/** 高棉语 */
		public final String KM = "km";
		/** 格鲁吉亚语 */
		public final String KA = "ka";
		/** 古吉拉特语 */
		public final String GU = "gu";
		/** 海地克里奥尔语 */
		public final String HT = "ht";
		/** 韩语 */
		public final String KO = "ko";
		/** 荷兰语 */
		public final String NL = "nl";
		/** 加利西亚语 */
		public final String GL = "gl";
		/** 加泰罗尼亚语 */
		public final String CA = "ca";
		/** 捷克语 */
		public final String CS = "cs";
		/** 卡纳达语 */
		public final String KN = "kn";
		/** 克罗地亚语 */
		public final String HR = "hr";
		/** 拉丁语 */
		public final String LA = "la";
		/** 拉脱维亚语 */
		public final String LV = "lv";
		/** 老挝语 */
		public final String LO = "lo";
		/** 立陶宛语 */
		public final String LT = "lt";
		/** 罗马尼亚语 */
		public final String RO = "ro";
		/** 马耳他语 */
		public final String MT = "mt";
		/** 马拉地语 */
		public final String MR = "mr";
		/** 马来语 */
		public final String MS = "ms";
		/** 马其顿语 */
		public final String MK = "mk";
		/** 孟加拉语 */
		public final String BN = "bn";
		/** 苗语 */
		public final String HMN = "hmn";
		/** 挪威语 */
		public final String NO = "no";
		/** 葡萄牙语 */
		public final String PT = "pt";
		/** 日语 */
		public final String JA = "ja";
		/** 瑞典语 */
		public final String SV = "sv";
		/** 塞尔维亚语 */
		public final String SR = "sr";
		/** 世界语 */
		public final String EO = "eo";
		/** 斯洛伐克语 */
		public final String SK = "sk";
		/** 斯洛文尼亚语 */
		public final String SL = "sl";
		/** 斯瓦希里语 */
		public final String SW = "sw";
		/** 宿务语 */
		public final String CEB = "ceb";
		/** 泰卢固语 */
		public final String TE = "te";
		/** 泰米尔语 */
		public final String TA = "ta";
		/** 泰语 */
		public final String TH = "th";
		/** 土耳其语 */
		public final String TR = "tr";
		/** 威尔士语 */
		public final String CY = "cy";
		/** 乌尔都语 */
		public final String UR = "ur";
		/** 乌克兰语 */
		public final String UK = "uk";
		/** 希伯来语 */
		public final String IW = "iw";
		/** 希腊语 */
		public final String EL = "el";
		/** 西班牙语 */
		public final String ES = "es";
		/** 匈牙利语 */
		public final String HU = "hu";
		/** 亚美尼亚语 */
		public final String HY = "hy";
		/** 意大利语 */
		public final String IT = "it";
		/** 意第绪语 */
		public final String YI = "yi";
		/** 印地语 */
		public final String HI = "hi";
		/** 印尼语 */
		public final String ID = "id";
		/** 印尼爪哇语 */
		public final String JW = "jw";
		/** 英语 */
		public final String EN = "en";
		/** 越南语 */
		public final String VI = "vi";
		/** 中文(繁体) */
		public final String ZH_TW = "zh-TW";
		/** 中文(简体) */
		public final String ZH_CN = "zh-CN";
	}
	public Properties getMail() {
		return mail;
	}
	public void setMail(Properties mail) {
		this.mail = mail;
	}
	public DataMap<String, Object> getData() {
		if(data == null)
			data = new DataMap<String, Object>();
		return data;
	}
}
