/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;

public final class BeansUtil extends BeanUtils {
	static {
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);

		ConvertUtils.register(new SqlTimestampConverter(null), Timestamp.class);
	}

	public static void copyProperties(Object target, Object source)
			throws InvocationTargetException, IllegalAccessException {
		refreshObject(target, source);
	}

	public static Object refreshObject(Object oldObject, Object newObject) {
		Field[] field = newObject.getClass().getDeclaredFields();

		for (int i = 0; i < field.length; ++i) {
			String name = field[i].getName();

			name = name.substring(0, 1).toUpperCase() + name.substring(1);

			String type = field[i].getGenericType().toString();

			Method m = null;
			try {
				m = newObject.getClass().getMethod("get" + name);
				if (!(ObjectUtil.isNull(m)))
					if ("class java.lang.String".equals(type)) {
						String value = (String) m.invoke(newObject
						);
						if (value != null) {
							m = oldObject.getClass().getMethod("set" + name,
									field[i].getType());
							if (!(ObjectUtil.isNull(m)))
								m.invoke(oldObject, value);
						}
					} else if ("class java.lang.Double".equals(type)) {
						Double value = (Double) m.invoke(newObject
						);

						m = oldObject.getClass().getMethod("set" + name,
								field[i].getType());
						if (!(ObjectUtil.isNull(m)))
							if (value != null)
								m.invoke(oldObject, value);
							else
								m.invoke(oldObject,
										Double.valueOf(0.0D));
					} else if ("class java.lang.Integer".equals(type)) {
						Integer value = (Integer) m.invoke(newObject
						);

						m = oldObject.getClass().getMethod("set" + name,
								field[i].getType());
						if (!(ObjectUtil.isNull(m)))
							if (value != null)
								m.invoke(oldObject, value);
							else
								m.invoke(oldObject,
										Integer.valueOf(0));
					} else {
						Object object = m.invoke(newObject);
						if (object != null) {
							m = oldObject.getClass().getMethod("set" + name,
									field[i].getType());
							if (!(ObjectUtil.isNull(m)))
								m.invoke(oldObject, object);
						}
					}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return oldObject;
	}
}