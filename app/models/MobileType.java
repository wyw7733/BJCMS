package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import play.db.ebean.Model;
import util.Constant;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/1/14.
 */
@Entity
@Table(name = "C_MobileType")
public class MobileType extends Model {
    @Id
    public String id;
    /** 名称*/
    public String name;
    /** code*/
    public String code;
    /** 备注*/
    public String memo;
    /** 是否删除：0否、1是*/
    public String del;
    /** 更新人*/
    public String updateuser;
    /** 更新日期*/
    public Date updatetime;
    /** 站点ID*/
    public String station_id;
    /**
     * 根据id获取对象
     * @param id
     * @return
     */
    @Transactional
    public static MobileType getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(MobileType.class).where().idEq(id).findUnique();
    }

    /**
     * 添加
     * @param mobileType
     * @return mobileType
     */
    @Transactional
    public static MobileType insert(MobileType mobileType,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(mobileType);
        return mobileType;
    }

    /**
     * 修改
     * @param mobileType
     * @return mobileType
     */
    @Transactional
    public static MobileType upadte(MobileType mobileType,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(mobileType);
        return mobileType;
    }

    /**
     * 删除
     * @param mobileType
     * @return mobileType
     */
    @Transactional
    public static MobileType delete(MobileType mobileType,String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(MobileType.class, mobileType.id);
        return mobileType;
    }
    /**
     * 模糊检索未删除列表
     * @param query
     * @return
     */
    @Transactional
    public static List<MobileType> find(String query,String dataSourceType) {
        query = query.replaceAll("_", "\\\\_");
        query = query.replaceAll("%", "\\\\%");
        query = query.replaceAll("'", "");
        return Ebean.getServer(dataSourceType).find(MobileType.class).where("del='" + Constant.DEL_NO + "' and (name like '%" + query + "%' or memo like '%" + query + "%')").findList();
    }
    /**
     * 条件查询
     * @param map
     * @return
     */
    @Transactional
    public static List<MobileType> selectList(Map<String, Object> map,String dataSourceType) {
        List<MobileType> list = Ebean.getServer(dataSourceType).find(MobileType.class).where().allEq(map).findList();
        return list;
    }
    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Transactional
    public static void deleteTypes(String ids,String dataSourceType) {
        if (ObjectUtil.isNotNull(ids)) {
            for (String id : ids.split(",")) {
                MobileType mobileType=new MobileType();
                mobileType.id=id;
                //设置删除状态
                mobileType.del=Constant.DEL_YES;
                Ebean.getServer(dataSourceType).update(mobileType);
            }

        }
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
