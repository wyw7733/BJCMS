package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.db.ebean.Model;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户
 * Created by zuopanpan on 2016/4/7.
 */
@Entity
@Table(name = "M_User")
public class User extends Model {
    @Id
    public String id;
    /**
     * 用户名
     */
    public String name;
    /**
     * 密码
     */
    public String password;
    /**
     * 姓名
     */
    public String tname;
    /**
     * 电话
     */
    public String tel;
    /**
     * 邮箱
     */
    public String mail;
    /**
     * 类型 0：个人，1：机构
     */
    public String type;
    /**
     * 更新人
     */
    public String updateuser;
    /**
     * 更新时间
     */
    public Date updatetime;

    /**
     * 新增
     *
     * @param
     * @return
     */
    @Transactional
    public static User insert(User user, String dataSourceType) {
        Ebean.getServer(dataSourceType).save(user);
        return user;
    }

    /**
     * 新增
     *
     * @param
     * @return
     */
    @Transactional
    public static User getObject(String id, String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(User.class).where().idEq(id).findUnique();
    }

    /**
     * 更新
     *
     * @param
     * @return
     */
    @Transactional
    public static User update(User user, String dataSourceType) {
        Ebean.getServer(dataSourceType).update(user);
        return user;
    }

    /**
     * 登录
     *
     * @param
     * @return
     */
    @Transactional
    public static String login(Map<String, Object> map, String dataSourceType) {
        Object password = map.remove("password");
        List<User> list = Ebean.getServer(dataSourceType).find(User.class).where().allEq(map).findList();
        if (list.size() == 0) {
            return "namedontExist";
        }
        map.put("password", password);
        List<User> list1 = Ebean.getServer(dataSourceType).find(User.class).where().allEq(map).findList();
        if (list1.size() == 1) {
            return list1.get(0).id;
        }
        return null;
    }

    /**
     * 重名校验
     *
     * @param existMap
     * @return adminUser
     */
    @Transactional
    public static Boolean isExist(Map<String, String> existMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("select * from M_User u where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(existMap)) {
            if (ObjectUtil.isNotNull(existMap.get("name"))) {
                sql.append(" and u.name=?");
                args.add(existMap.get("name"));
            }

            if (ObjectUtil.isNotNull(existMap.get("id"))) {
                sql.append(" and u.id!=?");
                args.add(existMap.get("id"));
            }
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> list = sqlQuery.findList();
        if (ObjectUtil.isNotNull(list)) {
            return true;
        }
        return false;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
