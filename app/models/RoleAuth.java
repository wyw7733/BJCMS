package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.db.ebean.Model;
import util.ObjectUtil;
import util.UUIDUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static play.mvc.Controller.session;

/**
 * Created by zuopanpan on 2016/3/2.
 */
@Entity
@Table(name="M_roleAuth")
public class RoleAuth extends Model {
    @Id
    public String id;
    /** 角色Id*/
    public String role_id;
    /** 权限Id*/
    public String authority_id;
    /** code*/
    public String code;
    /** 更新人*/
    public String updateuser;
    /** 更新时间*/
    public Date updatetime;
    /** 站点id*/
    public String station_id;
    /**
     * 条件查询
     * @param
     * @return
     */
    @Transactional
    public static List<SqlRow> selectList(Map<String,String> map,String dataSourceType) {
        StringBuilder sb = new StringBuilder("select * from M_roleAuth where 1=1");
        List<Object> args = Lists.newArrayList();
        if(ObjectUtil.isNotNull(map)){
            if(ObjectUtil.isNotNull(map.get("role_id"))){
                sb.append(" and role_id=?");
                args.add(map.get("role_id"));
            }
            if(ObjectUtil.isNotNull(map.get("authority_id"))){
                sb.append(" and authority_id=?");
                args.add(map.get("authority_id"));
            }
        }
        sb.append(" order by code");
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sb.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();
        return rowList;
    }
    /**
     * 添加
     * @param
     * @return
     */
    @Transactional
    public static void insert(Map<String,String> map,String dataSourceType) {
        //1、删除当前角色所有旧权限
        Ebean.getServer(dataSourceType).createSqlUpdate("delete from M_roleAuth where role_id=?").setParameter(1,map.get("roleId")).execute();
        //2、添加当前角色所有新权限

        //循环取出权限id
        for(int i=0;i<map.size()-1;i++){
            String authority_id=map.get("authIds[" + i + "]");
            RoleAuth roleAuth=new RoleAuth();
            roleAuth.role_id = map.get("roleId");
            roleAuth.id= UUIDUtil.getUUID();
            roleAuth.code= Authority.getObject(authority_id,dataSourceType).code;
            roleAuth.authority_id=authority_id;
            roleAuth.updatetime=new Date();
            roleAuth.updateuser=session("id");
            roleAuth.station_id=session("site_id");
            Ebean.getServer(dataSourceType).save(roleAuth);
        }
    }
    /**
     * 添加
     * @param
     * @return
     */
    @Transactional
    public static RoleAuth insert(RoleAuth roleAuth,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(roleAuth);
        return roleAuth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getAuthority_id() {
        return authority_id;
    }

    public void setAuthority_id(String authority_id) {
        this.authority_id = authority_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
