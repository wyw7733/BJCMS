package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/1/28.
 */
@Entity
@Table(name = "C_ColumnType")
public class ColumnType extends Model {
    @Id
    public String id;
    public String name;
    public String code;
    public String memo;
    public String del;
    public String updateuser;
    public Date updatetime;
    public String station_id;
    public List<Column> columnList;

    /**
     * Description: 根据id查询对象
     *
     * @param id
     * @return
     */
    @Transactional
    public static ColumnType getObject(String id, String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(ColumnType.class).where().idEq(id).findUnique();
    }

    /**
     * 条件查询
     *
     * @param map
     * @return
     */
    @Transactional
    public static List<ColumnType> selectList(Map<String, Object> map, String dataSourceType) {
        List<ColumnType> list = Ebean.getServer(dataSourceType).find(ColumnType.class).where().allEq(map).findList();
        return list;
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

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }
}
