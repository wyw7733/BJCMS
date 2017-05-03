package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zuopanpan on 2016/3/7.
 */
@Entity
@Table(name="M_LoginLogs")
public class LoginLogs extends Model{
    @Id
    public String id;
    /** 登录用户*/
    public String loginuser;
    /** 登录IP*/
    public String loginip;
    /** 更新时间*/
    public Date updatetime;
    /** 站点id*/
    public String station_id;
    /**
     * 添加
     * @param
     * @return
     */
    @Transactional
    public static LoginLogs insert(LoginLogs loginLogs,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(loginLogs);
        return loginLogs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginuser() {
        return loginuser;
    }

    public void setLoginuser(String loginuser) {
        this.loginuser = loginuser;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
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
