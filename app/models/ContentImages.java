package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.db.ebean.Model;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by houyaohui on 2016/2/2.
 */
@Entity
@Table(name = "C_ContentImages")
public class ContentImages extends Model {
    @Id
    public String id;
    /**
     * 内容ID
     */
    public String content_id;
    /**
     * 排列顺序
     */
    public String priority;
    /**
     * 图片路径
     */
    public String attachmentpath;
    /**
     * 图片名称
     */
    public String attachmentname;
    /**
     * 图片内容
     */
    public String attachmentnews;
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

    public static ContentImages getObject(String id, String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(ContentImages.class).where().idEq(id).findUnique();
    }

    /**
     * 添加
     *
     * @param content
     * @return Column
     */
    @Transactional
    public static ContentImages insert(ContentImages content, String dataSourceType) {
        Ebean.getServer(dataSourceType).save(content);
        return content;
    }

    /**
     * 修改
     *
     * @param content
     * @return Column
     */
    @Transactional
    public static ContentImages upadte(ContentImages content, String dataSourceType) {
        Ebean.getServer(dataSourceType).update(content);
        return content;
    }

    /**
     * 删除
     *
     * @param content
     * @return Column
     */
    @Transactional
    public static ContentImages delete(ContentImages content, String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(ContentImages.class, content.id);
        return content;

    }

    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    public static List<SqlRow> selectList(Map<String, String> searchMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("select c.* from C_ContentImages c where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (searchMap != null) {
            if (ObjectUtil.isNotNull(searchMap.get("content_id"))) {
                sql.append(" and c.content_id=? ");
                args.add(searchMap.get("content_id"));
            }
        } else {
            throw new RuntimeException("查询参数searchMap不能为null");
        }
        sql.append(" order by priority asc");
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();

        return rowList;
    }


    /**
     * 根据文章id删除
     *
     * @param contentId
     */
    public static void deleteByContentId(String contentId, String dataSourceType) {
        SqlUpdate sqlUpdate = Ebean.getServer(dataSourceType).createSqlUpdate("delete from C_ContentImages where content_id='" + contentId + "'");
        sqlUpdate.execute();
    }

    public static void updateNotInId(String ids, String content_id, String dataSourceType) {
        String sql = "delete from C_ContentImages where content_id='" + content_id + "' and id not in (" + ids + ")";
        SqlUpdate sqlUpdate = Ebean.getServer(dataSourceType).createSqlUpdate(sql);
        sqlUpdate.execute();
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAttachmentpath() {
        return attachmentpath;
    }

    public void setAttachmentpath(String attachmentpath) {
        this.attachmentpath = attachmentpath;
    }

    public String getAttachmentname() {
        return attachmentname;
    }

    public void setAttachmentname(String attachmentname) {
        this.attachmentname = attachmentname;
    }

    public String getAttachmentnews() {
        return attachmentnews;
    }

    public void setAttachmentnews(String attachmentnews) {
        this.attachmentnews = attachmentnews;
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
