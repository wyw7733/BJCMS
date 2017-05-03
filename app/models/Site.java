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
@Table(name = "M_Site")
public class Site extends Model {
    @Id
    public String id;
    /**
     * 站点名称
     */
    public String name;
    /**
     * 站点简称
     */
    public String memo;
    /**
     * 域名
     */
    public String dns;
    /**
     * 备案信息
     */
    public String bamemo;
    /**
     * 站长邮箱
     */
    public String siteemail;
    /**
     * 统计代码
     */
    public String statisticscode;
    /**
     * 版权信息
     */
    public String copyright;
    /**
     * SEO标题
     */
    public String seotitle;
    /**
     * SEO关键字
     */
    public String seokey;
    /**
     * SEO描述
     */
    public String seomemo;
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
     * 后台地址
     */
    public String manage_url;

    /**
     * 根据id获取对象
     *
     * @param
     * @return
     */
    @Transactional
    public static Site getObject(String id, String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Site.class).where().idEq(id).findUnique();
    }

    /**
     * 条件查询
     *
     * @param
     * @return
     */
    @Transactional
    public static List<Site> selectList(Map<String, Object> map, String dataSourceType) {
        if (ObjectUtil.isNotNull(map)) {
            return Ebean.getServer(dataSourceType).find(Site.class).where().allEq(map).findList();
        }
        return Ebean.getServer(dataSourceType).find(Site.class).findList();
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @Transactional
    public static Site insert(Site site, String dataSourceType) {
        Ebean.getServer(dataSourceType).save(site);
        return site;
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    @Transactional
    public static Site update(Site site, String dataSourceType) {
        Ebean.getServer(dataSourceType).update(site);
        return site;
    }

    @Transactional
    public static Site getSite(String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Site.class).findUnique();
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

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getBamemo() {
        return bamemo;
    }

    public void setBamemo(String bamemo) {
        this.bamemo = bamemo;
    }

    public String getSiteemail() {
        return siteemail;
    }

    public void setSiteemail(String siteemail) {
        this.siteemail = siteemail;
    }

    public String getStatisticscode() {
        return statisticscode;
    }

    public void setStatisticscode(String statisticscode) {
        this.statisticscode = statisticscode;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getSeotitle() {
        return seotitle;
    }

    public void setSeotitle(String seotitle) {
        this.seotitle = seotitle;
    }

    public String getSeokey() {
        return seokey;
    }

    public void setSeokey(String seokey) {
        this.seokey = seokey;
    }

    public String getSeomemo() {
        return seomemo;
    }

    public void setSeomemo(String seomemo) {
        this.seomemo = seomemo;
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

    public String getManage_url() {
        return manage_url;
    }

    public void setManage_url(String manage_url) {
        this.manage_url = manage_url;
    }
}


