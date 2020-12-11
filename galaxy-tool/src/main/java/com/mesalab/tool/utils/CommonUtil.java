package com.mesalab.tool.utils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;




/**
 * 
 * <p>项目开发中常用公共类.通过类方法进行访问.</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2010-11-8 下午04:50:04
 *
 */
public final class CommonUtil {
	
	private static final int ELEMENT_NOT_FOUND = -1;
	
	/**
	 *<p>Description:抑制默认的构造器,避免实例化对象 </p>
	 */
	 private CommonUtil(){
		 
	 }
	
   /**
	* 
	* <p>去掉List集合中重复的元素.如集合元素是实例对象,
	* 需要重写{@link Object#equals(Object)}与{@link Object#hashCode()}方法.
	* 注意：执行该方法后插入对象顺序将会打乱，执行效率高。如果想保持插入顺序可参看{@link #removeDuplicationWithOrder(List)}
	* </p>
	* <pre>
	* List list &lt;Person&gt;,在去重之前一定要重写Person对象的{@link Object#equals(Object)}与 {@link Object#hashCode()}方法.
	* List list  = new ArrayList(); 
	* list.add(10);
	* list.add(11);
	* list.add(10);
	* removeDuplication(list);
	* system.out.println(list.size());
	* result:2
	* </pre>
	* @param   list list集合
	* @see #removeDuplicationWithOrder(List)
	*/
	@SuppressWarnings("unchecked")
	public static void removeDuplication(List<?> list){
		if(StringUtil.isEmpty(list)){
			throw new NullPointerException("List 集合对象为空");
		}
		Set set = new HashSet(list);
		list.clear();
		list.addAll(set);
	} 
	
   /**
	* 
	* <p>去掉List集合中重复的元素.如集合元素是实例对象,
	* 需要重写{@link Object#equals(Object)}与{@link Object#hashCode()}方法.</p>
	* 注意：该方法将保持list插入元素的顺序，但效率略差。效率高去重可采用{@link #removeDuplication(List)}
	* <pre>
	* List list &lt;Person&gt;,在去重之前一定要重写Person对象的{@link Object#equals(Object)}与 {@link Object#hashCode()}方法.
	* </pre>
	* @param list  list集合
	* @see #removeDuplication(List)
	*/
	@SuppressWarnings("unchecked")
	public static void removeDuplicationWithOrder(List<?> list){
		if(StringUtil.isEmpty(list)){
			throw new NullPointerException("List 集合对象为空");
		}
		Set<Object> set = new HashSet<Object>();   
		List newList = new ArrayList();   
		for (Iterator iter = list.iterator();iter.hasNext(); ) {
			Object element = iter.next();
			if (set.add(element)){  
				newList.add(element);   
			} 
		}
		list.clear();   
		list.addAll(newList); 
	}
	
  /**
	* 
	* <p>精确小数位数，对一个浮点数按照给定的精度进行四舍五入.</p>
	* <pre>
	* double a1 = 1.789812302;
	* CommonUtil.round(a1, 2) = 1.79
	* CommonUtil.round(a1, 5) = 1.78981
	* 
	* float a2 = 1.239f;
	* CommonUtil.round(a1, 2) = 1.24
	* CommonUtil.round(a1, 5) = 1.239
	* </pre>
	* @param value 处理值
	* @param scale 精度(小数点后位数)
	* @return double    返回四舍五入后的数值 
	* @throws IllegalArgumentException
	*/
	public static double round(double value, int scale) {
		return round(value, scale, BigDecimal.ROUND_HALF_UP);
	}


	/**
	 *
	 * <p>精确小数位数，对一个浮点数按照给定的精度取值，不进行四舍五入.</p>
	 * <pre>
	 * double a1 = 1.789812302;
	 * CommonUtil.roundPrecision(a1, 2) = 1.78
	 * CommonUtil.roundPrecision(a1, 5) = 1.78981
	 *
	 * float a2 = 1.239f;
	 * CommonUtil.roundPrecision(a1, 2) = 1.23
	 * CommonUtil.roundPrecision(a1, 5) = 1.239
	 * </pre>
	 * @param value 处理值
	 * @param scale 精度(小数点后位数)
	 * @return double    返回指定精度的数值
	 * @throws IllegalArgumentException
	 */
	public static double roundPrecision(double value, int scale) {
		return round(value, scale, BigDecimal.ROUND_FLOOR);
	}

	/**
	 *
	 * <p>精确小数位数，对一个浮点数按照给定的精度取值，不进行四舍五入.</p>
	 * @param value 处理值
	 * @param scale 精度(小数点后位数)
	 *  @param mode 参见 {@link BigDecimal#ROUND_HALF_UP } 和 {@link BigDecimal#ROUND_FLOOR }用法
	 * @return double    返回指定精度的数值
	 * @throws IllegalArgumentException
	 */
	public static double round(double value, int scale, int mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(value));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, mode).doubleValue();
	}
	
  /**
	* 
	* <p>将数组转存为集合方式,集合的接口为{@link Collection}.
	* 注意数组如果为基本类型,需转换为包装类型.
	* </p>
	* <p>
	* 在转换过程中,如果数组元素为空,将不存入集合中.
	* </p>
	* <pre>
	* String[] aa = {"q","b",""};
	* Integer[] bb = {1,4};
	* List list = new ArrayList();
	* CommonUtil.convertArrayToCollection(aa,list); = {"q","b"}
	* 
	* Set set = new HashSet();
	* CommonUtil.convertArrayToCollection(bb,set); = {1,4}
	* </pre>
	* @param <T> 
	* @param arrayValue 数组对象
	* @param collection   集合对象
	*/
	public static <T> void convertArrayToCollection(T[] arrayValue, Collection<T> collection){
		
		if(StringUtil.isEmpty(arrayValue)){
		   return ;
		}
		
		for (T value : arrayValue) {
			
			if(!StringUtil.isEmpty(value)){
				collection.add(value);
			}
			
		}
	
		
	}
	
	/**
    * 
	* <p>判断元素是否在数组中存在，存在返回<code>true</code>，否在返回<code>false</code></p>
	* <pre>
	* String[] aa = {"aa","nn","dd"};
	* CommonUtil.existInArray(aa, "nn") = true
	* CommonUtil.existInArray(aa, "ss") = false
	* </pre>
	* @param array 数组对象
	* @param element 元素(判断是否存在数组中存在的对象元素)
	* @return <code>true</code> 元素在数组中存在,<code>false</code> 元素在数组中不存在.
	*/
	
	public static  boolean existInArray(Object[] array ,Object element){
		
		
		return indexOf(array,element)!=ELEMENT_NOT_FOUND;
		
	}
	
	/**
	 * 判断元素在数组中是否存在.存在返回非-1值，不存在返回-1.
	 */
	private static int indexOf(Object[] array,Object element){
		
		if(array==null){
			return ELEMENT_NOT_FOUND;
		}
		
		if(element==null){
			for( int i=0; i<array.length; i++ ){
				if(array[i]==null){
					return i;
				}
			}
		}else {
			for( int i=0; i<array.length; i++ ){
				if(element.equals(array[i])){
					return i;
				}
			}
		}
		
		
		return ELEMENT_NOT_FOUND;
		
	}
	
	

	
	
	
	
	
	
}
