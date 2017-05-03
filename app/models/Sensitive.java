package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.db.ebean.Model;
import util.ObjectUtil;
import util.UUIDUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by houyaohui on 2016/3/11.
 */
@Entity
@Table(name = "M_Sensitive")
public class Sensitive extends Model {
    @Id
    public String id;
    /**
     * 敏感词
     */
    public String search;
    /**
     * 替换词
     */
    public String replacement;
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
     * @param sensitive
     * @return sensitive
     */
    @Transactional
    public static Sensitive insert(Sensitive sensitive,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(sensitive);
        return sensitive;
    }

    /**
     * 修改
     *
     * @param sensitive
     * @return adminUser
     */
    @Transactional
    public static Sensitive update(Sensitive sensitive,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(sensitive);
        return sensitive;
    }

    @Transactional
    public static List<SqlRow> selectList(Map<String, String> searchMap,String dataSourceType) {
        StringBuilder sql = new StringBuilder("select * from M_Sensitive where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (searchMap != null) {
        } else {
            throw new RuntimeException("查询参数searchMap不能为null");
        }

        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();
        return rowList;
    }


    @Transactional
    public static Sensitive getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Sensitive.class).where().idEq(id).findUnique();
    }

    @Transactional
    public static void deleteObject(String id,String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(Sensitive.class, id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
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
