package com.magispec.shield.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.magispec.shield.service.BaseApplicaton;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil {
	/**
	 * SP工具类
	 */
	
	/**
	 * 根据key查找value时，value值类型为String时，传入返回值类型用这个.
	 */
	public final static int VALUE_IS_STRING = 0;
	/**
	 * 根据key查找value时，value值类型为boolean时，传入返回值类型用这个.
	 */
	public final static int VALUE_IS_BOOLEAN = 1;
	/**
	 * 根据key查找value时，value值类型为int时，传入返回值类型用这个.
	 */
	public final static int VALUE_IS_INT = 2;
	/**
	 * 根据key查找value时，value值类型为float时，传入返回值类型用这个.
	 */
	public final static int VALUE_IS_FLOAT = 3;
	/**
	 * 根据key查找value时，value值类型为long时，传入返回值类型用这个.
	 */
	public final static int VALUE_IS_LONG = 4;

	/**
	 * 向SharePreference中保存或修改单个属性
	 * 
	 * @param fileName
	 *            要保存到的sharePreference文件名称
	 * @param key
	 *            key值
	 * @param value
	 *            value值,只能为String,int,float,long,boolean五种类型,由Editor保存类型决定
	 */
	public static void saveOrUpdateAttribute(Context context, String fileName, String key, Object value) {
		// 获取sharePreference,默认权限为Context.MODE_APPEND
		SharedPreferences sp = getSP(context, fileName, Context.MODE_APPEND);
		Editor editor = sp.edit();
		saveOrUpdateValue(editor, key, value);
		editor.commit();
	}

	/**
	 * 向sharepreference文件中保存或修改多个属性 注意：map中保存的value值需要都为String类型数据
	 * 
	 * @param fileName
	 *            要保存到的sharepreference文件的名称
	 * @param valuesMap
	 *            要保存的内容的k-v对。value只能为String,int,float,login,boolean五种类型,
	 *            由Editor保存类型决定
	 */
	@SuppressWarnings("unchecked")
	public static void saveOrUpdateAttributes(Context context, String fileName, Map<String, ?> valuesMap) {
		// 获取sharePreference,默认权限为Context.MODE_APPEND
		SharedPreferences sp = getSP(context, fileName, Context.MODE_APPEND);
		Editor editor = sp.edit();
		Iterator<?> iterator = valuesMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			saveOrUpdateValue(editor, key, value);
		}
		editor.commit();
	}
	/**
	 * 获取sharePreference对象
	 * 
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            权限方式
	 * @return
	 */
	private static SharedPreferences getSP(Context context, String fileName, int mode) {
		try {
			SharedPreferences sp = BaseApplicaton.getInstance().getBaseContext().getSharedPreferences(fileName, mode);
			return sp;
		} catch (Exception e) {
			//Logger.e("context" + context + "  fileName=" + fileName + " mode=" + mode);
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 向SharePreference中保存数据
	 * 
	 * @param editor
	 *            sharePreference编缉器
	 * @param key
	 *            要保存的属性的key值
	 * @param value
	 *            要保存的属性的value 值，为Object类型，在保存时要判断其真正类型.
	 */
	private static void saveOrUpdateValue(Editor editor, String key, Object value) {
		if (null == value) {
			editor.remove(key);// 如果值为空，清除原来的数据即可
		} else {
			if (value instanceof String) {
				editor.putString(key, (String) value);
			} else if (value instanceof Boolean) {
				editor.putBoolean(key, (Boolean) value);
			} else if (value instanceof Integer) {
				editor.putInt(key, (Integer) value);
			} else if (value instanceof Float) {
				editor.putFloat(key, (Float) value);
			} else if (value instanceof Long) {
				editor.putLong(key, (Long) value);
			}
		}
	}

	/**
	 * 取出指定sharePreference中的所有的值
	 * 
	 * @param fileName
	 *            sharepreference文件名称
	 * @return map对象，注意value值的类型,保存了指定sharePreference文件中所有的k-v对
	 */
	public static Map<String, ?> getAll(Context context, String fileName) {
		SharedPreferences sp = getSP(context, fileName, Context.MODE_APPEND);
		Map<String, ?> valuesMap = sp.getAll();
		return valuesMap;
	}

	/**
	 * 根据键来在相应sharePreference文件中查找其value值
	 * 
	 * @param fileName
	 *            被查找的sharePreference文件的名称
	 * @param key
	 *            属性key值
	 * @param valueType
	 *            被查找属性的value值的类型，共五种。
	 * @return
	 */
	public static Object getAttributeByKey(Context context, String fileName, String key, int valueType) {
		SharedPreferences sp = getSP(context, fileName, Context.MODE_APPEND);
		Object value = null;
		switch (valueType) {
		case VALUE_IS_STRING:
			value = sp.getString(key, "");
			break;
		case VALUE_IS_BOOLEAN:
			value = sp.getBoolean(key, false);
			break;
		case VALUE_IS_INT:
			value = sp.getInt(key, 0);
			break;
		case VALUE_IS_FLOAT:
			value = sp.getFloat(key, 0);
			break;
		case VALUE_IS_LONG:
			value = sp.getLong(key, 0);
			break;
		}
		return value;
	}

	/**
	 * 删除指定sharePreference文件中指定的的某个属性的值
	 * 
	 * @param fileName
	 *            sharePreference文件名称
	 * @param key
	 *            文件中属性的key值。
	 */
	public static void deleteAttributeByKey(Context context, String fileName, String key) {
		SharedPreferences sp = getSP(context, fileName, Context.MODE_APPEND);
		Editor editor = sp.edit();
		editor = editor.remove(key);
		editor.commit();
	}

	/**
	 * 清除指定sharePrference文件的内容
	 * 
	 * @param fileNames
	 *            要清楚的sharepreference文件名称。
	 */
	public static void clearSharePreference(Context context, String... fileNames) {
		SharedPreferences sp = null;
		for (String fileName : fileNames) {
			sp = getSP(context, fileName, Context.MODE_APPEND);
			Editor editor = sp.edit();
			editor.clear();
			editor.commit();
		}
	}
}
