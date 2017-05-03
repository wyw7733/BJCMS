package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.WrappingTemplateModel;

/**
 * CopyRright (c)2007-2013: 陕西北佳信息技术有限责任公司<br>
 * Project:互联网项目管理系统<br>
 * Class Type:工具类<br>
 * Comments:SQL自定义标签<br>
 * JDK version:1.7<br>
 * Namespace:com.dl<br>
 * extends：<br>
 * implements：<br>
 * --------------------------------------------------------------<br>
 * V1.0 创建 houyaohui 2016-1-5 新项目<br>
 * --------------------------------------------------------------<br>
 */
public class SqlTag implements TemplateDirectiveModel {

    @SuppressWarnings("deprecation")
    @Override
    public void execute(Environment env, Map map, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        String dataSourceType = env.getDataModel().get("defaultServer").toString();
        Object obj = map.get("sql");
        List<SqlRow> defineList = null;
        if(Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            String sql = obj.toString();
            String sql1 = sql.substring(0, sql.lastIndexOf("limit"));
            String str = sql.substring(sql.lastIndexOf("limit"));
            String str1 = str.substring(str.lastIndexOf("limit ")+6);
            String[] strs = str1.split(",");
            StringBuffer sqlPage = new StringBuffer();
            sqlPage.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
            sqlPage.append(sql1);
            sqlPage.append(") tmp_tb where ROWNUM<=");
            sqlPage.append(strs[1]);
            sqlPage.append(") where row_id>");
            sqlPage.append(strs[0]);
            if (obj == null) {
                throw new RuntimeException("参数有误，必须传入sql语句");
            }
            defineList = getList(sqlPage.toString(),dataSourceType);
        }
        if(Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            if (obj == null) {
                throw new RuntimeException("参数有误，必须传入sql语句");
            }
            defineList = getList(obj.toString(),dataSourceType);
        }


            if (body != null) {
                loopVars[0] = WrappingTemplateModel.getDefaultObjectWrapper().wrap(
                        defineList);
                // 执行标签内容(same as <#nested> in FTL).
                body.render(env.getOut());
            } else {
                throw new RuntimeException("错误的标签体");
            }
    }

    private List<SqlRow> getList(String sql,String dataSourceType) {
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql);
        List<SqlRow> list = sqlQuery.findList();
        return list;
    }
}
