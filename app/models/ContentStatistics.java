package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.db.ebean.Model;
import util.Constant;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 内容统计
 * Created by zuopanpan on 2016/4/1.
 */
@Entity
@Table(name = "C_ContentStatistics")
public class ContentStatistics extends Model {
    @Id
    public String id;
    /**
     * 栏目id
     */
    public String column_id;
    /**
     * 内容id
     */
    public String content_id;
    /**
     * 总访问数
     */
    public int views;
    /**
     * 月访问数
     */
    public int viewsmonth;
    /**
     * 周访问数
     */
    public int viewsweek;
    /**
     * 日访问数
     */
    public int viewsday;
    /**
     * 总评论数
     */
    public int comments;
    /**
     * 月评论数
     */
    public int commentsmonth;
    /**
     * 周评论数
     */
    public int commentsweek;
    /**
     * 日评论数
     */
    public int commentsday;
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
     * 栏目访问量条件查询
     *
     * @param
     * @return
     */
    @Transactional
    public static List<SqlRow> selectList(Map<String, String> searchMap,String dataSourceType) {
        StringBuilder sql = new StringBuilder("SELECT cc.name,SUM(cs.views) views,SUM(cs.viewsday) viewsday,SUM(cs.viewsweek) viewsweek,SUM(cs.viewsmonth) viewsmonth FROM  C_Column cc ");
        sql.append("LEFT JOIN C_ContentStatistics cs ON cc.id=cs.column_id WHERE 1=1");
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("column"))) {
                if (searchMap.get("column").equals("root")) {//顶级栏目
                    sql.append(" AND LENGTH(cc.code)=3");
                } else {//底层栏目
                    sql.append(" AND LENGTH(cc.code)!=3");
                }
            }
            if (ObjectUtil.isNotNull(searchMap.get("del"))) {
                sql.append(" AND del=?");
                args.add(searchMap.get("del"));
            }
        }
        sql.append(" GROUP BY cc.name,cc.id");
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("path"))) {
                if (searchMap.get("path").equals("/columnViews")) {
                    sql.append(" ORDER BY SUM(cs.views) DESC,cc.id");
                } else if (searchMap.get("path").equals("/columnViewsday")) {
                    sql.append(" ORDER BY SUM(cs.viewsday) DESC,cc.id");
                } else if (searchMap.get("path").equals("/columnViewsweek")) {
                    sql.append(" ORDER BY SUM(cs.viewsweek) DESC,cc.id");
                } else if (searchMap.get("path").equals("/columnViewsmonth")) {
                    sql.append(" ORDER BY SUM(cs.viewsmonth) DESC,cc.id");
                }
            }
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();
        return rowList;
    }

    /**
     * 点击率条件查询
     *
     * @param
     * @return
     */
    @Transactional
    public static Page contentView(Map<String, String> searchMap,String dataSourceType) {
        Page page = new Page();
        StringBuilder sql = new StringBuilder("SELECT ct.title,cc.name,au.name username,SUM(cs.views) views,SUM(cs.viewsday) viewsday,SUM(cs.viewsweek) viewsweek,SUM(cs.viewsmonth) viewsmonth,ct.releasetime,ct.audit1 FROM  ");
        sql.append("C_Content ct LEFT JOIN C_Column cc ON ct.column_id=cc.id LEFT JOIN M_AdminUser au ON ct.createuser=au.id  ");
        sql.append("LEFT JOIN C_ContentStatistics cs ON ct.id=cs.content_id WHERE 1=1 and ct.audit1='1'");
        String sql1 = "";
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("column_id"))) {
                sql.append(" AND ct.column_id=?");
                args.add(searchMap.get("column_id"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("content_id"))) {
                sql.append(" AND ct.id=?");
                args.add(searchMap.get("content_id"));
            }
        }
        sql.append(" GROUP BY ct.id,ct.title, cc.name, au.name, ct.releasetime, ct.audit1");
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("path"))) {
                if (searchMap.get("path").equals("/views")) {
                    sql.append(" ORDER BY SUM(cs.views) DESC,ct.releasetime DESC,ct.id");
                } else if (searchMap.get("path").equals("/viewsday")) {
                    sql.append(" ORDER BY SUM(cs.viewsday) DESC,ct.releasetime DESC,ct.id");
                } else if (searchMap.get("path").equals("/viewsweek")) {
                    sql.append(" ORDER BY SUM(cs.viewsweek) DESC,ct.releasetime DESC,ct.id");
                } else if (searchMap.get("path").equals("/viewsmonth")) {
                    sql.append(" ORDER BY SUM(cs.viewsmonth) DESC,ct.releasetime DESC,ct.id");
                }
            }
        }
        int pageNum = 0;
        int rowsPerPage = 0;
        try {
            pageNum = Integer.parseInt(searchMap.get("pageNum"));
        } catch (Exception e) {
            pageNum = 1;
        }
        try {
            rowsPerPage = Integer.parseInt(searchMap.get("pageSize"));
        } catch (Exception e) {
            rowsPerPage = Constant.DEFAULT_PAGE_SIZE;
        }
        page.pageNum = pageNum;
        page.rowsPerPage = rowsPerPage;
        if(Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            StringBuffer sqlPage = new StringBuffer();
            sqlPage.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
            sqlPage.append(sql);
            sqlPage.append(") tmp_tb where ROWNUM<=");
            sqlPage.append((pageNum - 1) * rowsPerPage + rowsPerPage);
            sqlPage.append(") where row_id>");
            sqlPage.append((pageNum - 1) * rowsPerPage);
            sql1 = sql.toString();
            sql.delete(0, sql.length());
            sql.append(sqlPage);
        }
        if(Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            sql.append(" limit ").append((pageNum - 1) * rowsPerPage).append(",").append(rowsPerPage);
        }

        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();


        //查询总行数
        String countStr = "";
        if(Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            countStr = sql1.toString();
        }
        if(Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            countStr = sql.toString();
        }

        countStr = countStr.substring(countStr.indexOf("FROM"));
        countStr = "SELECT COUNT(*) AS count  " + countStr;
        countStr = countStr.substring(0, countStr.indexOf("GROUP"));
        SqlQuery sqlQueryCount = Ebean.getServer(dataSourceType).createSqlQuery(countStr);
        for (int i = 0; i < args.size(); i++) {
            sqlQueryCount.setParameter(i + 1, args.get(i));
        }
        SqlRow sqlRow = sqlQueryCount.findUnique();
        String rowNum = sqlRow.getString("count");

        page.rowsNum = Integer.parseInt(rowNum);
        int maxPage = (page.rowsNum - 1 + page.rowsPerPage) / page.rowsPerPage;
        page.maxPage = maxPage;
        page.list = rowList;
        return page;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumn_id() {
        return column_id;
    }

    public void setColumn_id(String column_id) {
        this.column_id = column_id;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getViewsmonth() {
        return viewsmonth;
    }

    public void setViewsmonth(int viewsmonth) {
        this.viewsmonth = viewsmonth;
    }

    public int getViewsweek() {
        return viewsweek;
    }

    public void setViewsweek(int viewsweek) {
        this.viewsweek = viewsweek;
    }

    public int getViewsday() {
        return viewsday;
    }

    public void setViewsday(int viewsday) {
        this.viewsday = viewsday;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getCommentsmonth() {
        return commentsmonth;
    }

    public void setCommentsmonth(int commentsmonth) {
        this.commentsmonth = commentsmonth;
    }

    public int getCommentsweek() {
        return commentsweek;
    }

    public void setCommentsweek(int commentsweek) {
        this.commentsweek = commentsweek;
    }

    public int getCommentsday() {
        return commentsday;
    }

    public void setCommentsday(int commentsday) {
        this.commentsday = commentsday;
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
