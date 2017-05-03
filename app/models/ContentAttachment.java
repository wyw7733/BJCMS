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
@Table(name = "C_ContentAttachment")
public class ContentAttachment extends Model {
    @Id
    public String id;
    /**
     * 内容ID
     */
    public String content_id;

    /**
     * 附件类型
     */
    public String type;

    /**
     * 排列顺序
     */
    public String priority;
    /**
     * pdf路径
     */
    public String pdfpath;
    /**
     * 附件路径
     */
    public String attachmentpath;
    /**
     * 附件名称
     */
    public String attachmentname;

    /**
     * 是否公开
     */
    public String opentype;
    /**
     * 下载次数
     */
    public Integer downloadcount;
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

    public static ContentAttachment getObject(String id, String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(ContentAttachment.class).where().idEq(id).findUnique();
    }

    /**
     * 添加
     *
     * @param content
     * @return Column
     */
    @Transactional
    public static ContentAttachment insert(ContentAttachment content, String dataSourceType) {
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
    public static ContentAttachment upadte(ContentAttachment content, String dataSourceType) {
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
    public static ContentAttachment delete(ContentAttachment content, String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(ContentAttachment.class, content.id);
        return content;

    }

    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Transactional
    public static List<SqlRow> selectList(Map<String, String> searchMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("select c.* from C_ContentAttachment c where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (searchMap != null) {
            if (ObjectUtil.isNotNull(searchMap.get("content_id"))) {
                sql.append(" and content_id=? ");
                args.add(searchMap.get("content_id"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("attachmentname"))) {
                sql.append(" and attachmentname=? ");
                args.add(searchMap.get("attachmentname"));
            }
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

    /**
     * 根据文章id删除
     *
     * @param contentId
     */
    public static void deleteByContentId(String contentId, String dataSourceType) {
        SqlUpdate sqlUpdate = Ebean.getServer(dataSourceType).createSqlUpdate("delete from C_ContentAttachment where content_id='" + contentId + "'");
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPdfpath() {
        return pdfpath;
    }

    public void setPdfpath(String pdfpath) {
        this.pdfpath = pdfpath;
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

    public String getOpentype() {
        return opentype;
    }

    public void setOpentype(String opentype) {
        this.opentype = opentype;
    }

    public Integer getDownloadcount() {
        return downloadcount;
    }

    public void setDownloadcount(Integer downloadcount) {
        this.downloadcount = downloadcount;
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
