package com.gary.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数组的工具类
 * @author Gary
 *
 */
public class ArrayUtils {
	/**
	 * 字符串化一个数组
	 * @param o Object数组
	 * @param str 分隔符
	 * @return String
	 */
	public static String arrayToString(Object[] o,String str) {
		String s = "";
		if (o.length >= 1) {
			for (int i = 0; i < o.length; i++) {
				s += o[i].toString() + str;
			}
		}
		if (s.length() > 1) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}
	/**
	 * int数组转为int集合
	 * @param o int数组
	 * @return int集合
	 */
	public static List<Integer> intArrayToList(int[] o){
		List<Integer> a = new ArrayList<Integer>();
		for (int v : o) {
			a.add(v);
		}
		return a;
	}
	/**
	 * 集合转为数组
	 * @param o 集合
	 * @return 数组
	 */
	public static Object[] intListToArray(List<?> o){
		Object[] a = new Object[o.size()];
		for (int i = 0; i < o.size(); i++) {
			a[i] = o.get(i);
		}
		return a;
	}
	/**
	 * 删除数组元素以及空字符串元素和空元素
	 * @author Gary
	 * @param a
	 * @param x
	 * @return String[]已经删除的数组
	 */
	public static Object[] deleteElement(Object[] a,Object x){//删除X
		int len=0;
		len=x==null?getArrayEmpty(a):getArrayObjectAndEmpty(a,x);
		Object b[]=new Object[a.length-len];
		if(b.length==0)return b;
		int j=0;
		for (Object i : a) {
			 if(i!=null&&!"".equals(i)){
				 if(!i.equals(x)){
			    	 b[j++]=i;
			     }	
			 }
		}
		return b;
	} 
	/**
	 * 删除数组中重复的元素
	 * @param o Object[]
	 * @return Object[]
	 */
	public static Object[] deleteRepeatArray(Object[] o){
		Set<Object> set=new HashSet<Object>(); 
		for (Object object : o) {
			set.add(object);
		}
		return set.toArray();
	}
	/**
	 * 判断数组里面有几个元素为空字符串和NULL
	 * @author Gary
	 * @param o
	 * @return int个元素
	 */
	public static int getArrayEmpty(Object[] o){
		int i=0;
		for (Object object : o) {
			if("".equals(object)||object==null){
				i++;
			}
		}
		return i;
	}
	
	/**
	 * 判断数组里面有几个元素为空字符串和要删除的元素
	 * @author Gary
	 * @param o
	 * @return int个元素
	 */
	public static int getArrayObjectAndEmpty(Object[] o,Object x){
		int i=0;
		for (Object object : o) {
			if("".equals(object)||object==null||x.equals(object)){
				i++;
			}
		}
		return i;
	}    
	
	/**
	 * 判断obj元素在o数组里面存在不
	 * @param o Object[]
	 * @param obj Object
	 * @return boolean
	 */
	public static boolean existsArray(Object[] o,Object obj){
		for (Object object : o) {
			if(object==null){
				if(object==obj){
					return true;
				}
			}else{
				if(object.equals(obj)){
					return true;
				}
			}
		}
		return false;
	}
	/** 
     * 判断两个数组是否相等 
    * 
    *@param a 
    *@param b 
    *@return 
     */  
    public static boolean arrayIsEqual(Object[] a, Object[] b) {
        if(a!=null&&b!=null){  
            if (a.length != b.length) {  
                return false;  
            } else {  
                for (int i = 0; i < a.length; i++) {  
                    if(a[i]==null){  
                        if(b[i]!=null){  
                            return false;  
                        }  
                    }else{  
                        if (!a[i].equals(b[i])) {  
                            return false;  
                        }  
                    }  
                }  
            }  
            return true;  
        }else {  
            if(a==null&&b==null){  
                return true;  
            }else{  
                return false;  
            }  
              
        }  
    }  
	public static boolean arrayIsEqual(Object[] a, Object[] b,Comparator<Object> comp) {
        if(a!=null&&b!=null){  
            if (a.length != b.length) {  
                return false;  
            } else {  
                for (int i = 0; i < a.length; i++) {  
                    if(a[i]==null){  
                        if(b[i]!=null){  
                            return false;  
                        }  
                    }else{  
                    	return (-1 != comp.compare(a[i],b[i])); 
                    }  
                }  
            }  
            return true;  
        }else {  
            if(a==null&&b==null){  
                return true;  
            }else{  
                return false;  
            }  
              
        }  
    }  
    public static int[] bubbleSort(int[] array, boolean desc){
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = 0; j < array.length - i - 1; j++) {
				if(desc){
					if(array[j] < array[j + 1]){
						int temp = array[j];
						array[j] = array[j + 1];
						array[j + 1] = temp;
					}
				}else{
					if(array[j] > array[j + 1]){
						int temp = array[j];
						array[j] = array[j + 1];
						array[j + 1] = temp;
					}
				}
			}
		}
		return array;
	}
   /** 
    * 判断两个数组内容是否相同 
    *@param a 集合a
    *@param b 集合b
    *@param type 集合比较接口
    *@param comp 对象判断相等接口
    *@return 
     */  
    public static boolean arraySortedIsEqual(List<?> a, List<?> b,Comparator<Object> type,Comparator<Object> comp) { 
        if(a!=null&&b!=null){  
        	Collections.sort(a,type); 
        	Collections.sort(b,type); 
            return arrayIsEqual(a.toArray(), b.toArray(new Object[b.size()]),comp);  
        }else if(a==null&&b==null){  
            return true;  
        }else{  
            return false;  
        }  
          
    }  
    /** 
     * 判断两个数组内容是否相同 
     *@param a 集合a
     *@param b 集合b
     *@return 
      */  
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean arraySortedIsEqual(List a,  List b) { 
        if(a!=null&&b!=null){  
        	Collections.sort(a); 
        	Collections.sort(b); 
            return arrayIsEqual(a.toArray(), b.toArray(new Object[b.size()]));  
        }else if(a==null&&b==null){  
            return true;  
        }else{  
            return false;  
        }  
          
    }  
}
