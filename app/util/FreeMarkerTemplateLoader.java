package util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import freemarker.cache.TemplateLoader;

/**
 * 
 * CopyRright (c)2007-2013: 陕西北佳信息技术有限责任公司<br>
 * Project:互联网项目管理系统<br>
 * Class Type:工具类类<br>
 * Comments:模版加载器<br>
 * JDK version:1.7<br>
 * Namespace:com.dl<br>
 * extends：<br>
 * implements：<br>
 * --------------------------------------------------------------<br>
 * V1.0 创建 houyaohui 2016-1-5 新项目<br>
 * --------------------------------------------------------------<br>
 */
public class FreeMarkerTemplateLoader implements TemplateLoader {
	/** 模版内容 */
	private String template;

	/**
	 * 
	 * Description: 重写构造器
	 */
	public FreeMarkerTemplateLoader(String template) {
		if (template == null) {
			this.template = "";
		} else {
			this.template = template;
		}
	}

	@Override
	public Object findTemplateSource(String name) throws IOException {
		return new StringReader(template);
	}

	@Override
	public long getLastModified(Object templateSource) {
		return 0;
	}

	@Override
	public Reader getReader(Object templateSource, String encoding)
			throws IOException {
		return (Reader) templateSource;
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		((StringReader) templateSource).close();
	}
}
