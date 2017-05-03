package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.annotation.Transactional;
import play.db.ebean.Model;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/3/1.
 */
@Entity
@Table(name = "M_Authority")
public class Authority extends Model {
    @Id
    public String id;
    /**
     * 角色名称
     */
    public String name;
    /**
     * action链接
     */
    public String action;
    /**
     * code
     */
    public String code;
    /**
     * 是否删除 0：否，1：是
     */
    public String del;
    /**
     * 更新人
     */
    public String updateuser;
    /**
     * 更新时间
     */
    public Date updatetime;
    /**
     * 站点id
     */
    public String station_id;

    /**
     * 根据id获取对象
     *
     * @param
     * @return
     */
    @Transactional
    public static Authority getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Authority.class).where().idEq(id).findUnique();
    }

    /**
     * 条件查询
     *
     * @param
     * @return
     */
    @Transactional
    public static List<Authority> selectList(Map<String, Object> map,String dataSourceType) {
        if (ObjectUtil.isNotNull(map)) {
            return Ebean.getServer(dataSourceType).find(Authority.class).where().allEq(map).findList();
        }
        return Ebean.getServer(dataSourceType).find(Authority.class).findList();
    }

    /**
     * 新增
     *
     * @param
     * @return
     */
    @Transactional
    public static Authority insert(Authority authority,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(authority);
        return authority;
    }

    /**
     * 更新
     *
     * @param
     * @return
     */
    @Transactional
    public static Authority update(Authority authority,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(authority);
        return authority;
    }

    /**
     * 将树转换为json数组格式
     */
    public static String jsonTree(Map<String, Object> map,String dataSourceType) {
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
    public static String jsonTreeRecursion(Map<String, Object> map,String dataSourceType) {
        List<SqlRow> sqlRowlist = selectSqlRowList(map,dataSourceType);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Object role_id = map.get("role_id");
        map.remove("role_id");
        for (int i = 0; i < sqlRowlist.size(); i++) {
            SqlRow sqlRow = sqlRowlist.get(i);
            sb.append("{\"id\":\"" + sqlRow.get("id") + "\",\"text\":\"" + sqlRow.get("name") + "\"");
            //设置当前节点状态
            sb.append(setState(role_id, sqlRow.get("id"),dataSourceType));
            map.put("code", sqlRow.get("code") + "___");
            //查询当前节点字节点
            List<SqlRow> childList = selectSqlRowList(map,dataSourceType);
            //若有字节点，则添加子节点数据
            if (childList.size() > 0) {
                sb.append(",\"children\":");
                map.put("role_id", role_id);
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
     * @param role_id
     * @param authority_id
     * @return
     */
    public static String setState(Object role_id, Object authority_id,String dataSourceType) {
        StringBuilder sb = new StringBuilder();
        //设置展开状态
        sb.append(",\"state\":{\"opened\":\"true\"");
        Map<String, String> map = new HashMap<>();
        map.put("role_id", (String) role_id);
        map.put("authority_id", (String) authority_id);
        //查询当前角色是否有当前权限
        List<SqlRow> list = RoleAuth.selectList(map,dataSourceType);
        //若有则设置选中状态selected：true
        if (list.size() > 0) {
            sb.append(",\"selected\": true");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 条件查询权限列表
     *
     * @param
     * @return
     */
    public static List<SqlRow> selectSqlRowList(Map<String, Object> map,String dataSourceType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from M_Authority where 1=1");
        if (ObjectUtil.isNotNull(map)) {
            if (ObjectUtil.isNotNull(map.get("del"))) {
                sql.append(" and del=" + map.get("del"));
            }
            if (ObjectUtil.isNotNull(map.get("code"))) {

                sql.append(" and code like '" + map.get("code") + "'");
            }
        }
        sql.append(" order by code");
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        return sqlQuery.findList();
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
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

    @Override
    public String toString() {
        return "Authority{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", action='" + action + '\'' +
                ", code='" + code + '\'' +
                ", del='" + del + '\'' +
                ", updateuser='" + updateuser + '\'' +
                ", updatetime=" + updatetime +
                ", station_id='" + station_id + '\'' +
                '}';
    }
}
