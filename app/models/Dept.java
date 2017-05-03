package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import play.data.Form;
import play.db.ebean.Model;
import util.Constant;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

/**
 * Created by zuopanpan on 2016/2/17.
 */
@Entity
@Table(name = "M_Dept")
public class Dept extends Model {
    @Id
    public String id;
    /**
     * 部门名称
     */
    public String name;
    /**
     * code
     */
    public String code;
    /**
     * 父节点id
     */
    public String parent;
    /**
     * 更新人
     */
    public String updateuser;
    /**
     * 更新日期
     */
    public Date updatetime;
    /**
     * 站点id
     */
    public String station_id;
    /**
     * 否超级部门：0:是,1:否
     */
    public String issupper;

    /**
     * 条件查询
     *
     * @param
     * @return
     */
    @Transactional
    public static List<SqlRow> selectList(Map<String, String> searchMap,String dataSourceType) {
        StringBuilder sql = new StringBuilder("select d.* from M_Dept d where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("code"))) {
                sql.append("and d.code like'").append(searchMap.get("code")).append("'");
            }
            if (ObjectUtil.isNotNull(searchMap.get("parent"))) {
                sql.append("and d.parent=?");
                args.add(searchMap.get("parent"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("oneCode"))) {
                sql.append("and d.code=?");
                args.add(searchMap.get("oneCode"));
            }
        }
        sql.append("order by d.updatetime");
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
     * @param
     * @return
     */
    @Transactional
    public static Dept insert(Dept dept,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(dept);
        return dept;
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    @Transactional
    public static Dept upadte(Dept dept,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(dept);
        return dept;
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    @Transactional
    public static Dept delete(Dept dept,String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(Dept.class, dept.id);
        return dept;
    }

    /**
     * Description: 根据id查询对象
     *
     * @param id
     * @return
     */
    @Transactional
    public static Dept getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Dept.class).where().idEq(id).findUnique();
    }

    /**
     * 查询最大code
     */
    @Transactional
    private static String searchCodeMax(String sql,String dataSourceType) {
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
    public static String getCodeVal(String parentId, String sql,String dataSourceType) {
        String sqlStr = sql + " and LENGTH(code)=3";
        // 查询父节点数据
        String parentCode = "";
        if (ObjectUtil.isNotNull(parentId) && !"0".equals(parentId)) {
            Dept dept = getObject(parentId,dataSourceType);
            parentCode = dept.code;
            sqlStr = sql + " and code like'" + parentCode + "___'";
        }
        // 获取当前最大code
        String codeMax = searchCodeMax(sqlStr,dataSourceType);
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
     * 查询部门（树形）
     *
     * @param ifHasNullValue
     * @return
     */
    public static String selectDeptJsonTree(boolean ifHasNullValue,String dataSourceType) {
        StringBuilder sb = new StringBuilder("[");
        if (ifHasNullValue) {
            sb.append("{\"id\":\"\",\"text\":\"-请选择-\"},");
        }
        Map<String, String> map = Maps.newHashMap();
        map.put("code", "___");
        String json = selectJsonTree(map,dataSourceType);
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
    public static String selectJsonTree(Map<String, String> map,String dataSourceType) {
        StringBuilder sb = new StringBuilder();
        // 根据code查询节点
        List<SqlRow> oneList = Dept.selectList(map,dataSourceType);
        for (int i = 0; i < oneList.size(); i++) {
            SqlRow oneMap = oneList.get(i);
            sb.append("{\"id\":\"" + oneMap.get("id") + "\",\"text\":\""
                    + oneMap.get("name") + "\",\"code\":\""
                    + oneMap.get("code") + "\",\"parent\":\""
                    + oneMap.get("parent") + "\",\"updateuser\":\""
                    + oneMap.get("updateuser") + "\",\"updatetime\":\""
                    + oneMap.get("updatetime") + "\",\"station_id\":\""
                    + oneMap.get("station_id") + "\"");
            // 判断该节点下是否存在子级
            map.put("code", oneMap.get("code") + "___");
            List<SqlRow> childList = Dept.selectList(map,dataSourceType);
            if (childList.size() > 0) {
                sb.append(",\"children\":[");
                sb.append(selectJsonTree(map,dataSourceType));
                sb.append("]");
            }
            sb.append("},");
        }
        if (oneList.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    /**
     * 将树转换为json数组格式
     */
    public static String jsonTree(Map<String, String> map,String dataSourceType) {
        if (ObjectUtil.isNull(map)) {
            map = new HashMap<>();
        }
        StringBuilder sb = new StringBuilder();
        map.put("code", "___");
        String childJson = jsonTreeRecursion(map,dataSourceType);
        sb.append(childJson);
        return sb.toString();
    }

    /**
     * 递归查询所有节点对象
     *
     * @param map
     * @return
     */
    public static String jsonTreeRecursion(Map<String, String> map,String dataSourceType) {
        List<SqlRow> sqlRowlist = selectList(map,dataSourceType);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        //  map.remove("role_id");
        for (int i = 0; i < sqlRowlist.size(); i++) {
            SqlRow sqlRow = sqlRowlist.get(i);
            sb.append("{\"id\":\"" + sqlRow.get("id") + "\",\"text\":\"" + sqlRow.get("name") + "\"");
            //设置当前节点状态
            sb.append(setState(sqlRow.getString("id"), map.get("dept_ids")));
            map.put("code", sqlRow.get("code") + "___");
            //查询当前节点字节点
            List<SqlRow> childList = selectList(map,dataSourceType);
            //若有字节点，则添加子节点数据
            if (childList.size() > 0) {
                sb.append(",\"children\":");
                sb.append(jsonTreeRecursion(map,dataSourceType));
            }
            sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    /**
     * 设置节点状态（展开、选中）
     *
     * @param dept_id
     * @return
     */
    public static String setState(String dept_id, String dept_ids) {
        StringBuilder sb = new StringBuilder();
        //设置展开状态
        sb.append(",\"state\":{\"opened\":\"true\"");
        if (ObjectUtil.isNotNull(dept_ids)) {
            if (dept_ids.indexOf(dept_id) > -1) {
                sb.append(",\"selected\": true");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getIssupper() {
        return issupper;
    }

    public void setIssupper(String issupper) {
        this.issupper = issupper;
    }
}
