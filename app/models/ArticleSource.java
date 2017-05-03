package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文章来源
 * Created by ysf on 2016/3/15.
 */
@Entity
@Table(name = "C_ArticleSource")
public class ArticleSource extends Model {
    @Id
    public String id;
    /**
     * 名称
     */
    public String name;
    /**
     * 链接
     */
    public String href;
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
     * 条件查询
     *
     * @param
     * @return ArticleSource
     */
    @Transactional
    public static List<ArticleSource> selectList(String dataSourceType) {
        List<ArticleSource> list = new ArrayList<>();
        list = Ebean.getServer(dataSourceType).find(ArticleSource.class).findList();
        return list;
    }

    /**
     * 添加
     *
     * @param articleSource
     * @return adminUser
     */
    @Transactional
    public static void insert(ArticleSource articleSource, String dataSourceType) {
        //添加用户信息
        Ebean.getServer(dataSourceType).save(articleSource);
    }

    /**
     * 根据id查询对象
     *
     * @param id
     * @return adminUser
     */
    @Transactional
    public static ArticleSource getObject(String id, String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(ArticleSource.class).where().idEq(id).findUnique();
    }

    /**
     * 修改
     *
     * @param articleSource
     * @return adminUser
     */
    @Transactional
    public static void update(ArticleSource articleSource, String dataSourceType) {
        //修改用户信息
        Ebean.getServer(dataSourceType).update(articleSource);
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    @Transactional
    public static void delete(ArticleSource articleSource, String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(ArticleSource.class, articleSource.id);
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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
