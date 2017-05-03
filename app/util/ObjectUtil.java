package util;

import java.util.List;
/**
 *
 * CopyRright (c)2007-2013: 陕西北佳信息技术有限责任公司<br>
 * Project:互联网项目管理系统<br>                                  
 * Class Type:抽象类<br>
 * Comments:描述内容<br>
 * JDK version:1.7<br>
 * Namespace:com.dl<br>
 * extends：<br>
 * implements：<br>
 *--------------------------------------------------------------<br>
 * V1.0 创建  houyaohui 2016-1-29  新项目<br>
 *--------------------------------------------------------------<br>
 */
public class ObjectUtil {
	private static final int BUFFERED_SIZE = 4096;

	public static boolean isNull(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String)
			return "".equals(o.toString());
		if (o instanceof List) {
			return (((List) o).size() == 0);
		}
		return false;
	}

	public static boolean isNotNull(Object o) {
		return (!(isNull(o)));
	}


}