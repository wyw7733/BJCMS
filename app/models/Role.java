package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import play.db.ebean.Model;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/3/1.
 */
@Entity
@Table(name="M_role")
public class Role extends Model {
    @Id
    public String id;
    /** 角色名称*/
    public String name;
    /** 角色描述*/
    public String memo;
    /** code*/
    public String tier_code;
    /** 是否删除 0：否，1：是*/
    public String del;
    /** 更新人*/
    public String updateuser;
    /** 更新时间*/
    public Date updatetime;
    /** 站点id*/
    public String station_id;
    /**
     * 根据id获取对象
     * @param
     * @return
     */
    @Transactional
    public static Role getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Role.class).where().idEq(id).findUnique();
    }
    /**
     * 条件查询
     * @param
     * @return
     */
    @Transactional
    public static List<Role> selectList(Map<String,Object> map,String dataSourceType) {
        if(ObjectUtil.isNotNull(map)){
            return Ebean.getServer(dataSourceType).find(Role.class).where().allEq(map).findList();
        }
        return Ebean.getServer(dataSourceType).find(Role.class).findList();
    }
    /**
     * 新增
     * @param
     * @return
     */
    @Transactional
    public static Role insert(Role role,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(role);
        return role;
    }
    /**
     * 更新
     * @param
     * @return
     */
    @Transactional
    public static Role update(Role role,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(role);
        return role;
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
     * @return
     */
    public static String getCodeVal(String parentId,String dataSourceType) {
        String parentCode="";
        StringBuilder sql=new StringBuilder("select MAX(tier_code) code from M_role where del=0");
        if(ObjectUtil.isNull(parentId)){
            sql.append(" and LENGTH(tier_code)=3");
        }else if ( !"0".equals(parentId)) {
            Role role = getObject(parentId,dataSourceType);
            parentCode = role.tier_code;
            sql.append(" and tier_code like'" + parentCode + "___'");
        }
        // 获取当前最大code
        String codeMax = searchCodeMax(sql.toString(),dataSourceType);
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTier_code() {
        return tier_code;
    }

    public void setTier_code(String tier_code) {
        this.tier_code = tier_code;
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
