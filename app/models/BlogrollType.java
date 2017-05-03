package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/2/26.
 */
@Entity
@Table(name="E_BlogrollType")
public class BlogrollType extends Model {
    @Id
    public String id;
    /** 类型名称*/
    public String name;
    /** 排序*/
    public String priority;
    /** 是否删除 0:否  1：是*/
    public String del;
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
    public static List<BlogrollType> selectList(Map<String, Object> map,String dataSourceType) {
        List<BlogrollType> list = Ebean.getServer(dataSourceType).find(BlogrollType.class).where().allEq(map).findList();
        return list;
    }
    /**
     * 添加
     * @param
     * @return
     */
    @Transactional
    public static BlogrollType insert(BlogrollType blogrollType,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(blogrollType);
        return blogrollType;
    }
    /**
     * 更新
     * @param
     * @return
     */
    @Transactional
    public static BlogrollType update(BlogrollType blogrollType,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(blogrollType);
        return blogrollType;
    }
    /**
     * 根据id获取对象
     * @param
     * @return
     */
    @Transactional
    public static BlogrollType getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(BlogrollType.class).where().idEq(id).findUnique();
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
