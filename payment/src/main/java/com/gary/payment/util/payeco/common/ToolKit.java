package com.gary.payment.util.payeco.common;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ToolKit
{
    public static void main(String[] args)
    {
        try
        {
            ToolKit.writeLog(ToolKit.class.getName(), "main", "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static SimpleDateFormat DateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

    public synchronized static void writeLog(LogLevel logLevel, String processName,
        String threadName, String eventMessage)
    {

        Logger logger = Logger.getLogger(processName);
        if (processName != null)
            processName = processName.replace(" ", ",");
        if (threadName != null)
            threadName = threadName.replace(" ", ",");

        eventMessage = threadName + " " + eventMessage;

        switch (logLevel)
        {
            case DEBUG:
                logger.debug(eventMessage);
                break;
            case INFO:
                logger.info(eventMessage);
                break;
            case WARN:
                logger.warn(eventMessage);
                break;
            case EXCEPTION:
                logger.error(eventMessage);
                break;
            case ERROR:
                logger.fatal(eventMessage);
                break;
            default:
                logger.info(eventMessage);
        }
    }

    public synchronized static void writeLog(String processName,
        String threadName, String message, Exception eventMessage)
    {
        String exStr = "";
        if (eventMessage != null)
        {
            exStr = "" + eventMessage.getMessage();
            for (StackTraceElement ste : eventMessage.getStackTrace())
                exStr = exStr + " " + ste.toString();
        }
        writeLog(LogLevel.EXCEPTION, processName, threadName, exStr);
    }

    public synchronized static void writeLog(String processName, String message,
        Exception eventMessage)
    {
        String exStr = "";
        if (eventMessage != null)
        {
            exStr = "" + eventMessage.getMessage();
            for (StackTraceElement ste : eventMessage.getStackTrace())
                exStr = exStr + " " + ste.toString();
        }
        writeLog(LogLevel.EXCEPTION, processName, Thread.currentThread().getName(), exStr);
    }

    public synchronized static void writeLog(LogLevel logLevel, String processName, String eventMessage)
    {
        writeLog(logLevel, processName, Thread.currentThread().getName(), eventMessage);
    }

    public synchronized static void writeLog(String processName, String eventMessage)
    {
        writeLog(LogLevel.DEBUG, processName, Thread.currentThread().getName(), eventMessage);
    }

    public synchronized static void writeLog(String processName, String threadName, String eventMessage)
    {
        writeLog(LogLevel.INFO, processName, threadName, eventMessage);
    }

    public static Integer toInt(LogLevel level)
    {
        if (level == LogLevel.DEBUG)
            return 0;
        else if (level == LogLevel.INFO)
            return 1;
        else if (level == LogLevel.WARN)
            return 2;
        else if (level == LogLevel.EXCEPTION)
            return 3;
        else
            return 4;
    }

    public synchronized static String getPropertyFromFile(String filename, String key)
    {
    	try{
	        ResourceBundle rb = ResourceBundle.getBundle(filename);
	        return rb.getString(key).trim();
    	}catch(Exception e){
    		ToolKit.writeLog(ToolKit.class.getName(), e.getMessage());
    		return "";
    	}
    }

    public synchronized static String getPropertyFromFile(String key)
    {
    	try{
	        ResourceBundle rb = ResourceBundle.getBundle("systemsetting");
	        return rb.getString(key).trim();
    	}catch(Exception e){
    		ToolKit.writeLog(ToolKit.class.getName(), e.getMessage());
    		return "";
    	}
    }
    

	private static char byte2Char(byte no) {

		char[] table = {
				0x00+'0',
				0x01+'0',
				0x02+'0',
				0x03+'0',
				0x04+'0',
				0x05+'0',
				0x06+'0',
				0x07+'0',
				0x08+'0',
				0x09+'0',
				0x00+'A',
				0x01+'A',
				0x02+'A',
				0x03+'A',
				0x04+'A',
				0x05+'A'
		};
		return table[no];
	}
	private static byte byte2OByte(byte c) {

		if(c >= '0' && c <= '9') {
			c = (byte)(c - '0');
		} else if(c >= 'a' && c <= 'z') {
			c = (byte)(c - 'a' + 0x0A);
		} else if(c >= 'A' && c <= 'Z') {
			c = (byte)(c - 'A' + 0x0A);
		}

		return c;
	}

	public static boolean isNumber(String num) {
		
		byte[] temp = num.getBytes();
		
		for(int i = 0;i < temp.length;i++) {
			byte a = temp[i];
			if(a < '0' || a > '9') return false;
		}
		return true;
	}
	
	public static byte[] str2OBytes(String str) {

		byte[] result = str.getBytes();

		for(int i = 0;i < result.length;i++) {

			result[i] = byte2OByte(result[i]);
		}
		return result;
	}

	public static byte[] bcdStr2Bytes(String bcd,boolean leftAdd0) {

		if(leftAdd0) {
			while(bcd.length()%2 != 0) bcd = "0" + bcd;
		} else {
			while(bcd.length()%2 != 0) bcd += "0";
		}

		byte[] temp = bcd.getBytes();
		byte[] result = new byte[temp.length/2];

		for(int i = 0;i < result.length;i++) {

			byte h = byte2OByte(temp[2*i]);
			byte l = byte2OByte(temp[2*i+1]);

			result[i] = (byte)((h << 4) + l);
		}
		return result;
	}

	public static String bcdBytes2Str(byte[] bytes) {
		return bcdBytes2Str(bytes,false,false);
	}
	public static String bcdBytes2Str(byte[] bytes,boolean cut,boolean cutLeft) {

		StringBuffer temp = new StringBuffer(bytes.length * 2); 

		for(int i = 0;i < bytes.length;i++) {

			byte h = (byte)((bytes[i]&0xf0) >>> 4);
			byte l = (byte)(bytes[i]&0x0f);

			temp.append(byte2Char(h)).append(byte2Char(l));
		}

		return cut?(cutLeft?temp.toString().substring(1):temp.toString().substring(0,temp.length()-1)):temp.toString();
	}

	public static String bin2BinStr(byte[] bytes) {

		StringBuffer temp = new StringBuffer(bytes.length*8); 

		for(int i = 0;i < bytes.length;i++) {

			temp.append((byte)((bytes[i]&0x80) >>> 7));
			temp.append((byte)((bytes[i]&0x40) >>> 6));
			temp.append((byte)((bytes[i]&0x20) >>> 5));
			temp.append((byte)((bytes[i]&0x10) >>> 4));
			temp.append((byte)((bytes[i]&0x08) >>> 3));
			temp.append((byte)((bytes[i]&0x04) >>> 2));
			temp.append((byte)((bytes[i]&0x02) >>> 1));
			temp.append((byte)((bytes[i]&0x01)));
		}

		return temp.toString();
	}

	public static byte[] str2Bcd(String str) {
		return str2Bcd(str,true);
	}

	public static byte[] str2Bcd(String str,boolean leftAdd0) {

		if(leftAdd0) {
			while(str.length()%2 != 0) str = "0" + str;
		} else {
			while(str.length()%2 != 0) str += "F";
		}

		byte[] temp = str.getBytes();
		byte[] result = new byte[temp.length/2];

		for(int i = 0;i < result.length;i++) {

			byte h = byte2OByte(temp[2*i]);
			byte l = byte2OByte(temp[2*i+1]);

			result[i] = (byte)((h << 4) + l);
		}

		return result;
	}


	public static byte[] binBytes2AscBytes(byte[] bin) {
		
		byte[] result = new byte[bin.length*8];
		
		for(int i = 0;i < bin.length;i++) {
			
			result[8*i]     = (byte)((bin[i]&0x80) >>> 7);
			result[8*i + 1] = (byte)((bin[i]&0x40) >>> 6);
			result[8*i + 2] = (byte)((bin[i]&0x20) >>> 5);
			result[8*i + 3] = (byte)((bin[i]&0x10) >>> 4);
			result[8*i + 4] = (byte)((bin[i]&0x08) >>> 3);
			result[8*i + 5] = (byte)((bin[i]&0x04) >>> 2);
			result[8*i + 6] = (byte)((bin[i]&0x02) >>> 1);
			result[8*i + 7] = (byte)((bin[i]&0x01));
		}
		
		return result;
	}
	public static byte[] ascBytes2BinBytes(byte[] asc) {
		
		byte[] result = new byte[asc.length/8];
		
		int a0,a1,a2,a3,a4,a5,a6,a7;

		for(int i=0;i < asc.length/8;i++) {
			
			a0 = asc[8*i];
			a1 = asc[8*i + 1];
			a2 = asc[8*i + 2];
			a3 = asc[8*i + 3];
			a4 = asc[8*i + 4];
			a5 = asc[8*i + 5];
			a6 = asc[8*i + 6];
			a7 = asc[8*i + 7];

			result[i] = (byte)((a0<<7) + (a1<<6) + (a2<<5) + (a3<<4) + (a4<<3) + (a5<<2) + (a6<<1) + a7);
		}
		
		return result;
	}
	
	/**
	 * ���룺95BE5779 04DC9CF7�������39 35 42 45 35 37 37 39  30 34 44 43 39 43 46 37
	 */
	public static byte[] bcdBytes2AscBytes(byte[] bcd) {

		byte[] result = new byte[bcd.length*2]; 

		for(int i = 0;i < bcd.length;i++) {

			byte h = (byte)((bcd[i]&0xf0) >>> 4);
			byte l = (byte)(bcd[i]&0x0f);

			result[2*i] = (byte)byte2Char(h);
			result[2*i + 1] = (byte)byte2Char(l);
		}

		return result;
	}
	

	
	public static int bcdBytes2Int(byte[] bcd) {
		
		return Integer.valueOf(ToolKit.bcdBytes2Str(bcd),16);
	}
	public static String getBcdString(byte[] src,int srcPos,int length) {
		return getBcdString(src,srcPos,0,length,false,false);
	}
	public static String getBcdString(byte[] src,int srcPos,int destPos,int length,boolean cut,boolean cutLeft) {
		
		byte[] temp = new byte[length];
		System.arraycopy(src,srcPos,temp,destPos,length);
		return bcdBytes2Str(temp,cut,cutLeft);
	}


	public static String getAscString(byte[] src,int srcPos,int length) {
		return getAscString(src,srcPos,0,length,false,false);
	}
	public static String getAscString(byte[] src,int srcPos,int destPos,int length,boolean cut,boolean cutLeft) {
		
		byte[] temp = new byte[length];
		System.arraycopy(src,srcPos,temp,destPos,length);
		String result = "";
		try {
			result = new String(temp,"ASCII");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String getString(byte[] src,int srcPos,int length) {
		byte[] temp = new byte[length];
		System.arraycopy(src,srcPos,temp,0,length);
		return new String(temp);
	}
	
	public static String round(String amt) {
		
		Double amount = Double.parseDouble(amt);
		
		int a = (int)Math.round(amount*100);
		
		double b = (double)a/100.00;
		if(b <= 0.0) b = 0.01;
		
		return String.valueOf(b);
	}
    public static String getElementValue(String elemName, Document doc) {
        String elemValue = "";
        if (null != doc) {
            Element elem = null;
            elem = (Element) doc.getElementsByTagName(elemName).item(0);
            if (null != elem && null != elem.getFirstChild()) {
                elemValue = elem.getFirstChild().getNodeValue();
            }
        }
        return elemValue;
    }
    
    //是否乱码
  	public static boolean isMessyCode(String str) {
  		String temp = str;
  		try {
  			temp = new String(temp.getBytes(), "UTF-8");
  		} catch (UnsupportedEncodingException e) {
  			temp = str;
  		}
  		for (int i = 0; i < temp.length(); i++) {
  			char c = temp.charAt(i);
  			// 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
  			// 从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
  			if ((int) c == 0xfffd || (int)c == 0x3f) {
  				return true;
  			}
  		}
  		return false;
  	}

      public static String format(String str, int beginSize, int endSize, String leftFill, String rightFill) throws Exception {

          return format(str, beginSize, endSize, leftFill, rightFill, false);
      }

      public static boolean isValidIdCard(String idNo) {
          if (idNo == null) {
              return false;
          }
          for (int i = 0; i < idNo.length(); i++) {
              char c = idNo.charAt(i);
              if (c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9' && c != '0' && c != 'x' && c != 'X') {
                  return false;
              }

          }
          return true;
      }

      public static String format(String str, int beginSize, int endSize, String leftFill, String rightFill, boolean cutLeft) throws Exception {

          while (beginSize > 0) {
              str = leftFill + str;
              beginSize--;
          }
          if (str.getBytes("gbk").length > endSize) {

              byte[] temp = str.getBytes();
              byte[] newbyte = new byte[endSize];
              if (cutLeft) {
                  for (int i = newbyte.length - 1, j = temp.length - 1; i >= 0; i--, j--) {
                      newbyte[i] = temp[j];
                  }
              } else {
                  for (int i = 0; i < newbyte.length; i++) {
                      newbyte[i] = temp[i];
                  }
              }
              str = new String(newbyte);
          }

          while (str.getBytes("gbk").length < endSize) {
              str += rightFill;
          }
          return str;
      }

      public static String format(String str, int endSize) {
          try {
              return format(str, 0, endSize, " ", " ");
          } catch (Exception e) {
              return "格式化字符串出现异常:" + e;
          }
      }

      public static String getStackTrace(Throwable e) {
          StringBuffer stack = new StringBuffer();
          stack.append(e);
          stack.append("\r\n");

          Throwable rootCause = e.getCause();

          while (rootCause != null) {
              stack.append("Root Cause:\r\n");
              stack.append(rootCause);
              stack.append("\r\n");
              stack.append(rootCause.getMessage());
              stack.append("\r\n");
              stack.append("StackTrace:\r\n");
              stack.append(rootCause);
              stack.append("\r\n");
              rootCause = rootCause.getCause();
          }


          for (int i = 0; i < e.getStackTrace().length; i++) {
              stack.append(e.getStackTrace()[i].toString());
              stack.append("\r\n");
          }
          return stack.toString();
      }

      public static String padLeft(String input, char c, int length) {
          String output = input;
          while (output.length() < length) {
              output = c + output;
          }
          return output;
      }

      public static String padRight(String input, char c, int length) {
          String output = input;
          while (output.length() < length) {
              output = output + c;
          }
          return output;
      }

      public static String padRight(String input, int length) {
          return padRight(input, ' ', length);
      }

      public static String padLeft(String input, int length) {
          return padLeft(input, '0', length);
      }

      public static String bytePadLeft(String input, char c, int length) {
          String output = input;
          while (output.getBytes().length < length) {
              output = c + output;
          }
          return output;
      }

      public static String bytePadRight(String input, char c, int length) {
          String output = input;
          while (output.getBytes().length < length) {
              output = output + c;
          }
          return output;
      }

      public static String formatNumber(double in, int sf) {

          DecimalFormat formater = new DecimalFormat();
          String pattern = "#########";

          if (sf > 0) {
              pattern = pattern + ".#";
          }

          for (int i = 1; i < sf; i++) {
              pattern = pattern + "#";
          }

          formater.applyPattern(pattern);

          String tmp = formater.format(in);

          if (sf > 0) {
              if (tmp.indexOf(".") == -1) {
                  tmp = tmp + ".";
              }

              int zeros = (sf + 1) - (tmp.length() - tmp.indexOf("."));

              for (int i = 0; i < zeros; i++) {
                  tmp += "0";
              }
          }

          return tmp;
      }

      public static String formatPassword(String pan, int showStartLen, int showEndLen) {

          String temp = "";
          for (int i = 0; i < pan.length(); i++) {
              if (i < showStartLen || i >= pan.length() - showEndLen) {
                  temp = temp + pan.charAt(i);
              } else {
                  temp = temp + "*";
              }
          }
          return temp;

      }

      public static boolean isNullOrEmpty(String str) {
          return str == null || str.trim().equals("");
      }

      public static boolean isNullOrEmpty(Object str) {
          return str == null || str.toString().equals("");
      }

      public static String toXmlString(Object o, String append) {
          StringBuilder sb = new StringBuilder(append);
          sb.append("<" + o.getClass().getName() + ">" + append);
          Object[] agrs = new Object[]{};
          for (Method m : o.getClass().getDeclaredMethods()) {
              if (m.getDeclaringClass().getName().equals(o.getClass().getName()) && m.getName().startsWith("get")) {
                  sb.append("  <" + m.getName().substring(3) + ">");
                  try {
                      Object obj = m.invoke(o, agrs);
                      sb.append(obj == null ? "" : obj);
                  } catch (Exception e) {
                      sb.append(toXmlString(e, ""));
                  }
                  sb.append("</" + m.getName().substring(3) + ">" + append);

              }
          }

          sb.append("</" + o.getClass().getName() + ">" + append);
          return sb.toString();
      }

      public static String toString(Object o) {
          if (o == null) {
              return "";
          }
          return o.toString().trim();
      }

      public static String toXmlString(Object o) {
          if (o == null) {
              return "<object></object>";
          }
          return toXmlString(o, "\n");
      }
      private static int num = 0;

      public synchronized static String getNextNum4() {

          if (num > 9999) {
              num = 0;
          }

          String back = num + "";

          while (back.length() < 4) {
              back = "0" + back;
          }

          num++;

          return back;
      }

      public static String toHexString(byte[] b) {
          StringBuilder sb = new StringBuilder(b.length * 2);
          for (int i = 0; i < b.length; i++) {
              sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
              sb.append(HEXCHAR[b[i] & 0x0f]);
          }
          return sb.toString();
      }

      public static byte[] toBytes(String s) {
          byte[] bytes;
          bytes = new byte[s.length() / 2];
          for (int i = 0; i < bytes.length; i++) {
              bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                      16);
          }
          return bytes;
      }
      
      public static String random(int len) {
          String str = "";
          java.util.Random rander = new java.util.Random(System.currentTimeMillis());
          for(int i=0; i<len; i++) {
              str+= HEXCHAR[rander.nextInt(16)];
          }
          return str;
      }
      
      private static char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6', '7',
          '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  	
  	public static byte[] bcdBytes2Bytes(byte[] bcd) {

  		if (bcd.length % 2 != 0)
  			return null;

  		byte[] temp = bcd;
  		byte[] result = new byte[temp.length / 2];

  		for (int i = 0; i < result.length; i++) {

  			byte h = byte2OByte(temp[2 * i]);
  			byte l = byte2OByte(temp[2 * i + 1]);

  			result[i] = (byte) ((h << 4) + l);
  		}
  		return result;
  	}
  	
  	public static String getGB2312String(byte[] src, int srcPos, int length) throws Exception{

  		byte[] temp = new byte[length];
  		System.arraycopy(src, srcPos, temp, 0, length);
  		return new String(temp, "GB2312");
  	}

  	public static byte[] hexStringToByte(String hex) {
  		int len = (hex.length() / 2);
  		byte[] result = new byte[len];
  		char[] achar = hex.toCharArray();
  		for (int i = 0; i < len; i++) {
  			int pos = i * 2;
  			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
  		}
  		return result;
  	}

  	private static byte toByte(char c) {
  		byte b = (byte) "0123456789ABCDEF".indexOf(c);
  		return b;
  	}

  	public static final String bytesToHexString(byte[] bArray) {
  		StringBuffer sb = new StringBuffer(bArray.length);
  		String sTemp;
  		for (int i = 0; i < bArray.length; i++) {
  			sTemp = Integer.toHexString(0xFF & bArray[i]);
  			if (sTemp.length() < 2)
  				sb.append(0);
  			sb.append(sTemp.toUpperCase());
  		}
  		return sb.toString();
  	}
  	
  	public static String base64Encode(byte[] src){
  		try {
  			if(src == null) return "";
  	  		return new sun.misc.BASE64Encoder().encode(src);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
  	
  	public static byte[] base64Decode(String src){
  		try {
  			if(isNullOrEmpty(src)) return null;
  			return new sun.misc.BASE64Decoder().decodeBuffer(src);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
