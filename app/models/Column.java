package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.h2.result.RowList;
import play.data.Form;
import play.db.ebean.Model;
import util.Constant;
import util.FreeMarkerUtil;
import util.ObjectUtil;
import util.UUIDUtil;

import javax.persistence.*;
import java.util.*;

/**
 * Created by zuopanpan on 2016/1/22.
 */
@Entity
@Table(name = "C_Column")
public class Column extends Model {
    @Id
    public String id;

    /**
     * 栏目类型
     */
    public String type_id;
    /**
     * 栏目名称
     */
    public String name;
    /**
     * 栏目缩略图
     */
    public String thumbnail;
    /**
     * 备注
     */
    public String memo;
    /**
     * 栏目模板
     */
    public String mobilecolumn_id;
    /**
     * 内容模板
     */
    public String mobilecontent_id;
    /**
     * 排列顺序
     */
    public String columnorder;
    /**
     * 评论类型 0：游客评论 1：登录评论 2：关闭评论
     */
    public String commenttype;
    /**
     * 显示类型 0：前台 1：后台
     */
    public String showtype;
    /**
     * 审核类型 0：否 1：是
     */
    public String audittype;
    /**
     * 外部链接
     */
    public String url;
    /**
     * 站点Id
     */
    public String station_id;
    /**
     * 创建人
     */
    public String createuser;
    /**
     * 创建时间
     */
    public Date createtime;
    /**
     * 更新人
     */
    public String updateuser;
    /**
     * 更新时间
     */
    public Date updatetime;
    /**
     * 父id
     */
    public String parent;
    /**
     * 删除标识：0 未删除,1删除
     */
    public String del;
    /**
     * code
     */
    public String code;
    /**
     * 部门id
     */
    public String dept_ids;

    /**
     * 查询
     *
     * @param map
     * @return
     */
    @Transactional
    public static List<Column> selectList(Map<String, Object> map, String dataSourceType) {
        List<Column> list = Ebean.getServer(dataSourceType).find(Column.class).where().allEq(map).findList();
        return list;
    }

    @Transactional
    public static List<SqlRow> selectListByMap(Map<String, String> searchMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("select * from C_Column c where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (searchMap != null) {
            if (searchMap.get("del") != null) {
                sql.append("and c.del=? ");
                args.add(searchMap.get("del"));
            }
            if (searchMap.get("code") != null) {
                sql.append("and c.code like'").append(searchMap.get("code")).append("'");
            }
            if (searchMap.get("codeOne") != null) {
                sql.append("and c.code=? ");
                args.add(searchMap.get("codeOne"));
            }
            if (searchMap.get("dept_id") != null) {
                sql.append("and c.dept_ids like").append("'%").append(searchMap.get("dept_id")).append("%' ");
            }
            if (searchMap.get("orderBy") != null) {
                sql.append(searchMap.get("orderBy"));
            }


        } else {
            throw new RuntimeException("查询参数searchMap不能为null");
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();

        return rowList;
    }

    /**
     * 添加
     *
     * @param column
     * @return Column
     */
    @Transactional
    public static Column insert(Column column, String dataSourceType) {
        //判断父id是否为null
        if (ObjectUtil.isNull(column.parent)) {
            //若为null,则赋值为类型id
            column.parent = column.type_id;
        }
        Ebean.getServer(dataSourceType).save(column);
        //生成栏目和首页
        generateColumn(true, true, dataSourceType);
        return column;
    }

    /**
     * 修改
     *
     * @param column
     * @return Column
     */
    @Transactional
    public static Column upadte(Column column, String dataSourceType) {
        Column oldColumn = getObject(column.id, dataSourceType);
        String parent = column.parent;
        //判断父id是否为null
        if (ObjectUtil.isNull(column.parent)) {
            //若为Null，则赋值为类型id
            column.parent = column.type_id;
        }
        Ebean.getServer(dataSourceType).update(column);
        //生成栏目和首页
        if (!oldColumn.name.equals(column.name)) {
            generateColumn(true, true, dataSourceType);
        } else {
            if (!oldColumn.mobilecolumn_id.equals(column.mobilecolumn_id)) {
                generateSelectColumn(column.id, dataSourceType);
            }
            if (!oldColumn.mobilecontent_id.equals(column.mobilecontent_id)) {
                Content.generateContents(null, column.id, dataSourceType);
            }
        }

        return column;
    }

    /**
     * 删除
     *
     * @param column
     * @return Column
     */
    @Transactional
    public static Column delete(Column column, String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(Column.class, column.id);
        return column;
    }


    /**
     * @param dept_id 查询树列表
     */
    @Transactional
    public static String selectTree(String dept_id, String dataSourceType) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("del", Constant.DEL_NO);
        //查询栏目类别
        List<ColumnType> TypeList = ColumnType.selectList(map, dataSourceType);
        StringBuilder sb = new StringBuilder("[");
        for (ColumnType type : TypeList) {
            sb.append("{\"id\":\"").append(type.id).append("\",");
            sb.append("\"text\":\"").append(type.name).append("\",");
            sb.append("\"parent\":\"").append("#").append("\",\"state\":{\"opened\":\"true\"}},");
        }
        Map<String, String> searchMap = Maps.newHashMap();
        searchMap.put("del", Constant.DEL_NO);
        Dept dept = Dept.getObject(dept_id,dataSourceType);
        //如果非超级部门,则查询当前部门下的栏目
        if (!Constant.ISSUPPER_NO.equals(dept.issupper)) {
            searchMap.put("dept_id", dept_id);
        }
        //查询栏目
        List<SqlRow> columnList = Column.selectListByMap(searchMap, dataSourceType);


        Column column = null;
        Column column1 = null;
        Column column2 = null;
        List<SqlRow> rowList;
        Map<String, Column> map2 = new HashMap<String, Column>();
        String ids = "";
        List<Column> columnList1 = new ArrayList<Column>();
        for (int i = 0; i < columnList.size(); i++) {
            SqlRow sqlRow = columnList.get(i);
            column1 = new Column();
            ids += sqlRow.getString("id") + ",";
            column1.id = sqlRow.getString("id");
            column1.name = sqlRow.getString("name");
            column1.parent = sqlRow.getString("parent");
            columnList1.add(column1);
            String code = sqlRow.getString("code");
            if (code.length() > 3) {
                int size = code.length() / 3;
                for (int j = 1; j < size; j++) {
                    String fatherCode = code.substring(0, j * 3);
                    Map<String, String> map1 = new HashMap<String, String>();
                    map1.put("codeOne", fatherCode);
                    rowList = Column.selectListByMap(map1, dataSourceType);
                    if (rowList.size() > 0) {
                        SqlRow sqlRow1 = rowList.get(0);
                        column2 = new Column();
                        column2.id = sqlRow1.getString("id");
                        column2.name = sqlRow1.getString("name");
                        column2.parent = sqlRow1.getString("parent");
                        map2.put(sqlRow1.getString("id"), column2);
                    }

                }
            }


        }
        Iterator iter = map2.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            if (!ids.contains(key.toString())) {
                columnList1.add((Column) val);
            }
        }


        for (Column column3 : columnList1) {
            sb.append("{\"id\":\"").append(column3.id).append("\",");
            sb.append("\"text\":\"").append(column3.name).append("\",");
            sb.append("\"parent\":\"").append(column3.parent).append("\",\"state\":{\"opened\":\"true\"}},");
        }
        String jsonStr = sb.toString();
        if (!"[".equals(jsonStr)) {
            jsonStr = jsonStr.substring(0, jsonStr.length() - 1) + "]";
        }
        return jsonStr;
    }

    /**
     * Description: 根据id查询对象
     *
     * @param id
     * @return
     */
    @Transactional
    public static Column getObject(String id, String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Column.class).where().idEq(id).findUnique();
    }

    /**
     * 查询最大code
     */
    @Transactional
    private static String searchCodeMax(String sql, String dataSourceType) {
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql);
        SqlRow sqlRow = sqlQuery.findUnique();
        return sqlRow.getString("code");
    }

    /**
     * Description: 生成code
     *
     * @param parentId
     * @param sql
     * @return
     */
    public static String getCodeVal(String parentId, String sql, String dataSourceType) {
        String sqlStr = sql + " and LENGTH(code)=3";
        // 查询父节点数据
        String parentCode = "";
        if (ObjectUtil.isNotNull(parentId) && !"0".equals(parentId)) {
            Column column = getObject(parentId, dataSourceType);
            parentCode = column.code;
            sqlStr = sql + " and code like'" + parentCode + "___'";
        }
        // 获取当前最大code
        String codeMax = searchCodeMax(sqlStr, dataSourceType);
        if (codeMax != null) {
            if ((parentCode != null) && (!("".equals(parentCode)))) {
                String last3 = codeMax.substring(codeMax.length() - 3,
                        codeMax.length());
                String dcode = Integer.parseInt(last3) + 1 + "";
                switch (dcode.length()) {
                    case 1:
                        return parentCode + "00" + dcode;
                    case 2:
                        return parentCode + "0" + dcode;
                }
                return parentCode + dcode;
            }
            String dcode = Integer.parseInt(codeMax) + 1 + "";
            switch (dcode.length()) {
                case 1:
                    return "00" + dcode;
                case 2:
                    return "0" + dcode;
            }
            return dcode;
        }
        if ((parentCode != null) && (!("".equals(parentCode)))) {
            return parentCode + "001";
        }
        return "001";
    }

    /**
     * 生成栏目、详情和首页
     *
     * @param isGenerateTowColumn 是否生成栏目所有二级页面
     * @param isGenerateContent   是否生成文章所有详情页面
     * @return
     */
    public static void generateColumn(boolean isGenerateTowColumn, boolean isGenerateContent, String dataSourceType) {
        //根据选择的模版生成静态页面
        Map<String, String> searchMap = Maps.newHashMap();
        //将结果集保存到map中传给模版工具类
        Map<String, Object> result = Maps.newHashMap();
        //获取域名路径
        Site site = Site.getSite(dataSourceType);
        if (ObjectUtil.isNull(site)) {
            throw new RuntimeException("站点设置不能为空");
        }
        result.put("manage_url", site.manage_url);
        //查询栏目
        searchMap.put("del", Constant.DEL_NO);
        searchMap.put("code", "___");
        searchMap.put("orderBy", " order by columnorder");
        List<SqlRow> columnList = Column.selectListByMap(searchMap, dataSourceType);
        result.put("columnList", columnList);
        //查询配置
        searchMap.put("del", Constant.DEL_NO);
        Config config = Config.getConfig(dataSourceType);
        //查询内容
        List<SqlRow> contentList = Lists.newArrayList();
        if (ObjectUtil.isNotNull(config)) {
            for (SqlRow temp : columnList) {
                Map<String, String> searchContentMap = Maps.newHashMap();
                searchContentMap.put("del", Constant.DEL_NO);
                searchContentMap.put("column_id", temp.getString("id"));
                searchContentMap.put("audit", Constant.AUDIT_PASS);
                searchContentMap.put("orderBy", " order by releasetime desc");
                List<SqlRow> list = Content.selectList(searchContentMap, dataSourceType);
                //获取每个栏目的第一条数据
                if (ObjectUtil.isNotNull(list)) {
                    contentList.add(list.get(0));
                }
                //生成栏目二级页面
                if (isGenerateTowColumn) {
                    //查询模版
                    Mobile mobile = Mobile.getObject(temp.getString("mobilecolumn_id"), dataSourceType);
                    String mobilePath = mobile.path;
                    String begin = mobilePath.substring(0, mobilePath.lastIndexOf("/"));
                    String end = mobilePath.substring(mobilePath.lastIndexOf("/") + 1, mobilePath.length());
                    String savePath = config.htmlpath + "html/" + temp.getString("code") + "/" + temp.getString("code") + ".html";
                    result.put("abspath", "../");
                    //添加栏目对象
                    result.put("column", temp);
                    result.put("defaultServer",dataSourceType);
                    //根据栏目模版生成二级栏目列表
                    try {
                        FreeMarkerUtil.generateHtml(begin, savePath, end, result,dataSourceType);
                    } catch (Exception e) {
                        System.out.print(e.getMessage());
                        BusinessException businessException = new BusinessException();
                        businessException.id = UUIDUtil.getUUID();
                        businessException.name = e.getMessage();
                        businessException.updatetime = new Date();
                        businessException.is_success = "0";
                        BusinessException.insert(businessException,dataSourceType);
                    }

                }
            }
            result.put("contentList", contentList);

            //生成首页
            String path = Mobile.selectByTypeId(Constant.MOBILETYPE_INDEX, dataSourceType).get(0).path;
            String begin = path.substring(0, path.lastIndexOf("/"));
            String end = path.substring(path.lastIndexOf("/") + 1, path.length());
            //根据首页模版生成首页
            result.put("abspath", "html/");
            result.put("defaultServer",dataSourceType);
            try {
                FreeMarkerUtil.generateHtml(begin, config.htmlpath + "/index.html", end, result,dataSourceType);
            } catch (Exception e) {
                System.out.print(e.getMessage());
                BusinessException businessException = new BusinessException();
                businessException.id = UUIDUtil.getUUID();
                businessException.name = e.getMessage();
                businessException.updatetime = new Date();
                businessException.is_success = "0";
                BusinessException.insert(businessException,dataSourceType);
            }
            //生成每个文章
            if (isGenerateContent) {
                //生成文章详情页面
                searchMap.clear();
                searchMap.put("del", Constant.DEL_NO);
                searchMap.put("audit", Constant.AUDIT_PASS);
                contentList = Content.selectList(searchMap, dataSourceType);
                if (ObjectUtil.isNotNull(contentList)) {
                    for (SqlRow row : contentList) {
                        //文章对应模版
                        Mobile mobile = Mobile.getObject(row.getString("mobile_id"), dataSourceType);
                        String mobilePath = mobile.path;
                        begin = mobilePath.substring(0, mobilePath.lastIndexOf("/"));
                        end = mobilePath.substring(mobilePath.lastIndexOf("/") + 1, mobilePath.length());
                        result.put("content", row);
                        result.put("abspath", "../");
                        result.put("root", site.dns);
                        result.put("defaultServer",dataSourceType);
                        try {
                            FreeMarkerUtil.generateHtml(begin, config.htmlpath + "html/" + row.getString("htmlpath"), end, result,dataSourceType);
                        } catch (Exception e) {
                            System.out.print(e.getMessage());
                            BusinessException businessException = new BusinessException();
                            businessException.id = UUIDUtil.getUUID();
                            businessException.name = e.getMessage();
                            businessException.is_success = "0";
                            businessException.updatetime = new Date();
                            BusinessException.insert(businessException,dataSourceType);
                        }
                    }
                }

            }
        }


    }

    /**
     * 根据选择的栏目生成二级页面
     *
     * @param column_id
     */
    public static void generateSelectColumn(String column_id, String dataSourceType) {
        //根据选择的模版生成静态页面
        Map<String, String> searchMap = Maps.newHashMap();
        //将结果集保存到map中传给模版工具类
        Map<String, Object> result = Maps.newHashMap();
        //获取域名路径
        Site site = Site.getSite(dataSourceType);
        if (ObjectUtil.isNull(site)) {
            throw new RuntimeException("站点设置不能为空");
        }
        result.put("manage_url", site.manage_url);
        //查询栏目
        String code = "";
        if (ObjectUtil.isNotNull(column_id)) {
            Column column = Column.getObject(column_id, dataSourceType);
            code = column.code;
        }
        //如果code是空则更新全部，反之则更新该栏目下的栏目二级页面
       /* if (ObjectUtil.isNotNull(code)) {
            searchMap.put("code", code + "___");
        } else {
            searchMap.put("code", "___");
        }*/
        if (ObjectUtil.isNotNull(code)) {
            searchMap.put("codeOne", code);
        }
        searchMap.put("del", Constant.DEL_NO);
        searchMap.put("orderBy", " order by columnorder");
        List<SqlRow> columnList = Column.selectListByMap(searchMap, dataSourceType);
        result.put("columnList", columnList);
        //查询配置
        searchMap.put("del", Constant.DEL_NO);
        Config config = Config.getConfig(dataSourceType);
        for (SqlRow temp : columnList) {
            //查询模版
            Mobile mobile = Mobile.getObject(temp.getString("mobilecolumn_id"), dataSourceType);
            if (ObjectUtil.isNull(mobile)) {
                System.out.print(temp.getString("mobilecolumn_id"));
            }
            String mobilePath = mobile.path;
            String begin = mobilePath.substring(0, mobilePath.lastIndexOf("/"));
            String end = mobilePath.substring(mobilePath.lastIndexOf("/") + 1, mobilePath.length());
            String savePath = config.htmlpath + "/html/" + temp.getString("code") + "/" + temp.getString("code") + ".html";
            //添加栏目对象
            result.put("column", temp);
            result.put("abspath", "../");
            result.put("root", site.dns);
            result.put("defaultServer",dataSourceType);
            //根据栏目模版生成二级栏目列表
            try {
                FreeMarkerUtil.generateHtml(begin, savePath, end, result,dataSourceType);
            } catch (Exception e) {
                System.out.print(e.getMessage());
                BusinessException businessException = new BusinessException();
                businessException.id = UUIDUtil.getUUID();
                businessException.name = e.getMessage();
                businessException.is_success = "0";
                businessException.updatetime = new Date();
                BusinessException.insert(businessException,dataSourceType);
            }


        }
    }

    /**
     * 查询栏目（树形）
     *
     * @param form
     * @return
     */
    public static String selectColumnJsonTree(Form form, boolean ifHasNullValue, String dept_id, String dataSourceType) {
        StringBuilder sb = new StringBuilder("[");
        if (ifHasNullValue) {
            sb.append("{\"id\":\"\",\"text\":\"-请选择栏目-\"},");
        }
        Map<String, String> map = Maps.newHashMap();
        map.put("del", Constant.DEL_NO);
        map.put("code", "___");
        String json = selectJsonTree(map, dataSourceType);
        if (!json.equals("")) {
            sb.append(json);
        } else {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 查询栏目（树形）
     *
     * @param form
     * @return
     */
    public static String selectColumnJsonTreeDep(Form form, boolean ifHasNullValue, String dept_id, String dataSourceType) {
        StringBuilder sb = new StringBuilder("[");
        if (ifHasNullValue) {
            sb.append("{\"id\":\"\",\"text\":\"-请选择栏目-\"},");
        }
        Map<String, String> map = Maps.newHashMap();
        map.put("del", Constant.DEL_NO);
        map.put("code", "___");
        Dept dept = Dept.getObject(dept_id,dataSourceType);
        //如果非超级部门,则查询当前部门下的栏目
        if (!Constant.ISSUPPER_NO.equals(dept.issupper)) {
            map.put("dept_id", dept_id);
        }
        String json = selectJsonTree(map, dataSourceType);
        if (!json.equals("")) {
            sb.append(json);
        } else {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @param map
     * @return
     * @Description: 拼接json
     */
    public static String selectJsonTree(Map<String, String> map, String dataSourceType) {
        StringBuilder sb = new StringBuilder();
        // 根据code查询节点
        List<SqlRow> oneList = Column.selectListByMap(map, dataSourceType);
        for (int i = 0; i < oneList.size(); i++) {
            SqlRow oneMap = oneList.get(i);
            sb.append("{\"id\":\"" + oneMap.get("id") + "\",\"text\":\""
                    + oneMap.get("name") + "\",\"type_id\":\""
                    + oneMap.get("type_id") + "\",\"thumbnail\":\""
                    + oneMap.get("thumbnail") + "\",\"memo\":\""
                    + oneMap.get("memo") + "\",\"mobilecolumn_id\":\""
                    + oneMap.get("mobilecolumn_id") + "\",\"mobilecontent_id\":\""
                    + oneMap.get("mobilecontent_id") + "\",\"columnorder\":\""
                    + oneMap.get("columnorder") + "\",\"commenttype\":\""
                    + oneMap.get("commenttype") + "\",\"showtype\":\""
                    + oneMap.get("showtype") + "\",\"audittype\":\""
                    + oneMap.get("audittype") + "\",\"url\":\""
                    + oneMap.get("url") + "\",\"station_id\":\""
                    + oneMap.get("station_id") + "\",\"createuser\":\""
                    + oneMap.get("createuser") + "\",\"createtime\":\""
                    + oneMap.get("createtime") + "\",\"updateuser\":\""
                    + oneMap.get("updateuser") + "\",\"updatetime\":\""
                    + oneMap.get("updatetime") + "\",\"code\":\""
                    + oneMap.get("code") + "\",\"parent\":\""
                    + oneMap.get("parent") + "\",\"del\":\""
                    + oneMap.get("del") + "\"");
            // 判断该节点下是否存在子级
            map.put("code", oneMap.get("code") + "___");
            List<SqlRow> childList = Column.selectListByMap(map, dataSourceType);
            if (childList.size() > 0) {
                sb.append(",\"children\":[");
                sb.append(selectJsonTree(map, dataSourceType));
                sb.append("]");
            }
            sb.append("},");
        }
        if (oneList.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMobilecolumn_id() {
        return mobilecolumn_id;
    }

    public void setMobilecolumn_id(String mobilecolumn_id) {
        this.mobilecolumn_id = mobilecolumn_id;
    }

    public String getMobilecontent_id() {
        return mobilecontent_id;
    }

    public void setMobilecontent_id(String mobilecontent_id) {
        this.mobilecontent_id = mobilecontent_id;
    }

    public String getColumnorder() {
        return columnorder;
    }

    public void setColumnorder(String columnorder) {
        this.columnorder = columnorder;
    }

    public String getCommenttype() {
        return commenttype;
    }

    public void setCommenttype(String commenttype) {
        this.commenttype = commenttype;
    }

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public String getAudittype() {
        return audittype;
    }

    public void setAudittype(String audittype) {
        this.audittype = audittype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDept_ids() {
        return dept_ids;
    }

    public void setDept_ids(String dept_ids) {
        this.dept_ids = dept_ids;
    }


}
