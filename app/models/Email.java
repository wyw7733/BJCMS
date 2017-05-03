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
 * Created by zuopanpan on 2016/2/25.
 */
@Entity
@Table(name="M_Email")
public class Email extends Model {
    @Id
    public String id;
    /** 邮箱*/
    public String email;
    /** 邮箱密码*/
    public String emailpass;
    /** smtp*/
    public String smtp;
    /** 更新人*/
    public String updateuser;
    /** 更新时间*/
    public Date updatetime;
    /** 站点Id*/
    public String station_id;
    /**
     * 根据id获取对象
     * @param
     * @return
     */
    @Transactional
    public static Email getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Email.class).where().idEq(id).findUnique();
    }
    /**
     * 条件查询
     * @param
     * @return
     */
    @Transactional
    public static List<Email> selectList(Map<String,Object> map,String dataSourceType) {
        if(ObjectUtil.isNotNull(map)){
            return Ebean.getServer(dataSourceType).find(Email.class).where().allEq(map).findList();
        }
      return Ebean.getServer(dataSourceType).find(Email.class).findList();
    }
    /**
     * 添加
     * @param
     * @return
     */
    @Transactional
    public static Email insert(Email email,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(email);
        return email;
    }
    /**
     * 修改
     * @param
     * @return
     */
    @Transactional
    public static Email update(Email email,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(email);
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailpass() {
        return emailpass;
    }

    public void setEmailpass(String emailpass) {
        this.emailpass = emailpass;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
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
