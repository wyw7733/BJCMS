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
@Table(name = "M_Config")
public class Config extends Model {
    @Id
    public String id;
    /**
     * 部署路径
     */
    public String deploypath;
    /**
     * 端口号
     */
    public String port;
    /**
     * 静态页路径
     */
    public String htmlpath;
    /**
     * 附件路径
     */
    public String wordpath;
    /**
     * 系统邮箱名称
     */
    public String sysemail;
    /**
     * 系统邮箱密码
     */
    public String sysemailpass;
    /**
     * 系统邮箱SMTP
     */
    public String sysemailsmtp;
    /**
     * 更新人
     */
    public String updateuser;
    /**
     * 更新时间
     */
    public Date updatetime;
    /**
     * 站点Id
     */
    public String station_id;
    /**
     * 模板路径
     */
    public String templatepath;

    /**
     * 根据id获取对象
     *
     * @param
     * @return
     */
    @Transactional
    public static Config getObject(String id, String dataSourceType) {

        return Ebean.getServer(dataSourceType).find(Config.class).where().idEq(id).findUnique();

    }

    /**
     * 获取第一个对象
     *
     * @param
     * @return
     */
    @Transactional
    public static Config getObject(String dataSourceType) {
        Config config = new Config();
        List<Config> list = Config.selectList(null, dataSourceType);
        //判断列表是否有值
        if (list.size() > 0) {
            //若有值,则取第一条数据
            config = list.get(0);
        }
        return config;
    }

    /**
     * 条件查询
     *
     * @param
     * @return
     */
    @Transactional
    public static List<Config> selectList(Map<String, Object> map, String dataSourceType) {
        if (ObjectUtil.isNotNull(map)) {
            return Ebean.getServer(dataSourceType).find(Config.class).where().allEq(map).findList();
        }
        return Ebean.getServer(dataSourceType).find(Config.class).findList();
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @Transactional
    public static Config insert(Config config, String dataSourceType) {
        Ebean.getServer(dataSourceType).save(config);
        return config;
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    @Transactional
    public static Config update(Config config, String dataSourceType) {
        Ebean.getServer(dataSourceType).update(config);
        return config;
    }

    @Transactional
    public static Config getConfig(String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Config.class).findUnique();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeploypath() {
        return deploypath;
    }

    public void setDeploypath(String deploypath) {
        this.deploypath = deploypath;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHtmlpath() {
        return htmlpath;
    }

    public void setHtmlpath(String htmlpath) {
        this.htmlpath = htmlpath;
    }

    public String getWordpath() {
        return wordpath;
    }

    public void setWordpath(String wordpath) {
        this.wordpath = wordpath;
    }

    public String getSysemail() {
        return sysemail;
    }

    public void setSysemail(String sysemail) {
        this.sysemail = sysemail;
    }

    public String getSysemailpass() {
        return sysemailpass;
    }

    public void setSysemailpass(String sysemailpass) {
        this.sysemailpass = sysemailpass;
    }

    public String getSysemailsmtp() {
        return sysemailsmtp;
    }

    public void setSysemailsmtp(String sysemailsmtp) {
        this.sysemailsmtp = sysemailsmtp;
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

    public String getTemplatepath() {
        return templatepath;
    }

    public void setTemplatepath(String templatepath) {
        this.templatepath = templatepath;
    }
}
