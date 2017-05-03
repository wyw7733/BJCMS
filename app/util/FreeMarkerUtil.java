package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.common.collect.Lists;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import models.Config;

/**
 * CopyRright (c)2007-2013: 陕西北佳信息技术有限责任公司<br>
 * Project:互联网项目管理系统<br>
 * Class Type:工具类<br>
 * Comments:freeMarker工具类<br>
 * JDK version:1.7<br>
 * Namespace:com.dl<br>
 * extends：<br>
 * implements：<br>
 * --------------------------------------------------------------<br>
 * V1.0 创建 houyaohui 2016-1-4 新项目<br>
 * --------------------------------------------------------------<br>
 */
public class FreeMarkerUtil {
    /**
     * 配置对象
     */
    private static Configuration config;

    /**
     * 私有构造方法
     */
    private FreeMarkerUtil() {
    }

    /**
     * Description: 获取config对象
     *
     * @return
     */
    private static Configuration getInstance() {
        if (config == null) {
            // 设置模版仓库
            config = new Configuration(Configuration.VERSION_2_3_23);
            config.setDefaultEncoding("UTF-8");
            //加载自定义标签
            config.setSharedVariable("sql", new SqlTag());
        }
        return config;
    }

    /**
     * Description: 读FTL文件生成HTML文件
     *
     * @param htmlPath 保存路径
     * @param ftl      模版名称
     * @param obj      数据对象
     */
    public static void generateHtml(String templatePath, String htmlPath, String ftl, Object obj,String dataSourceType) throws IOException {

        // 获取配置
        Configuration config = FreeMarkerUtil.getInstance();
            // 设置模版仓库
            //config.setDirectoryForTemplateLoading(new File(templatePath));

            //加载模版仓库
            FileTemplateLoader ftl1 = new FileTemplateLoader(new File(templatePath));
            Config config1 = Config.getConfig(dataSourceType);
            File file = new File(config1.templatepath);
            if (file != null && file.exists()) {
                File[] files = file.listFiles();
                TemplateLoader[] loaders = new TemplateLoader[files.length];
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        FileTemplateLoader ftl2 = new FileTemplateLoader(new File(files[i].getAbsolutePath()));
                        loaders[i] = ftl2;
                    }
                }
                MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
                config.setTemplateLoader(mtl);

                // 读取模版
                Template temp = config.getTemplate(ftl, "UTF-8");
                // 生成模版
                processHtml(htmlPath, obj, temp);
            }
    }

    /**
     * Description: 从数据库中读取模版
     *
     * @param templateStr 模版内容
     * @param obj         对象
     * @return
     */
    public static void generateHtmlDatabase(String path, String templateStr,
                                            Object obj) {
        // 获取配置
        Configuration config = FreeMarkerUtil.getInstance();
        try {
            // 加载模版内容
            config.setTemplateLoader(new FreeMarkerTemplateLoader(templateStr));
            Template template = config.getTemplate("");
            // 生成HTML
            processHtml(path, obj, template);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 解析模版生成HTML
     *
     * @param path
     * @param obj
     * @param template
     */
    private static void processHtml(String path, Object obj, Template template) {
        if (path != null && path.indexOf("/") != -1) {
            // 创建文件夹
            createDir(path);
        }
        OutputStream fop = null;
        Writer out = null;
        // 生成HTML
        try {
            fop = new FileOutputStream(path);
            out = new OutputStreamWriter(fop, "UTF-8");
            template.process(obj, out);
            out.flush();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fop, out);
        }
    }

    /**
     * Description: 关闭流
     *
     * @param fop
     * @param out
     */
    private static void close(OutputStream fop, Writer out) {
        try {
            if (out != null)
                out.close();
            if (fop != null)
                fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 创建文件夹
     *
     * @param path
     */
    private static void createDir(String path) {
        // 截取文件夹路径
        String start = path.substring(0, path.lastIndexOf("/"));
        File file = new File(start);
        // 文件夹不存在，则创建
        if (!file.exists())
            file.mkdirs();
    }
}
