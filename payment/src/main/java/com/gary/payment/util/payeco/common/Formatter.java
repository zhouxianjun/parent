package com.gary.payment.util.payeco.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

public class Formatter {

    public static void main(String[] args) {

        Calendar c = Calendar.getInstance();
        c.set(2009, 12, 31);
        Date d = Formatter.formatSettlementDate(c.getTime(), "0101");

        System.out.println(Formatter.yyyy_MM_dd(d));
    }
    private static SimpleDateFormat yy = null;
    private static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = null;
    private static SimpleDateFormat yyMMddHHmmss = null;
    private static SimpleDateFormat yyyyMMdd = null;
    private static SimpleDateFormat yyyy_MM_dd = null;
    private static SimpleDateFormat HH_mm_ss_SSS = null;
    private static SimpleDateFormat HH_mm_ss = null;
    private static SimpleDateFormat yyyyMMddHHmmss = null;
    private static SimpleDateFormat MMddHHmmss = null;
    private static SimpleDateFormat yyMMdd = null;
    private static SimpleDateFormat yyyy = null;
    private static SimpleDateFormat MMdd = null;
    private static NumberFormat amountFormatter = null;
    private static NumberFormat numberFormatter = null;

    public static String MMddHHmmss(Date date) {
        if (MMddHHmmss == null) {
            MMddHHmmss = new SimpleDateFormat("MMddHHmmss");
        }
        return MMddHHmmss.format(date);
    }

    public static String MMdd(Date date) {
        if (MMdd == null) {
            MMdd = new SimpleDateFormat("MMdd");
        }
        return MMdd.format(date);
    }

    public static String yyyy(Date date) {
        if (yyyy == null) {
            yyyy = new SimpleDateFormat("yyyy");
        }
        return yyyy.format(date);
    }

    public static String yyyy_MM_dd(Date date) {
        if (yyyy_MM_dd == null) {
            yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
        }
        return yyyy_MM_dd.format(date);
    }

    public static String yyMMdd(Date date) {
        if (yyMMdd == null) {
            yyMMdd = new SimpleDateFormat("yyMMdd");
        }
        return yyMMdd.format(date);
    }

    public static String yy(Date year) {
        if (yy == null) {
            yy = new SimpleDateFormat("yy");
        }
        return yy.format(year);
    }

    public static Date formatSettlementDate(Date transDate, String settleDate) {
        int month = Integer.parseInt(settleDate.substring(0, 2));
        int day = Integer.parseInt(settleDate.substring(2));
        String strTmp = Formatter.yyyyMMdd(transDate);
        int year = Integer.parseInt(strTmp.substring(0, 4));
        int month2 = Integer.parseInt(strTmp.substring(4, 6));

        Calendar c = Calendar.getInstance();
        if (month2 == 12 && month == 1) {
            year++;
        }
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatTime1(Date time) {
        if (yyyy_MM_dd_HH_mm_ss == null) {
            yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return yyyy_MM_dd_HH_mm_ss.format(time);
    }

    public static String yyyy_MM_dd_HH_mm_ss(Date time) {
        if (yyyy_MM_dd_HH_mm_ss == null) {
            yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return yyyy_MM_dd_HH_mm_ss.format(time);
    }

    public static String yyMMddHHmmss(Date time) {
        if (yyMMddHHmmss == null) {
            yyMMddHHmmss = new SimpleDateFormat("yyMMddHHmmss");
        }
        return yyMMddHHmmss.format(time);
    }

    public static String yyyyMMdd(Date time) {
        if (yyyyMMdd == null) {
            yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        }
        return yyyyMMdd.format(time);
    }

    public static String HH_mm_ss_SSS(Date time) {
        if (HH_mm_ss_SSS == null) {
            HH_mm_ss_SSS = new SimpleDateFormat("HH:mm:ss.SSS");
        }
        return HH_mm_ss_SSS.format(time);
    }

    public static String HHmmss(Date time) {
        if (HH_mm_ss == null) {
            HH_mm_ss = new SimpleDateFormat("HHmmss");
        }
        return HH_mm_ss.format(time);
    }

    public static String yyyyMMddHHmmss(Date time) {
        if (yyyyMMddHHmmss == null) {
            yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        }
        return yyyyMMddHHmmss.format(time);
    }

    public static String formatAmount(Double number) {
    	return formatAmount(number, 2, 12);
    }

    public static String formatAmount(Double number, int fractionDigits, int integerDigits) {
        return formatAmount((Object)number, fractionDigits, integerDigits);
    }

    public static String formatAmount(Object number, int fractionDigits, int integerDigits) {
        if (amountFormatter == null) {
            amountFormatter = NumberFormat.getInstance();
            amountFormatter.setMaximumFractionDigits(fractionDigits);
            amountFormatter.setMaximumIntegerDigits(integerDigits);
            amountFormatter.setGroupingUsed(false);
        }
        return amountFormatter.format(number);
    }

    public static String formatNumber(Double number) {
        if (numberFormatter == null) {
            numberFormatter = NumberFormat.getInstance();
            numberFormatter.setGroupingUsed(false);
        }
        return numberFormatter.format(number);
    }

    public static String formatString(String str, int beginSize, int endSize, String leftFill, String rightFill) throws Exception {

        while (beginSize > 0) {
            str = leftFill + str;
            beginSize--;
        }
        if (str.getBytes("gbk").length > endSize) {
            byte[] temp = str.getBytes();
            byte[] newbyte = new byte[endSize];
            for (int i = 0; i < newbyte.length; i++) {
                newbyte[i] = temp[i];
            }
            str = new String(newbyte);
        }

        while (str.getBytes("gbk").length < endSize) {
            str += rightFill;
        }
        return str;
    }

    public static String formatString(String str, int endSize) {
        try {
            return formatString(str, 0, endSize, " ", " ");
        } catch (Exception e) {
            return "格式化字符串出现异常:" + e;
        }
    }

    public static String formatBytes(byte[] bts) {
        String tmp = "";
        for (byte b : bts) {
            tmp += " " + b;
        }
        return tmp;
    }

    public static byte[] base64Decode(String data) throws IOException {
        if(ToolKit.isNullOrEmpty(data))
            return null;
        return new sun.misc.BASE64Decoder().decodeBuffer(data);
    }

    public static String base64Encode(byte[] data) throws IOException {
        if(data == null)
            return "";
        return new sun.misc.BASE64Encoder().encode(data);
    }
    
	private static char[] codec_table = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
		'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
		'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', '+', '/' };
	
    public static String encode(byte[] a) {
		
		int totalBits = a.length * 8;
		int nn = totalBits % 6;
		int curPos = 0;// process bits
		StringBuffer toReturn = new StringBuffer();
		while (curPos < totalBits) {
			int bytePos = curPos / 8;
			switch (curPos % 8) {
			case 0:
				toReturn.append(codec_table[(a[bytePos] & 0xfc) >> 2]);
				break;
			case 2:

				toReturn.append(codec_table[(a[bytePos] & 0x3f)]);
				break;
			case 4:
				if (bytePos == a.length - 1) {
					toReturn.append(codec_table[((a[bytePos] & 0x0f) << 2) & 0x3f]);
				} else {
					int pos = (((a[bytePos] & 0x0f) << 2) | ((a[bytePos + 1] & 0xc0) >> 6)) & 0x3f;
					toReturn.append(codec_table[pos]);
				}
				break;
			case 6:
				if (bytePos == a.length - 1) {
					toReturn.append(codec_table[((a[bytePos] & 0x03) << 4) & 0x3f]);
				} else {
					int pos = (((a[bytePos] & 0x03) << 4) | ((a[bytePos + 1] & 0xf0) >> 4)) & 0x3f;
					toReturn.append(codec_table[pos]);
				}
				break;
			default:
				//never hanppen
				break;
			}
			curPos+=6;
		}
		if(nn==2)
		{
			toReturn.append("==");
		}
		else if(nn==4)
		{
			toReturn.append("=");
		}
		return toReturn.toString();

	}
    
    public static String getCurrencyName(String currencyType){
    	if(ToolKit.isNullOrEmpty(currencyType)){
    		return "人民币";
    	}
		if(currencyType.equals("01")){
			return "人民币";
		}
		if(currencyType.equals("02")){
			return "港币";
		}
		if(currencyType.equals("03")){
			return "澳门币";
		}
		if(currencyType.equals("04")){
			return "台币";
		}
		if(currencyType.equals("05")){
			return "马元";
		}
		if(currencyType.equals("06")){
			return "朝鲜";
		}
		if(currencyType.equals("07")){
			return "泰铢";
		}
		if(currencyType.equals("08")){
			return "美元";
		}
		if(currencyType.equals("09")){
			return "日元";
		}
		if(currencyType.equals("10")){
			return "韩币";
		}
		if(currencyType.equals("11")){
			return "新元";
		}
		if(currencyType.equals("12")){
			return "欧元";
		}
		if(currencyType.equals("13")){
			return "英镑";
		}
		return "人民币";
	}
    
    public static String format2Date(String src){
    	if(!ToolKit.isNullOrEmpty(src) && src.length() == 10){
    		return "20" + src.substring(0, 2) + "-" + src.substring(2, 4) + "-" + src.substring(4, 6) 
    				+ " " + src.substring(6, 8) + ":" + src.substring(8, 10) + ":" + src.substring(10);
    	}
    	return src;
    }
}
