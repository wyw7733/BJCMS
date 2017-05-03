package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import util.UUIDUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wuyawei on 2017/4/24.
 */
@Entity
@Table(name="E_UseLink")
public class UseLink {
    @Id
    public String id;
    //链接名称
    public String name;
    // url
    public String url;
    // 是否删除 0:否  1：是
    public String del;
    // 更新人
    public String update_user;
    // 更新时间
    public Date update_time;
    // 站点id
    public String station_id;
    //排序
    public String order_int;

    /**
     * 查询列表
     * @param map
     * @param dataSource
     * @return
     */
    @Transactional
    public static List<UseLink> selectList(Map<String,Object> map,String dataSource){
        List<UseLink> list = new ArrayList<>();
        if(null==map){
            list = Ebean.getServer(dataSource).find(UseLink.class).findList();
        }
        return list;
    }

    /**
     * 添加单个对象
     * @return
     */
    @Transactional
    public static UseLink insert(UseLink useLink,String dataScource){
        Ebean.getServer(dataScource).save(useLink);
        return useLink;
    }

    /**
     * 根据id查询要修改的对象
     * @return
     */
    @Transactional
    public static UseLink getObject(String id,String dataSource){
        return Ebean.getServer(dataSource).find(UseLink.class).where().idEq(id).findUnique();
    }

    /**
     * 根据id修改对象
     * @return
     */
    @Transactional
    public static UseLink update(UseLink useLink,String dataSource){
        Ebean.getServer(dataSource).update(useLink);
        return useLink;
    }

    /**
     * 删除单条链接对象
     */
    @Transactional
    public static void delete(UseLink useLink, String dataSource) {
        Ebean.getServer(dataSource).delete(UseLink.class, useLink.id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDel() {
        return del;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public void setOrder_int(String order_int) {
        this.order_int = order_int;
    }

    public String getOrder_int() {

        return order_int;
    }

    @Override
    public String toString() {
        return "UseLink{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", del='" + del + '\'' +
                ", update_user='" + update_user + '\'' +
                ", update_time=" + update_time +
                ", station_id='" + station_id + '\'' +
                ", order_int='" + order_int + '\'' +
                '}';
    }
}
