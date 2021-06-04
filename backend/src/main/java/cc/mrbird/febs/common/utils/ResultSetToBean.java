package cc.mrbird.febs.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
/**
 *  把返回结果集封装成bean对象
 * @author 安达
 * @date 2020年6月28日
 */
public class ResultSetToBean {
	/**
	 * 把ResultSet的结果放到java对象中
	 * @author 安达
	 * @date 2020年6月29日
	 * @param resultSet
	 * @param objectClass
	 * @return
	 */
	public static <T> ArrayList<T> putResult(ResultSet resultSet, Class<T> objectClass) throws Exception {
		ArrayList<T> arrayList = new ArrayList<T>();
//		try {
			while (resultSet.next()) {
				//通过反射创建 objectClass 对象
				T obj = objectClass.newInstance();
				//objectClass 中所有属性
				Field[] fields = getAllFieldName(objectClass);
				for (Field f : fields) {
					//得到注解
					TableField tableField = f.getAnnotation(TableField.class);
					//如果字段注解不为空
					if(tableField!=null){
						String columnName=tableField.value();
						//取出属性名称
						String fieldName = f.toString().substring(f.toString().lastIndexOf(".")+1);
						Class<?>  type = f.getType();// 获取字段类型
						/**
						 * 根据属性的类型获取返回值类型
						 */
						if (type.isAssignableFrom(String.class)) {
							invokeSet(obj, fieldName, resultSet.getString(columnName));
						} else if (type.isAssignableFrom(byte.class) || type.isAssignableFrom(Byte.class)) {
							invokeSet(obj, fieldName, resultSet.getByte(columnName));
						} else if (type.isAssignableFrom(short.class) || type.isAssignableFrom(Short.class)) {
							invokeSet(obj, fieldName, resultSet.getShort(columnName));
						} else if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
							invokeSet(obj, fieldName, resultSet.getInt(columnName));
						} else if (type.isAssignableFrom(long.class) || type.isAssignableFrom(Long.class)) {
							invokeSet(obj, fieldName, resultSet.getLong(columnName));
						} else if (type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) {
							invokeSet(obj, fieldName, resultSet.getFloat(columnName));
						} else if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)) {
							invokeSet(obj, fieldName, resultSet.getDouble(columnName));
						} else if (type.isAssignableFrom(BigDecimal.class)) {
							invokeSet(obj, fieldName, resultSet.getBigDecimal(columnName));
						} else if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) {
							invokeSet(obj, fieldName, resultSet.getBoolean(columnName));
						} else if (type.isAssignableFrom(Date.class)) {
							invokeSet(obj, fieldName, resultSet.getDate(columnName));
						}else{
							//如果都不属于那些，可能有哪些类型没考虑全面，先返回string类型吧，碰到以后再加上
							invokeSet(obj, fieldName, resultSet.getString(columnName));
						}
					}
				}

				//System.out.println(obj.toString());
				 arrayList.add(obj);
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return arrayList;
	}

	/**
	 * 根据属性名获取get方法
	 * @param objectClass
	 * @param fieldName 属性名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Method getSetMethod(Class objectClass, String fieldName) {
		try {
			Class[] parameterTypes = new Class[1];
			Field field = objectClass.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			//获得set的方法名
			String methodName=getMethodName("set",fieldName);
			//得到set方法
			Method method = objectClass.getMethod(methodName, parameterTypes);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取所有属性
	 * @author 安达
	 * @date 2020年6月29日
	 * @param objectClass
	 * @return Field[]
	 * @return
	 */
	private static Field[] getAllFieldName(Class objectClass) {
		Field[] fields = objectClass.getDeclaredFields();
		for(Field f:fields){
			//如果方法是 private修饰的,当你用反射去访问的时候 setAccessible(true); 之后 才能访问
			f.setAccessible(true);
		}
		return fields;
	}

	/**
	 * 根据属性名执行相应set方法
	 * @param o 执行对象
	 * @param fieldName 属性名
	 * @param value 属性值
	 */
	private static void invokeSet(Object o, String fieldName, Object value) {
		Method method = getSetMethod(o.getClass(), fieldName);
		try {
			method.invoke(o, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *  转换成我们类特有的大写小写规则属性名
	 * @author 安达
	 * @date 2020年6月28日
	 * @param fieldName
	 * @return
	 */
	private static String getMethodName(String type,String fieldName){
		String secondWord=fieldName.substring(1, 2);//获取第二个字段
		//如果第二个字段是大写，第一个字段就要小写
		if(secondWord.matches("[A-Z]")){
			return type+fieldName;
		}else{
			StringBuffer sb = new StringBuffer();
			sb.append(type);
			//在正常情况下，set后面的第一个字母转换成大写
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			return sb.toString();
		}


	}
}