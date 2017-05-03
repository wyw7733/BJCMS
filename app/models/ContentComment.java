package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import play.db.ebean.Model;
import util.Constant;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/3/9.
 */
@Entity
@Table(name = "C_ContentComment")
public class ContentComment extends Model {
    @Id
    public String id;
    /**
     * 内容id
     */
    public String content_id;
    /**
     * 评论用户
     */
    public String user_id;
    /**
     * 评论内容
     */
    public String name;
    /**
     * 评论时间
     */
    public Date createtime;
    /**
     * 审核状态
     */
    @javax.persistence.Column(name = "audit1", nullable = true, length = 1)
    public String audit;
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
     * @return
     */
    @Transactional
    public static List<ContentComment> selectList(Map<String, Object> map,String dataSourceType) {
        List<ContentComment> list = new ArrayList<ContentComment>();
        if (ObjectUtil.isNotNull(map)) {
            list = Ebean.getServer(dataSourceType).find(ContentComment.class).where().allEq(map).findList();
        } else {
            list = Ebean.getServer(dataSourceType).find(ContentComment.class).findList();
        }
        return list;
    }

    /**
     * 条件查询
     *
     * @param
     * @return
     */
    @Transactional
    public static List<SqlRow> selectMapList(Map<String, String> searchMap,String dataSourceType) {
        StringBuilder sql = new StringBuilder("SELECT cc.id,cc.name,cc.createtime,cc.audit1,au.name AS username,c.title FROM C_ContentComment cc LEFT JOIN M_AdminUser au ON cc.user_id=au.id\n" +
                "LEFT JOIN C_Content c ON  cc.content_id=c.id");
        sql.append(" order by createtime desc");
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        List<SqlRow> rowList = sqlQuery.findList();
        return rowList;
    }

    /**
     * 更新
     *
     * @param
     * @return
     */
    @Transactional
    public static ContentComment update(ContentComment contentComment,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(contentComment);
        return contentComment;
    }

    /**
     * 批量审核
     *
     * @param
     * @return
     */
    @Transactional
    public static void auditComments(String ids, ContentComment contentComment,String dataSourceType) {
        if (ObjectUtil.isNotNull(ids)) {
            for (String id : ids.split(",")) {
                ContentComment temp = new ContentComment();
                BeanUtils.copyProperties(contentComment, temp);
                temp.id = id;
                Ebean.getServer(dataSourceType).update(temp);
            }
        }
    }

    /**
     * 批量删除
     *
     * @param
     * @return
     */
    public static void deleteComments(String ids,String dataSourceType) {
        if (ObjectUtil.isNotNull(ids)) {
            for (String id : ids.split(",")) {
                Ebean.getServer(dataSourceType).delete(ContentComment.class, id);
            }
        }
    }

    /**
     * 条件统计查询
     *
     * @param
     * @return
     */
    public static List<SqlRow> commentCountList(Map<String, String> searchMap,String dataSourceType) {
        StringBuilder sql = null;
        if(Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            sql = new StringBuilder("select month, count(count) count from (SELECT to_char(cc.createtime,'mm') month, cc.id count FROM C_ContentComment cc ");
        }
        if(Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            sql = new StringBuilder("SELECT DATE_FORMAT(cc.createtime,'%m') month, COUNT(cc.id) count FROM C_ContentComment cc ");
        }
        sql.append("LEFT JOIN C_Content ct ON cc.content_id=ct.id ");
        sql.append("LEFT JOIN C_Column cn ON ct.column_id=cn.id where 1=1");
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("year"))) {
                if(Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and to_char(cc.createtime, 'yyyy')=? ");
                }
                if(Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and YEAR(cc.createtime)=? ");
                }
                args.add(searchMap.get("year"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("content_id"))) {
                sql.append(" and cc.content_id=? ");
                args.add(searchMap.get("content_id"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("column_id"))) {
                sql.append(" and ct.column_id=? ");
                args.add(searchMap.get("column_id"));
            }
        }
        if(Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            sql.append(")GROUP BY month");
        }
        if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            sql.append("GROUP BY month");
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }

        List<SqlRow> rowList = sqlQuery.findList();
        int allCount = allCount(searchMap,dataSourceType);
        for (SqlRow sqlRow : rowList) {
            Integer count = sqlRow.getInteger("count");
            double temp = (double) count / (double) allCount;
            String percentage = temp * 100 + "%";
            sqlRow.put("percentage", percentage);
        }
        return rowList;
    }

    /**
     * 条件统计总数
     *
     * @param
     * @return
     */
    public static int allCount(Map<String, String> searchMap,String dataSourceType) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(cc.id) allCount FROM C_ContentComment cc ");
        sql.append("LEFT JOIN C_Content ct ON cc.content_id=ct.id ");
        sql.append("LEFT JOIN C_Column cn ON ct.column_id=cn.id where 1=1");
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("year"))) {
                if(Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and to_char(cc.createtime, 'yyyy')=? ");
                }
                if(Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and YEAR(cc.createtime)=? ");
                }
                args.add(searchMap.get("year"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("content_id"))) {
                sql.append(" and cc.content_id=? ");
                args.add(searchMap.get("content_id"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("column_id"))) {
                sql.append(" and ct.column_id=? ");
                args.add(searchMap.get("column_id"));
            }
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();
        SqlRow sqlRow = rowList.get(0);
        Integer allCount = sqlRow.getInteger("allCount");
        return allCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
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
