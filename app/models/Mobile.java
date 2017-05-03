package models;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by houyaohui on 2016/1/13.
 */
@Entity
@Table(name = "C_Mobile")
public class Mobile extends Model {
    @Id
    public String id;
    public String type_id;
    public String name;
    public String path;
    public String memo;
    public String del;
    public String updateuser;
    public Date updatetime;
    public String station_id;

    /*添加*/
    @Transactional
    public static Mobile insert(Mobile mobile,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(mobile);
        return mobile;
    }

    /*修改*/
    @Transactional
    public static Mobile update(Mobile mobile,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(mobile);
        return mobile;
    }

    /*删除*/
    @Transactional
    public static Mobile delete(Mobile mobile,String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(Mobile.class, mobile.id);
        return mobile;
    }

    @Transactional
    public static List<Mobile> selectList(Map<String, Object> map,String dataSourceType) {
        List<Mobile> list = Ebean.getServer(dataSourceType).find(Mobile.class).where().allEq(map).findList();
        return list;
    }

    @Transactional
    public static List<SqlRow> selectListByMap(Map<String, String> searchMap,String dataSourceType) {
        StringBuilder sql = new StringBuilder("select c.* from C_Mobile c where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (searchMap != null) {
            //删除
            if (searchMap.get("del") != null) {
                sql.append(" and c.del=? ");
                args.add(searchMap.get("del"));
            } else {
                sql.append(" and c.del='0' ");
            }
            if (searchMap.get("type_id") != null) {
                sql.append(" and c.type_id=? ");
                args.add(searchMap.get("type_id"));
            }
            //排序
            if (searchMap.get("orderBy") != null) {
                sql.append(searchMap.get("orderBy"));
            } else {
                sql.append(" order by updatetime asc");
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

    @Transactional
    public static List<Mobile> selectByTypeId(String type_id,String dataSourceType) {
        List<Mobile> list = Ebean.getServer(dataSourceType).find(Mobile.class).where().eq("type_id", type_id).findList();
        return list;
    }

    @Transactional
    public static Mobile getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Mobile.class).where().idEq(id).findUnique();
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
}
