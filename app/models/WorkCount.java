package models;

import com.avaje.ebean.SqlRow;

import java.util.List;

/**
 * 工作量统计临时bean
 * Created by zuopanpan on 2016/3/15.
 */
public class WorkCount {
    /** 用户名*/
    public String name;
    /** 统计数据*/
    public List<SqlRow> dateList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SqlRow> getDateList() {
        return dateList;
    }

    public void setDateList(List<SqlRow> dateList) {
        this.dateList = dateList;
    }
}
