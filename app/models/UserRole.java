package models;

import com.avaje.ebean.Ebean;
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
 * Created by zuopanpan on 2016/3/2.
 */
@Entity
@Table(name = "M_userRole")
public class UserRole extends Model {
    @Id
    public String id;
    /**
     * 用户Id
     */
    public String user_id;
    /**
     * 角色Id
     */
    public String role_id;
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
     * 添加
     *
     * @param
     * @return
     */
    @Transactional
    public static UserRole insert(UserRole userRole, String dataSourceType) {
        Ebean.getServer(dataSourceType).save(userRole);
        return userRole;
    }

    /**
     * 条件查询
     *
     * @param
     * @return
     */
    @Transactional
    public static List<UserRole> selectList(Map<String, Object> map, String dataSourceType) {
        if (ObjectUtil.isNotNull(map)) {
            return Ebean.getServer(dataSourceType).find(UserRole.class).where().allEq(map).findList();
        }
        return Ebean.getServer(dataSourceType).find(UserRole.class).findList();
    }

    /**
     * 修改
     *
     * @param userRole
     * @return userRole
     */
    @Transactional
    public static UserRole update(UserRole userRole, String dataSourceType) {
        Ebean.getServer(dataSourceType).update(userRole);
        return userRole;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
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
