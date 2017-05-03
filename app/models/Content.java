package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Http;
import play.mvc.Http.MultipartFormData;
import util.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by houyaohui on 2016/2/2.
 */
@Entity
@Table(name = "C_Content")
public class Content extends Model {
    @Id
    public String id;
    /**
     * 栏目ID
     */
    public String column_id;
    /**
     * 模版ID
     */
    public String mobile_id;
    /**
     * 标题
     */
    public String title;
    /**
     * 副标题
     */
    public String subtitle;
    /**
     * 缩略图
     */
    public String thumbnail;
    /**
     * 作者
     */
    public String author;
    /**
     * 文章来源id
     */
    public String source_id;
    /**
     * 外部链接
     */
    public String httpurl;
    /**
     * 摘要
     */
    public String snippet;
    /**
     * 内容
     */
    public String content;
    /**
     * 去掉绝对路径的内容
     */
    public String content1;
    /**
     * 静态页路径
     */
    public String htmlpath;
    /**
     * 对应栏目code
     */
    public String code;
    /**
     * 发布时间
     */
    public Date releasetime;

    /**
     * 审核状态
     */
    @javax.persistence.Column(name = "audit1", nullable = true, length = 1)
    public String audit;
    /**
     * 审核人
     */
    public String audituser;
    /**
     * 审核时间
     */
    public Date audittime;
    /**
     * 审核意见
     */
    public String auditopinion;
    /**
     * 置顶状态
     */
    public String stick;
    /**
     * 阅读次数
     */
    public Integer readsize;
    /**
     * 收藏次数
     */
    public Integer collectsize;
    /**
     * 是否删
     */
    public String del;
    /**
     * 站点_ID
     */
    public String station_id;
    /**
     * 创建人
     */
    public String createuser;
    /**
     * 创建时间
     */
    public Date createtime;
    /**
     * 更新人
     */
    public String updateuser;
    /**
     * 更新时间
     */
    public Date updatetime;
    @Transient
    public List<SqlRow> imageList = Lists.newArrayList();
    @Transient
    public List<SqlRow> attachmentList = Lists.newArrayList();
    @Transient
    public String attachmentPath;

    /**
     * 首页查询最新文章
     *
     * @param
     * @return Column
     */
   @Transactional
    public static  List<SqlRow> selectNewest(String dataSourceType) {

        StringBuilder sql =  new StringBuilder("");
        if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            sql=new StringBuilder("select * from (select tmp_tb.*,ROWNUM row_id from (");
            sql.append("select c.*, u.name as authorName from C_Content c, M_ADMINUSER u  where c.createuser = u.id and c.audit1='1' order by c.updatetime desc ");
            sql.append(") tmp_tb where ROWNUM<=6) where row_id>0");
        }
        if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
           sql=new StringBuilder("SELECT @rowno:=@rowno+1 AS row_id,c.* FROM(SELECT c.*, u.name AS authorName FROM C_Content c, M_AdminUser u  WHERE c.createuser = u.id AND c.audit1='1' ORDER BY c.updatetime DESC LIMIT 0,6) c,(SELECT @rowno:=0) t");
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        List<SqlRow> rowList = sqlQuery.findList();
        return rowList;
    }

    /**
     * 根据id查询对象
     *
     * @param id
     * @return Column
     */
    @Transactional
    public static Content getObject(String id, String dataSourceType) {
        Content content = Ebean.getServer(dataSourceType).find(Content.class).where().idEq(id).findUnique();
        Map<String, String> search = Maps.newHashMap();
        search.put("content_id", content.id);
        content.attachmentList = ContentAttachment.selectList(search, dataSourceType);
        if(content.attachmentList.size()>0){
            content.attachmentPath=content.attachmentList.get(0).getString("attachmentpath");
        }
        content.imageList = ContentImages.selectList(search, dataSourceType);
        return content;
    }

    /**
     * 添加
     *
     * @param content
     * @return content
     */
    @Transactional
    public static Content insert(Content content, String dataSourceType) {
        content.content1 = content.content;
        //查询全局配置
        Site site = Site.getSite(dataSourceType);
        String str = content.content;
        String dns = site.dns;
        str = str.replaceAll(dns.substring(0, dns.length() - 1), "");
        content.content = str;
        Ebean.getServer(dataSourceType).save(content);
        return content;
    }

    /**
     * 添加文章
     *
     * @param body
     * @param form
     * @return Column
     */
 /*   @Transactional
    public static Content insertContent(Http.MultipartFormData body, Form<Content> form, String userId, String station_id, String dataSourceType) {

        Content content = form.get();
        content.createuser = userId;
        content.updateuser = userId;
        content.updatetime = new Date();
        content.station_id = station_id;
        String releasetimeString = form.data().get("releasetimeString");
        if (ObjectUtil.isNotNull(releasetimeString)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                content.releasetime = sdf.parse(releasetimeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (ObjectUtil.isNull(content.releasetime))
            content.releasetime = null;
        String mobile_id = content.mobile_id;
        //如果模版id为空，则取栏目的默认模版
        Column column = Column.getObject(content.column_id, dataSourceType);
        if (ObjectUtil.isNull(mobile_id)) {
            content.mobile_id = column.mobilecontent_id;
        }
        content.id = UUIDUtil.getUUID();
        List<MultipartFormData.FilePart> filePartList = body.getFiles();
        //查询配置z
        Config config = Config.getConfig(dataSourceType);
        if (ObjectUtil.isNotNull(config)) {
            //保存文件路径
            for (int i = 0; i < filePartList.size(); i++) {
                MultipartFormData.FilePart filePart = filePartList.get(i);
                String fileName = filePart.getKey();
                String path = saveFile(filePart, dataSourceType);
                switch (fileName) {
                    case "thumbnail":
                        content.thumbnail = path;
                        break;
                    case "accessorys":
                        ContentAttachment attachment = new ContentAttachment();
                        attachment.id = UUIDUtil.getUUID();
                        attachment.attachmentname = filePart.getFilename();
                        attachment.attachmentpath = path;
                        attachment.content_id = content.id;
                        attachment.downloadcount = 0;
                        attachment.type = "4";
                        ContentAttachment.insert(attachment, dataSourceType);
                        break;
                    //图文
                    case "attachmentname":
                        ContentImages images = new ContentImages();
                        images.id = UUIDUtil.getUUID();
                        images.content_id = content.id;
                        images.attachmentname = filePart.getFilename();
                        images.attachmentnews = form.data().get("attachmentnews[" + i + "]");
                        images.priority = form.data().get("priority[" + i + "]");
                        images.attachmentpath = path;
                        ContentImages.insert(images, dataSourceType);
                        break;
                    case "videos":
                        //新增视频附件
                        filePart = filePartList.get(i);
                        String path4 = saveFile(filePart, dataSourceType);
                        ContentAttachment attachmentVideo = new ContentAttachment();
                        attachmentVideo.id = UUIDUtil.getUUID();
                        attachmentVideo.attachmentname = filePart.getFilename();
                        attachmentVideo.attachmentpath = path4;
                        attachmentVideo.content_id = content.id;
                        attachmentVideo.downloadcount = 0;
                        attachmentVideo.type = "2";
                        ContentAttachment.insert(attachmentVideo, dataSourceType);
                        break;
                }
            }
            content.createtime = new Date();
            content.updatetime = content.createtime;
            content.del = Constant.DEL_NO;
            content.code = column.code;
            //静态页路径
            content.htmlpath = column.code + "/" + new Date().getTime() + ".html";
            //过滤敏感字符
            String str = content.content;
            List<SqlRow> sqlRowList = Sensitive.selectList(new HashMap<String, String>(),dataSourceType);
            for (SqlRow sqlRow : sqlRowList) {
                str = str.replace(sqlRow.getString("search"), sqlRow.getString("replacement"));
            }
            //保存数据
            content.content = str;

            //去掉绝对路径的内容
            content.content1 = content.content;
            //查询全局配置
            Site site = Site.getSite(dataSourceType);
            String str1 = content.content;
            String dns = site.dns;
            str1 = str.replaceAll(dns.substring(0, dns.length() - 1), "");
            content.content = str1;

            Ebean.getServer(dataSourceType).save(content);
        }
        return content;
    }*/
    /**
     * 添加文章
     *
     * @param body
     * @param form
     * @return Column
     */
    @Transactional
    public static String insertContent(Http.MultipartFormData body, Form<Content> form, String userId, String station_id, String dataSourceType) {

        Content content = form.get();
        content.createuser = userId;
        content.updateuser = userId;
        content.updatetime = new Date();
        content.station_id = station_id;
        String releasetimeString = form.data().get("releasetimeString");
        if (ObjectUtil.isNotNull(releasetimeString)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                content.releasetime = sdf.parse(releasetimeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (ObjectUtil.isNull(content.releasetime))
            content.releasetime = null;
        String mobile_id = content.mobile_id;
        //如果模版id为空，则取栏目的默认模版
        Column column = Column.getObject(content.column_id, dataSourceType);
        if (ObjectUtil.isNull(mobile_id)) {
            content.mobile_id = column.mobilecontent_id;
        }
        content.id = UUIDUtil.getUUID();
        List<MultipartFormData.FilePart> filePartList = body.getFiles();
        //查询配置z
        Config config = Config.getConfig(dataSourceType);
        if (ObjectUtil.isNotNull(config)) {
            //保存文件路径
            for (int i = 0; i < filePartList.size(); i++) {
                MultipartFormData.FilePart filePart = filePartList.get(i);
                String fileName = filePart.getKey();
                String path = saveFile(filePart, dataSourceType);
                switch (fileName) {
                    case "thumbnail":
                        content.thumbnail = path;
                        break;
                    case "accessorys":
                        ContentAttachment attachment = new ContentAttachment();
                        attachment.id = UUIDUtil.getUUID();
                        attachment.attachmentname = filePart.getFilename();
                        Map<String,String> map=new HashMap<>();
                        map.put("attachmentname",attachment.attachmentname);
                        List<SqlRow> list=ContentAttachment.selectList(map,dataSourceType);
                        if(list.size()>0){
                            return "false";
                        }
                        attachment.attachmentpath = path;
                        attachment.content_id = content.id;
                        attachment.downloadcount = 0;
                        attachment.type = "4";
                        ContentAttachment.insert(attachment, dataSourceType);
                        break;
                    //图文
                    case "attachmentname":
                        ContentImages images = new ContentImages();
                        images.id = UUIDUtil.getUUID();
                        images.content_id = content.id;
                        images.attachmentname = filePart.getFilename();
                        images.attachmentnews = form.data().get("attachmentnews[" + i + "]");
                        images.priority = form.data().get("priority[" + i + "]");
                        images.attachmentpath = path;
                        ContentImages.insert(images, dataSourceType);
                        break;
                    case "videos":
                        //新增视频附件
                        filePart = filePartList.get(i);
                        String path4 = saveFile(filePart, dataSourceType);
                        ContentAttachment attachmentVideo = new ContentAttachment();
                        attachmentVideo.id = UUIDUtil.getUUID();
                        attachmentVideo.attachmentname = filePart.getFilename();
                        attachmentVideo.attachmentpath = path4;
                        attachmentVideo.content_id = content.id;
                        attachmentVideo.downloadcount = 0;
                        attachmentVideo.type = "2";
                        ContentAttachment.insert(attachmentVideo, dataSourceType);
                        break;
                }
            }
            content.createtime = new Date();
            content.updatetime = content.createtime;
            content.del = Constant.DEL_NO;
            content.code = column.code;
            //静态页路径
            content.htmlpath = column.code + "/" + new Date().getTime() + ".html";
            //过滤敏感字符
            String str = content.content;
            List<SqlRow> sqlRowList = Sensitive.selectList(new HashMap<String, String>(),dataSourceType);
            for (SqlRow sqlRow : sqlRowList) {
                str = str.replace(sqlRow.getString("search"), sqlRow.getString("replacement"));
            }
            //保存数据
            content.content = str;

            //去掉绝对路径的内容
            content.content1 = content.content;
            //查询全局配置
            Site site = Site.getSite(dataSourceType);
            String str1 = content.content;
            String dns = site.dns;
            str1 = str.replaceAll(dns.substring(0, dns.length() - 1), "");
            content.content = str1;

            Ebean.getServer(dataSourceType).save(content);
        }
        return "true";
    }
    /**
     * 修改
     *
     * @param content
     * @return Column
     */
    @Transactional
    public static Content upadte(Content content, String dataSourceType) {
        Ebean.getServer(dataSourceType).update(content);
        return content;
    }

    @Transactional
    public static Content updateContent(Http.MultipartFormData body, Form<Content> form, String userId, String station_id, String dataSourceType) {
        Content content = form.get();
        content.thumbnail = form.data().get("contentThumbnail");
        content.updateuser = userId;
        content.updatetime = new Date();
        content.station_id = station_id;
        String releasetimeString = form.data().get("releasetimeString");
        if (ObjectUtil.isNotNull(releasetimeString)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                content.releasetime = sdf.parse(releasetimeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (ObjectUtil.isNull(content.releasetime))
            content.releasetime = null;
        String mobile_id = content.mobile_id;
        //如果模版id为空，则取栏目的默认模版
        Column column = Column.getObject(content.column_id, dataSourceType);
        content.code = column.code;
        if (ObjectUtil.isNull(mobile_id)) {
            content.mobile_id = column.mobilecontent_id;
        }
        content.updatetime = new Date();
        content.audit = "0";
        //查询全局配置
        Config config = Config.getConfig(dataSourceType);

        if (ObjectUtil.isNotNull(config)) {
            //获取之前的类型
            Content oldContent = Content.getObject(content.id, dataSourceType);
            Column oldColumn = Column.getObject(oldContent.column_id, dataSourceType);
            Column newColumn = Column.getObject(content.column_id, dataSourceType);
            if (!newColumn.type_id.equals(oldColumn.type_id)) {
                ContentAttachment.deleteByContentId(content.id, dataSourceType);
                ContentImages.deleteByContentId(content.id, dataSourceType);
            }
            List<MultipartFormData.FilePart> filePartList = body.getFiles();

            //修改没有改变图片的数据
            String oldId = form.data().get("oldId");
            //截掉最后一个逗号
            if (oldId.endsWith(",")) {
                oldId = oldId.substring(0, oldId.length() - 1);
            }
            String[] oldIds = oldId.split(",");
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < oldIds.length; j++) {
                if (!"change".equals(oldIds[j])) {
                    sb.append("'").append(oldIds[j]).append("',");
                }
            }
            String deleteIds = sb.toString();
            if (deleteIds.endsWith(",")) {
                deleteIds = deleteIds.substring(0, deleteIds.length() - 1);
            }
            if (ObjectUtil.isNotNull(deleteIds) && deleteIds.length() >= 32) {
                ContentImages.updateNotInId(deleteIds, content.id, dataSourceType);
            } else {
                ContentImages.updateNotInId("''", content.id, dataSourceType);
            }
            if (filePartList.size() <= 0 || filePartList.get(0).getKey().equals("thumbnail_")) {
                for (int j = 0; j < oldIds.length; j++) {
                    String id = oldIds[j];
                    if (ObjectUtil.isNotNull(id)) {
                        if (!"change".equals(id)) {
                            ContentImages temp = new ContentImages();
                            temp.id = id;
                            temp.priority = form.data().get("priority[" + j + "]");
                            temp.attachmentnews = form.data().get("attachmentnews[" + j + "]");
                            ContentImages.upadte(temp, dataSourceType);
                        }
                    }
                }
            }
            //保存文件路径
            for (int i = 0; i < filePartList.size(); i++) {
                MultipartFormData.FilePart filePart = filePartList.get(i);
                String fileName = filePart.getKey();
                switch (fileName) {
                    //缩略图
                    case "thumbnail_":
                        filePart = filePartList.get(i);
                        //删除本地文件
                        Content tempContent = Content.getObject(content.id, dataSourceType);
                        if (ObjectUtil.isNotNull(tempContent.thumbnail)) {
                            FileUtil.delete(config.htmlpath + tempContent.thumbnail);
                        }
                        String path = saveFile(filePart, dataSourceType);
                        content.thumbnail = path;
                        break;
                    //下载附件
                    case "accessorys":
                        //删除原来的附件
                        Map<String, String> searchMap = Maps.newHashMap();
                        searchMap.put("content_id", content.id);
                        List<SqlRow> sqlRowList = ContentAttachment.selectList(searchMap, dataSourceType);
                        for (SqlRow row : sqlRowList) {
                            String attachmentpath = row.getString("attachmentpath");
                            FileUtil.delete(config.htmlpath + attachmentpath);
                        }
                        ContentAttachment.deleteByContentId(content.id, dataSourceType);
                        //新增附件
                        filePart = filePartList.get(i);
                        String path1 = saveFile(filePart, dataSourceType);
                        ContentAttachment attachment = new ContentAttachment();
                        attachment.id = UUIDUtil.getUUID();
                        attachment.attachmentname = filePart.getFilename();
                        attachment.attachmentpath = path1;
                        attachment.content_id = content.id;
                        attachment.downloadcount = 0;
                        attachment.type = "4";
                        ContentAttachment.insert(attachment, dataSourceType);
                        break;
                    //图文
                    case "attachmentname":
                        int flag = i;
                        if (i == 0) {
                            for (int j = 0; j < oldIds.length; j++) {
                                String id = oldIds[j];
                                if (ObjectUtil.isNotNull(id)) {
                                    if (!"change".equals(id)) {
                                        ContentImages temp = new ContentImages();
                                        temp.id = id;
                                        temp.priority = form.data().get("priority[" + j + "]");
                                        temp.attachmentnews = form.data().get("attachmentnews[" + j + "]");
                                        ContentImages.upadte(temp, dataSourceType);
                                    } else {
                                        ContentImages images = new ContentImages();
                                        images.id = UUIDUtil.getUUID();
                                        images.content_id = content.id;
                                        images.attachmentname = filePart.getFilename();
                                        images.attachmentnews = form.data().get("attachmentnews[" + j + "]");
                                        images.priority = form.data().get("priority[" + j + "]");
                                        filePart = filePartList.get(flag);
                                        String path2 = saveFile(filePart, dataSourceType);
                                        images.attachmentpath = path2;
                                        ContentImages.insert(images, dataSourceType);
                                        flag++;
                                    }
                                }
                            }
                        }
                        break;
                    //视频
                    case "videos":
                        Map<String, String> searchMapVideos = Maps.newHashMap();
                        searchMapVideos.put("content_id", content.id);
                        List<SqlRow> sqlRowListVideos = ContentAttachment.selectList(searchMapVideos, dataSourceType);
                        for (SqlRow row : sqlRowListVideos) {
                            String attachmentpath = row.getString("attachmentpath");
                            FileUtil.delete(config.htmlpath + attachmentpath);
                        }
                        //删除原来的视频
                        ContentAttachment.deleteByContentId(content.id, dataSourceType);
                        //新增视频附件
                        filePart = filePartList.get(i);
                        String path4 = saveFile(filePart, dataSourceType);
                        ContentAttachment attachmentVideo = new ContentAttachment();
                        attachmentVideo.id = UUIDUtil.getUUID();
                        attachmentVideo.attachmentname = filePart.getFilename();
                        attachmentVideo.attachmentpath = path4;
                        attachmentVideo.content_id = content.id;
                        attachmentVideo.downloadcount = 0;
                        attachmentVideo.type = "2";
                        ContentAttachment.insert(attachmentVideo, dataSourceType);
                        break;
                }
            }
            //过滤敏感字符
            String str = content.content;
            List<SqlRow> sqlRowList = Sensitive.selectList(new HashMap<String, String>(),dataSourceType);
            for (SqlRow sqlRow : sqlRowList) {
                str = str.replace(sqlRow.getString("search"), sqlRow.getString("replacement"));
            }
            //保存数据
            content.content = str;

            //去掉绝对路径的内容
            content.content1 = content.content;
            //查询全局配置
            Site site = Site.getSite(dataSourceType);
            String str1 = content.content;
            String dns = site.dns;
            str1 = str.replaceAll(dns.substring(0, dns.length() - 1), "");
            content.content = str1;

            Content.upadte(content, dataSourceType);
            //generateContent(content, config);
        }
        return content;
    }


    /**
     * 删除
     *
     * @param content
     * @return Column
     */
    @Transactional
    public static Content delete(Content content, String dataSourceType) {
        Ebean.getServer(dataSourceType).delete(Content.class, content.id);
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
        StringBuilder sql = new StringBuilder("select c.*, cc.name as columnName, u.name as authorName, d.name as depName from C_Content c, C_Column cc, M_AdminUser u, M_Dept d where c.column_id = cc.id and c.createuser = u.id and u.dept_id = d.id");
        List<Object> args = Lists.newArrayList();
        if (searchMap != null) {
            //删除
            if (ObjectUtil.isNotNull(searchMap.get("del"))) {
                sql.append(" and c.del=? ");
                args.add(searchMap.get("del"));
            } else {
                sql.append(" and c.del='0' ");
            }
            //审核类型
            if (ObjectUtil.isNotNull(searchMap.get("audit"))) {
                sql.append(" and c.audit1=? ");
                args.add(searchMap.get("audit"));
            }
            //code
            if (ObjectUtil.isNotNull(searchMap.get("code"))) {
                sql.append(" and c.code like'").append(searchMap.get("code")).append("'");
            }
            //标题
            if (ObjectUtil.isNotNull(searchMap.get("likeTitle"))) {
                String title = searchMap.get("likeTitle").replaceAll("_", "\\\\_");
                title = title.replaceAll("%", "\\\\%");
                title = title.replaceAll("'", "");
                sql.append(" and c.title like'%").append(title).append("%'");
            }
            //作者
            if (ObjectUtil.isNotNull(searchMap.get("likeAuthor"))) {
                String likeAuthor = searchMap.get("likeAuthor").replaceAll("_", "\\\\_");
                likeAuthor = likeAuthor.replaceAll("%", "\\\\%");
                likeAuthor = likeAuthor.replaceAll("'", "");
                sql.append(" and c.author like'%").append(likeAuthor).append("%'");
            }
            //推荐
            if (ObjectUtil.isNotNull(searchMap.get("stick"))) {
                sql.append(" and c.stick=? ");
                args.add(searchMap.get("stick"));
            }
            //发布时间to_char(sysdate,¨yyyy/mm/dd¨)
            if (ObjectUtil.isNotNull(searchMap.get("likeReleasetime"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and  to_char(c.releasetime,'yyyy-mm-dd')=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and  DATE_FORMAT(c.releasetime,'%Y-%m-%d')=?");
                }
                args.add(searchMap.get("likeReleasetime"));
            }
            //大于发布时间
            if (ObjectUtil.isNotNull(searchMap.get("gtReleasetime"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and  to_char(c.releasetime,'yyyy-mm-dd')>=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and  DATE_FORMAT(c.releasetime,'%Y-%m-%d')>=?");
                }
                args.add(searchMap.get("gtReleasetime"));
            }
            //栏目id
            if (searchMap.get("column_id") != null) {
                sql.append(" and column_id=?");
                args.add(searchMap.get("column_id"));
            }
            //栏目ids
            if (searchMap.get("column_ids") != null) {
                sql.append(" and column_id in (");
                String column_ids = searchMap.get("column_ids");
                for (String column_id : column_ids.split(",")) {
                    sql.append("'").append(column_id).append("',");
                }
                String str = sql.substring(0, sql.length() - 1) + ")";
                sql = new StringBuilder(str);
            }
            //查询未发布
            if (searchMap.get("restartReleasetime") != null) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and  to_char(c.releasetime,'yyyy-mm-dd hh:mm:ss')>=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and  DATE_FORMAT(c.releasetime,'%Y-%m-%d %H:%i:%s')>=?");
                }
                args.add(searchMap.get("restartReleasetime"));
            }
            //排序
            if (searchMap.get("orderBy") != null) {
                sql.append(searchMap.get("orderBy"));
            } else {
                sql.append(" order by c.releasetime desc, c.id desc");
            }
            //是否分页
            if (ObjectUtil.isNotNull(searchMap.get("isPage"))) {
                int currentPage = 0;
                int pageSize = 0;
                try {
                    currentPage = Integer.parseInt(searchMap.get("page"));
                    pageSize = Integer.parseInt(searchMap.get("pageSize"));
                } catch (Exception e) {
                    currentPage = 1;
                    pageSize = Constant.DEFAULT_PAGE_SIZE;
                }
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    StringBuffer sqlPage = new StringBuffer();
                    sqlPage.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
                    sqlPage.append(sql);
                    sqlPage.append(") tmp_tb where ROWNUM<=");
                    sqlPage.append((currentPage - 1) * pageSize + pageSize);
                    sqlPage.append(") where row_id>");
                    sqlPage.append((currentPage - 1) * pageSize);
                    sql.delete(0, sql.length());
                    sql.append(sqlPage);
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" limit ").append((currentPage - 1) * pageSize).append(",").append(pageSize);
                }
            }
        } else {
            throw new RuntimeException("查询参数searchMap不能为null");
        }

        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();
        for (SqlRow sqlRow : rowList) {
            //查询附件
            Map<String, String> search = Maps.newHashMap();
            search.put("content_id", sqlRow.getString("id"));
            sqlRow.put("attachmentList", ContentAttachment.selectList(search, dataSourceType));
            //查询图片
            sqlRow.put("imageList", ContentImages.selectList(search, dataSourceType));
        }
        return rowList;
    }


    /**
     * 分页查询
     *
     * @param searchMap
     * @return
     */
    @Transactional
    public static Page selectListByPage(Map<String, String> searchMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("select c.*,cc.name as columnName from C_Content c,C_Column cc where 1=1 and c.column_id=cc.id ");
        String sql1 = "";
        List<Object> args = Lists.newArrayList();
        Page page = new Page();
        if (searchMap != null) {
            //删除
            if (ObjectUtil.isNotNull(searchMap.get("del"))) {
                sql.append(" and c.del=? ");
                args.add(searchMap.get("del"));
            } else {
                sql.append(" and c.del='0' ");
            }
            //审核类型
            if (ObjectUtil.isNotNull(searchMap.get("audit"))) {
                sql.append(" and c.audit1=? ");
                args.add(searchMap.get("audit"));
            }
            //code
            if (ObjectUtil.isNotNull(searchMap.get("code"))) {
                sql.append(" and c.code like'").append(searchMap.get("code")).append("'");
            }

            //标题
            if (ObjectUtil.isNotNull(searchMap.get("likeTitle"))) {
                String title = searchMap.get("likeTitle").replaceAll("_", "\\\\_");
                title = title.replaceAll("%", "\\\\%");
                title = title.replaceAll("'", "");
                sql.append(" and c.title like'%").append(title).append("%'");
            }
            //作者
            if (ObjectUtil.isNotNull(searchMap.get("likeAuthor"))) {
                String likeAuthor = searchMap.get("likeAuthor").replaceAll("_", "\\\\_");
                likeAuthor = likeAuthor.replaceAll("%", "\\\\%");
                likeAuthor = likeAuthor.replaceAll("'", "");
                sql.append(" and c.author like'%").append(likeAuthor).append("%'");
            }
            //发布时间
            if (ObjectUtil.isNotNull(searchMap.get("likeReleasetime"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and   to_char(c.releasetime,'yyyy-mm-dd')=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and  DATE_FORMAT(c.releasetime,'%Y-%m-%d')=?");
                }
                args.add(searchMap.get("likeReleasetime"));
            }
            //大于发布时间
            if (ObjectUtil.isNotNull(searchMap.get("gtReleasetime"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and   to_char(c.releasetime,'yyyy-mm-dd')>=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and  DATE_FORMAT(c.releasetime,'%Y-%m-%d')>=?");
                }
                args.add(searchMap.get("gtReleasetime"));
            }
            //栏目id
            if (searchMap.get("column_id") != null) {
                sql.append(" and column_id=?");
                args.add(searchMap.get("column_id"));
            }
            //关键字
            if (searchMap.get("keywords") != null) {
                String keywords = searchMap.get("keywords").replaceAll("_", "\\\\_");
                keywords = keywords.replaceAll("%", "\\\\%");
                keywords = keywords.replaceAll("'", "");
                sql.append(" AND c.title like '%" + keywords + "%' ");//名称
              /*  sql.append(" AND (c.title like '%" + keywords + "%' ");//名称
               /* if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" OR  to_char(c.releasetime,'yyyy-mm-dd') like '%" + keywords + "%' )");//时间
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" OR DATE_FORMAT(c.releasetime,'%Y-%m-%d') like '%" + keywords + "%' )");//时间
                }*/
            }
            //排除sql
            if (searchMap.get("excludeSql") != null) {
                sql.append(" AND ").append(searchMap.get("excludeSql"));//名称
            }
            sql.append(" order by c.releasetime desc");
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
            if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
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
            if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                sql.append(" limit ").append((pageNum - 1) * rowsPerPage).append(",").append(rowsPerPage);
            }
        } else {
            throw new RuntimeException("查询参数searchMap不能为null");
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();

        //查询总行数
        String countStr = "";
        if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            countStr = sql1.toString();
        }
        if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            countStr = sql.toString();
        }
        countStr = countStr.substring(countStr.indexOf("from"));
        countStr = "select count(*) as count  " + countStr;
        countStr = countStr.substring(0, countStr.indexOf("order"));
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


    /**
     * 保存文件
     *
     * @param filePart
     */

    private static String saveFile(MultipartFormData.FilePart filePart, String dataSourceType) {
        //查询配置
        Config config = Config.getConfig(dataSourceType);
        String path = "";
        if (ObjectUtil.isNotNull(config)) {
            String fileName = filePart.getFilename();
            File file = filePart.getFile();

            try {
                FileInputStream is = new FileInputStream(file);
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                String dirName = filePart.getKey();
                //图片上传插件默认提交name=file
                if ("file".equals(dirName)) {
                    dirName = "photos";
                } else if ("thumbnail_".equals(dirName)) {
                    dirName = "thumbnail";
                }
                File sf = new File(config.wordpath + dirName);
                if (!sf.exists()) {
                    sf.mkdirs();
                }
                String prefix = fileName.substring(fileName.lastIndexOf("."));
                path = sf.getPath() + "/" + UUIDUtil.getUUID() + prefix;
                if("accessorys".equals(dirName)){
                    path = sf.getPath() + "/" + fileName;
                }
                OutputStream os = new FileOutputStream(path);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
                path = path.substring(config.wordpath.length(), path.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }

    /**
     * 生成文章
     *
     * @param content         文章对象
     * @param config          网站全局配置对象
     * @param contentPath     生成内容静态页路径
     * @param indexPath       生成首页静态页路径
     * @param isGenerateIndex 是否生成首页
     * @param isShow          是否为预览
     */
    public static boolean generateContent(Content content, Config config, String contentPath, String indexPath, boolean isGenerateIndex, boolean isShow, String dataSourceType) {
        boolean flag = true;
        try {
            //获取模版路径
            Mobile mobile = Mobile.getObject(content.mobile_id, dataSourceType);
            String mobilePath = mobile.path;
            String begin = mobilePath.substring(0, mobilePath.lastIndexOf("/"));
            String end = mobilePath.substring(mobilePath.lastIndexOf("/") + 1, mobilePath.length());

            //将结果集保存到map中传给模版工具类
            Map<String, Object> result = Maps.newHashMap();
            Content previous = getPreviousContent(content, dataSourceType);
            Content next = getNextContent(content, dataSourceType);
            result.put("previousContent", previous);
            result.put("nextContent", next);
            result.put("content", content);

            //获取域名路径
            Site site = Site.getSite(dataSourceType);
            if (ObjectUtil.isNull(site)) {
                throw new RuntimeException("站点设置不能为空");
            }

            result.put("manage_url", site.manage_url);
            //查询栏目
            Map<String, String> searchMap = Maps.newHashMap();
            searchMap.put("del", Constant.DEL_NO);
            searchMap.put("code", "___");
            searchMap.put("orderBy", " order by columnorder");
            List<SqlRow> columnList = Column.selectListByMap(searchMap, dataSourceType);
            result.put("columnList", columnList);
            List<SqlRow> contentList = Lists.newArrayList();
            //查询每个栏目的最新一条内容
            for (SqlRow temp : columnList) {
                //查询每个栏目的最新一条内容
                Map<String, String> searchContentMap = Maps.newHashMap();
                searchContentMap.put("del", Constant.DEL_NO);
                searchContentMap.put("audit", Constant.AUDIT_PASS);
                searchContentMap.put("column_id", temp.getString("id"));
                searchContentMap.put("orderBy", " order by releasetime desc");
                List<SqlRow> list = Content.selectList(searchContentMap, dataSourceType);
                //获取每个栏目的第一条数据
                if (ObjectUtil.isNotNull(list)) {
                    contentList.add(list.get(0));
                }
            }
            result.put("contentList", contentList);
            if (ObjectUtil.isNotNull(content.id)) {
                if (ObjectUtil.isNull(content.htmlpath)) {
                    content.htmlpath = content.code + "/" + new Date().getTime() + ".html";
                    Content content1 = new Content();
                    content1.id = content.id;
                    content1.htmlpath = content.htmlpath;
                    Content.upadte(content1, dataSourceType);
                }
            }
            String path = config.htmlpath + "html/" + content.htmlpath;
            if (ObjectUtil.isNotNull(contentPath)) {
                path = contentPath;
            }
            //生成文章详情页
            if (isShow == true) {
                result.put("abspath", "html/");
            } else {
                result.put("abspath", "../");
            }
            result.put("root", site.dns);
            result.put("defaultServer",dataSourceType);
            FreeMarkerUtil.generateHtml(begin, path, end, result,dataSourceType);
            //生成首页
            if (isGenerateIndex == true) {
                path = config.htmlpath + "index.html";
                if (ObjectUtil.isNotNull(indexPath)) {
                    path = indexPath;
                }
                //根据首页模版生成首页
                mobilePath = Mobile.selectByTypeId(Constant.MOBILETYPE_INDEX, dataSourceType).get(0).path;
                begin = mobilePath.substring(0, mobilePath.lastIndexOf("/"));
                end = mobilePath.substring(mobilePath.lastIndexOf("/") + 1, mobilePath.length());
                result.put("abspath", "html/");
                result.put("root", site.dns);
                result.put("defaultServer",dataSourceType);
                FreeMarkerUtil.generateHtml(begin, path, end, result,dataSourceType);
            }

        } catch (Exception e) {
            flag = false;
            System.out.print(e.getMessage());
            BusinessException businessException = new BusinessException();
            businessException.id = UUIDUtil.getUUID();
            businessException.name = e.getMessage();
            businessException.updatetime = new Date();
            businessException.content_id = content.id;
            businessException.is_success = "0";
            BusinessException.insert(businessException,dataSourceType);
        } finally {
            return flag;
        }
    }

    /**
     * 审核单个文章不通过
     *
     * @param content
     */
    @Transactional
    public static void auditConten(Content content, String dataSourceType) {
        Ebean.getServer(dataSourceType).update(content);
        //重新生成首页
        Column.generateColumn(false, false, dataSourceType);
        Column.generateSelectColumn(content.column_id, dataSourceType);
    }

    /**
     * 批量审核文章
     *
     * @param ids
     */
    @Transactional
    public static void auditContens(String ids, Content content1, String dataSourceType) {
        //查询全局设置信息
        //判断审核状态是否为通过
        Config config = new Config();
        if (Constant.AUDIT_PASS.equals(content1.audit)) {
            config = Config.getObject(dataSourceType);
        }
        if (ObjectUtil.isNotNull(ids)) {
            for (String id : ids.split(",")) {
                Content temp = new Content();
                Content content = Content.getObject(id, dataSourceType);
                content.audituser = content1.audituser;
                //设置审核时间
                content.audittime = content1.audittime;
                //设置更新人
                content.updateuser = content1.updateuser;
                content.audit = content1.audit;
                //设置更新时间
                content.updatetime = new Date();
                BeanUtils.copyProperties(content, temp);
                temp.id = id;
                //判断审核状态是否为通过
                if (Constant.AUDIT_PASS.equals(content.audit)) {
                    if (ObjectUtil.isNull(temp.releasetime) || temp.releasetime.before(new Date())) {
                        temp.releasetime = new Date();
                        Content.upadte(temp, dataSourceType);
                        content = Content.getObject(id, dataSourceType);
                        //生成文章静态页
                        content.releasetime = temp.releasetime;
                        generateContent(content, config, "", "", true, false, dataSourceType);
                        Content nextContent = getNextContent(content, dataSourceType);
                        if (ObjectUtil.isNotNull(nextContent.id)) {
                            generateContent(nextContent, config, "", "", true, false, dataSourceType);
                        }
                        //生成二级栏目
                        Column column1 = Column.getObject(content.column_id, dataSourceType);
                        Column.generateSelectColumn(column1.id, dataSourceType);
                        Column column2 = Column.getObject(column1.parent, dataSourceType);
                        if (ObjectUtil.isNotNull(column2)) {
                            Column.generateSelectColumn(column2.id, dataSourceType);
                        }
                    } else {
                        temp.audit = Constant.AUDIT_TIMER;
                        Content.upadte(temp, dataSourceType);
                        final Content content2 = Content.getObject(id, dataSourceType);
                        final Config config1 = config;
                        final String dataSourceType1 = dataSourceType;
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                Content content3 = new Content();
                                BeanUtils.copyProperties(content2, content3);
                                content3.audit = Constant.AUDIT_PASS;
                                Content.upadte(content3, dataSourceType1);
                                boolean flag = generateContent(content3, config1, "", "", true, false, dataSourceType1);
                                if (flag) {
                                    //生成二级栏目
                                    Column column1 = Column.getObject(content3.column_id, dataSourceType1);
                                    Column.generateSelectColumn(column1.id, dataSourceType1);
                                    Column column2 = Column.getObject(column1.parent, dataSourceType1);
                                    if (ObjectUtil.isNotNull(column2)) {
                                        Column.generateSelectColumn(column2.id, dataSourceType1);
                                    }
                                    BusinessException businessException = new BusinessException();
                                    businessException.id = UUIDUtil.getUUID();
                                    businessException.name = "添加成功";
                                    businessException.is_success = "1";
                                    businessException.updatetime = new Date();
                                    businessException.content_id = content3.id;
                                    BusinessException.insert(businessException,dataSourceType1);
                                }
                            }
                        }, temp.releasetime);
                    }
                } else {
                    Content.upadte(temp, dataSourceType);
                    //生成二级栏目
                    Column column1 = Column.getObject(content.column_id, dataSourceType);
                    Column.generateSelectColumn(column1.id, dataSourceType);
                    Column column2 = Column.getObject(column1.parent, dataSourceType);
                    if (ObjectUtil.isNotNull(column2)) {
                        Column.generateSelectColumn(column2.id, dataSourceType);
                    }
                }
            }
        }
        //重新生成首页
        Column.generateColumn(false, false, dataSourceType);
    }

    /**
     * 批量恢复文章
     *
     * @param ids
     */
    @Transactional
    public static void recovery(String ids, Content content, String dataSourceType) {
        if (ObjectUtil.isNotNull(ids)) {
            for (String id : ids.split(",")) {
                Content temp = new Content();
                BeanUtils.copyProperties(content, temp);
                temp.id = id;
                temp.audit = Constant.AUDIT_NOTPASS;
                Content.upadte(temp, dataSourceType);
            }
        }
    }

    /**
     * 内容统计
     *
     * @param searchMap
     * @return
     */
    @Transactional
    public static List<SqlRow> countContentList(Map<String, String> searchMap, String dataSourceType) {
        StringBuilder sql = null;
        if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            sql = new StringBuilder("select months,count(COUNT) count from (SELECT to_char(releasetime,'mm') months, id COUNT FROM C_Content WHERE audit1='1' ");
        }
        if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            sql = new StringBuilder("SELECT DATE_FORMAT(releasetime,'%m') months,COUNT(id) COUNT FROM C_Content WHERE audit1='1' ");
        }
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("year"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and to_char(releasetime, 'yyyy')=? ");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and YEAR(releasetime)=? ");
                }
                args.add(searchMap.get("year"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("column_id"))) {
                sql.append(" and column_id=? ");
                args.add(searchMap.get("column_id"));
            }
        }
        if(Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            sql.append(")GROUP BY months  ORDER BY months");
        }
        if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            sql.append("GROUP BY months  ORDER BY months");
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();
        int allCount = getAllCount(searchMap, dataSourceType);
        for (SqlRow sqlRow : rowList) {
            Integer count = sqlRow.getInteger("count");
            double temp = (double) count / (double) allCount;
            String percentage = "";
            if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                DecimalFormat df = new DecimalFormat("#.##");
                percentage = df.format(temp * 100) + "%";
            }
            if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                percentage = temp * 100 + "%";
            }
            sqlRow.put("percentage", percentage);
        }
        return rowList;
    }

    /**
     * 统计
     *
     * @param searchMap
     * @return
     */
    @Transactional
    public static int getAllCount(Map<String, String> searchMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(id) allCount FROM C_Content WHERE audit1='1'");
        List<Object> args = Lists.newArrayList();
        if (searchMap != null) {
            if (searchMap.get("year") != null) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and to_char(releasetime, 'yyyy')=? ");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" and YEAR(releasetime)=? ");
                }
                args.add(searchMap.get("year"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("column_id"))) {
                sql.append(" and column_id=? ");
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


    /**
     * 发布工作量统计
     *
     * @param searchMap
     * @return
     */
    @Transactional
    public static List<WorkCount> workloadContentList(Map<String, String> searchMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("SELECT au.id user_id,au.name username,cn.name columnname,(SELECT COUNT(id) FROM C_Content WHERE column_id=cn.id AND audit1='1'");
        StringBuilder creatsql = new StringBuilder(") allcount,(SELECT COUNT(cct.id) FROM C_Content cct LEFT JOIN M_AdminUser mau ON mau.id=cct.createuser LEFT JOIN C_Column ccn ON cct.column_id=ccn.id WHERE mau.id=au.id AND ccn.id=cn.id AND cct.audit1='1'");
        StringBuilder auditsql = new StringBuilder(" (SELECT COUNT(cct.id) FROM C_Content cct LEFT JOIN M_AdminUser mau ON mau.id=cct.audituser LEFT JOIN C_Column ccn ON cct.column_id=ccn.id WHERE mau.id=au.id AND ccn.id=cn.id AND cct.audit1='1'");
        StringBuilder querysql = new StringBuilder(" FROM M_AdminUser au LEFT JOIN C_Content ct ON au.id=ct.createuser");
        querysql.append(" LEFT JOIN C_Column cn ON ct.column_id=cn.id WHERE 1=1 AND cn.id IS NOT NULL AND ct.audit1='1'");
        List<Object> args1 = Lists.newArrayList();
        List<Object> args2 = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("startyear"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" AND to_char(releasetime, 'yyyymm')>=?");
                    creatsql.append(" AND to_char(cct.releasetime, 'yyyymm')>=?");
                    auditsql.append(" AND to_char(cct.audittime, 'yyyymm')>=?");
                    querysql.append(" AND to_char(ct.releasetime, 'yyyymm')>=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" AND DATE_FORMAT(releasetime, '%Y%m ')>=?");
                    creatsql.append(" AND DATE_FORMAT(cct.releasetime, '%Y%m ')>=?");
                    auditsql.append(" AND DATE_FORMAT(cct.audittime, '%Y%m ')>=?");
                    querysql.append(" AND DATE_FORMAT(ct.releasetime, '%Y%m ')>=?");
                }
                args1.add(searchMap.get("startyear"));
                args2.add(searchMap.get("startyear"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("endyear"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" AND to_char(releasetime, 'yyyymm')<=?");
                    creatsql.append(" AND to_char(cct.releasetime, 'yyyymm')<=?");
                    auditsql.append(" AND to_char(cct.audittime, 'yyyymm')<=?");
                    querysql.append(" AND to_char(ct.releasetime, 'yyyymm')<=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" AND DATE_FORMAT(releasetime, '%Y%m ')<=?");
                    creatsql.append(" AND DATE_FORMAT(cct.releasetime, '%Y%m ')<=?");
                    auditsql.append(" AND DATE_FORMAT(cct.audittime, '%Y%m ')<=?");
                    querysql.append(" AND DATE_FORMAT(ct.releasetime, '%Y%m ')<=?");
                }
                args1.add(searchMap.get("endyear"));
                args2.add(searchMap.get("endyear"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("column_id"))) {
                querysql.append(" AND cn.id=?");
                args2.add(searchMap.get("column_id"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("user_id"))) {
                querysql.append(" AND au.id=?");
                args2.add(searchMap.get("user_id"));
            }

        }
        creatsql.append(" ) creatcount,");
        auditsql.append(" ) auditcount");
        sql.append(creatsql).append(auditsql).append(querysql);
        sql.append(" GROUP BY au.id, au.name,cn.name,cn.id");
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args1.size(); i++) {
            sqlQuery.setParameter(i + 1, args1.get(i));
        }
        int count = args1.size();
        for (int i = 0; i < args1.size(); i++) {
            sqlQuery.setParameter(i + 1 + count, args1.get(i));
        }
        count = 2 * args1.size();
        for (int i = 0; i < args1.size(); i++) {
            sqlQuery.setParameter(i + 1 + count, args1.get(i));
        }
        count = 3 * args1.size();
        for (int i = 0; i < args2.size(); i++) {
            sqlQuery.setParameter(i + 1 + count, args2.get(i));
        }
        //查询以发布为主统计工作量数据
        List<SqlRow> rowList = sqlQuery.findList();
        //查询以审核为主统计工作量数据
        List<SqlRow> auditList = contentAuditList(searchMap, dataSourceType);
        //将两个list合并且过滤重复数据
        List<SqlRow> resultList = removeDuplicate(rowList, auditList);
        //将有工作量的用户名字放进nameList
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            SqlRow sqlRow = resultList.get(i);
            String username = sqlRow.getString("username");
            if (!nameList.contains(username)) {
                nameList.add(username);
            }
        }
        //将工作量数据分组放入对应的用户下
        List<WorkCount> list = groupByUser(nameList, resultList);
        return list;
    }

    /**
     * 审核工作量统计
     *
     * @param searchMap
     * @return
     */
    @Transactional
    public static List<SqlRow> contentAuditList(Map<String, String> searchMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("SELECT au.id user_id, au.name username,cn.name columnname,(SELECT COUNT(id) FROM C_Content WHERE column_id=cn.id AND audit1='1'");
        StringBuilder creatsql = new StringBuilder(") allcount,(SELECT COUNT(cct.id) FROM C_Content cct LEFT JOIN M_AdminUser mau ON mau.id=cct.createuser LEFT JOIN C_Column ccn ON cct.column_id=ccn.id WHERE mau.id=au.id AND ccn.id=cn.id AND cct.audit1='1'");
        StringBuilder auditsql = new StringBuilder(" (SELECT COUNT(cct.id) FROM C_Content cct LEFT JOIN M_AdminUser mau ON mau.id=cct.audituser LEFT JOIN C_Column ccn ON cct.column_id=ccn.id WHERE mau.id=au.id AND ccn.id=cn.id AND cct.audit1='1'");
        StringBuilder querysql = new StringBuilder(" FROM M_AdminUser au LEFT JOIN C_Content ct ON au.id=ct.audituser");
        querysql.append(" LEFT JOIN C_Column cn ON ct.column_id=cn.id WHERE 1=1 AND cn.id IS NOT NULL AND ct.audit1='1'");
        List<Object> args1 = Lists.newArrayList();
        List<Object> args2 = Lists.newArrayList();

        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("startyear"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" AND to_char(releasetime, 'yyyymm')>=?");
                    creatsql.append(" AND to_char(cct.releasetime, 'yyyymm')>=?");
                    auditsql.append(" AND to_char(cct.audittime, 'yyyymm')>=?");
                    querysql.append(" AND to_char(ct.audittime, 'yyyymm')>=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" AND DATE_FORMAT(releasetime, '%Y%m ')>=?");
                    creatsql.append(" AND DATE_FORMAT(cct.releasetime, '%Y%m ')>=?");
                    auditsql.append(" AND DATE_FORMAT(cct.audittime, '%Y%m ')>=?");
                    querysql.append(" AND DATE_FORMAT(ct.audittime, '%Y%m ')>=?");
                }
                args1.add(searchMap.get("startyear"));
                args2.add(searchMap.get("startyear"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("endyear"))) {
                if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" AND to_char(releasetime, 'yyyymm')<=?");
                    creatsql.append(" AND to_char(cct.releasetime, 'yyyymm')<=?");
                    auditsql.append(" AND to_char(cct.audittime, 'yyyymm')<=?");
                    querysql.append(" AND to_char(ct.audittime, 'yyyymm')<=?");
                }
                if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
                    sql.append(" AND DATE_FORMAT(releasetime, '%Y%m ')<=?");
                    creatsql.append(" AND DATE_FORMAT(cct.releasetime, '%Y%m ')<=?");
                    auditsql.append(" AND DATE_FORMAT(cct.audittime, '%Y%m ')<=?");
                    querysql.append(" AND DATE_FORMAT(ct.audittime, '%Y%m ')<=?");
                }
                args1.add(searchMap.get("endyear"));
                args2.add(searchMap.get("endyear"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("column_id"))) {
                querysql.append(" AND cn.id=?");
                args2.add(searchMap.get("column_id"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("user_id"))) {
                querysql.append(" AND au.id=?");
                args2.add(searchMap.get("user_id"));
            }
        }
        creatsql.append(" ) creatcount,");
        auditsql.append(" ) auditcount");
        sql.append(creatsql).append(auditsql).append(querysql);
        sql.append(" GROUP BY au.id, au.name,cn.name,cn.id");
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args1.size(); i++) {
            sqlQuery.setParameter(i + 1, args1.get(i));
        }
        int count = args1.size();
        for (int i = 0; i < args1.size(); i++) {
            sqlQuery.setParameter(i + 1 + count, args1.get(i));
        }
        count = 2 * args1.size();
        for (int i = 0; i < args1.size(); i++) {
            sqlQuery.setParameter(i + 1 + count, args1.get(i));
        }
        count = 3 * args1.size();
        for (int i = 0; i < args2.size(); i++) {
            sqlQuery.setParameter(i + 1 + count, args2.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();
        return rowList;
    }

    /**
     * 将两个List<SqlRow>合并且过滤重复数据
     *
     * @return
     */
    public static List<SqlRow> removeDuplicate(List<SqlRow> rowList1, List<SqlRow> rowList2) {
        List<SqlRow> resultList = Lists.newArrayList();
        resultList.addAll(rowList1);
        for (int i = 0; i < rowList2.size(); i++) {
            SqlRow sqlRow = rowList2.get(i);
            boolean tag = false;
            for (int j = 0; j < rowList1.size(); j++) {
                SqlRow temp = rowList1.get(j);
                if (sqlRow.get("username").equals(temp.get("username"))) {
                    if (sqlRow.get("columnname").equals(temp.get("columnname"))) {
                        tag = true;
                        break;
                    }
                }
            }
            if (!tag) {
                resultList.add(rowList1.get(i));
            }
        }
        return resultList;
    }

    /**
     * 将工作量数据分组放入对应的用户下
     *
     * @return
     */
    public static List<WorkCount> groupByUser(List<String> nameList, List<SqlRow> resultList) {
        List<WorkCount> list = new ArrayList<>();
        for (int i = 0; i < nameList.size(); i++) {
            String username = nameList.get(i);
            WorkCount map = new WorkCount();
            map.name = username;
            List<SqlRow> dateList = Lists.newArrayList();
            for (int j = 0; j < resultList.size(); j++) {
                SqlRow sqlRow = resultList.get(j);
                if (username.equals(sqlRow.getString("username"))) {
                    dateList.add(sqlRow);
                }
            }
            map.dateList = dateList;
            list.add(map);
        }
        return list;
    }

    /**
     * 生成根据起始时间生成文章
     */
    public static void generateContents(String gtReleasetime, String column_id, String dataSourceType) {
        Config config = Config.getConfig(dataSourceType);
        Map<String, String> searchMap = Maps.newHashMap();
        //查询未删除的文章、通过审核的文章
        searchMap.put("del", Constant.DEL_NO);
        searchMap.put("audit", Constant.AUDIT_PASS);
        //如果栏目不为空则根据栏目生成文章
        if (ObjectUtil.isNotNull(column_id)) {
            Column column=Column.getObject(column_id,dataSourceType);
            searchMap.put("code", column.code);
        }
        if (ObjectUtil.isNotNull(gtReleasetime)) {
            searchMap.put("gtReleasetime", gtReleasetime);
        }
        List<SqlRow> contentList = Content.selectList(searchMap, dataSourceType);
        for (SqlRow row : contentList) {
            Content content = Content.getObject(row.getString("id"), dataSourceType);
            //生成文章
            generateContent(content, config, "", "", true, false, dataSourceType);
        }
    }

/*
    */

    /**
     * 预览
     *
     * @param body
     * @param form
     * @param userId
     */
    public static String showContent(Http.MultipartFormData body, Form<Content> form, String userId, String dataSourceType) {
        Content content = form.get();
        String mobile_id = content.mobile_id;
        //如果模版id为空，则取栏目的默认模版
        Column column = Column.getObject(content.column_id, dataSourceType);
        content.code = column.code;
        content.releasetime = new Date();
        if (ObjectUtil.isNull(mobile_id)) {
            content.mobile_id = column.mobilecontent_id;
        }
        //过滤敏感字符
        String str = content.content;
        List<SqlRow> sqlRowList = Sensitive.selectList(new HashMap<String, String>(),dataSourceType);
        for (SqlRow sqlRow : sqlRowList) {
            str = str.replace(sqlRow.getString("search"), sqlRow.getString("replacement"));
        }
        //保存数据
        content.content = str;
        //查询全局配置
        Config config = Config.getConfig(dataSourceType);
        Site site = Site.getSite(dataSourceType);
        String tempName = "temp.html";
        generateContent(content, config, config.htmlpath + tempName, "", false, true, dataSourceType);
        return site.dns + tempName;
    }

    /**
     * 根据文章预览
     *
     * @param content
     */
    public static String showContentByContent(Content content, String dataSourceType) {


        String mobile_id = content.mobile_id;
        //如果模版id为空，则取栏目的默认模版
        Column column = Column.getObject(content.column_id, dataSourceType);
        content.code = column.code;
        content.releasetime = new Date();
        if (ObjectUtil.isNull(mobile_id)) {
            content.mobile_id = column.mobilecontent_id;
        }
        //过滤敏感字符
        String str = content.content;
        List<SqlRow> sqlRowList = Sensitive.selectList(new HashMap<String, String>(),dataSourceType);
        for (SqlRow sqlRow : sqlRowList) {
            str = str.replace(sqlRow.getString("search"), sqlRow.getString("replacement"));
        }
        //保存数据
        content.content = str;
        //查询全局配置
        Config config = Config.getConfig(dataSourceType);
        Site site = Site.getSite(dataSourceType);
        String tempName = "temp.html";
        generateContent(content, config, config.htmlpath + tempName, "", false, true, dataSourceType);
        return site.dns + tempName;
    }

    /**
     * 查询文章列表中当前文章的上一篇文章
     *
     * @param content
     */
    @Transactional
    public static Content getPreviousContent(Content content, String dataSourceType) {
        StringBuilder sql = null;
        if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            sql = new StringBuilder("select * from C_Content where id=(select c.p from (select id,lag(id,1) over (order by releasetime desc,id desc) as p from C_Content");
            sql.append(" where audit1='1' and column_id = '").append(content.column_id).append("') c where c.id='");
            sql.append(content.id).append("')");
        }
        if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            sql = new StringBuilder("select * from C_Content where unix_timestamp(releasetime)>unix_timestamp('");
            sql.append(content.releasetime);
            sql.append("') order by releasetime asc limit 1");
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        List<SqlRow> rowList = sqlQuery.findList();
        Content previousContent = new Content();
        if (ObjectUtil.isNotNull(rowList)) {
            previousContent.id = rowList.get(0).getString("id");
            previousContent = getObject(previousContent.id, dataSourceType);
        }
        return previousContent;
    }

    /**
     * 查询文章列表中当前文章的下一篇文章
     *
     * @param content
     */
    @Transactional
    public static Content getNextContent(Content content, String dataSourceType) {
        StringBuilder sql = null;
        if (Constant.DEFAULT_DATASOURCE.equals(dataSourceType)) {
            sql = new StringBuilder("select * from C_Content where id=(select c.p from (select id,lead(id,1) over (order by releasetime desc,id desc) as p from C_Content");
            sql.append(" where audit1='1' and column_id = '").append(content.column_id).append("') c where c.id='");
            sql.append(content.id).append("')");
        }
        if (Constant.MYSQL_DATASOURCE.equals(dataSourceType)) {
            sql = new StringBuilder("select * from C_Content where unix_timestamp(releasetime)<unix_timestamp('");
            sql.append(content.releasetime);
            sql.append("') order by releasetime desc limit 1");
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        List<SqlRow> rowList = sqlQuery.findList();
        Content nextContent = new Content();
        if (ObjectUtil.isNotNull(rowList)) {
            nextContent.id = rowList.get(0).getString("id");
            nextContent = getObject(nextContent.id, dataSourceType);
        }
        return nextContent;
    }

    /**
     * 删除文章并删除静态页
     *
     * @param
     */
    @Transactional
    public static void deleteContent(String ids,String user_id,String dataSourceType) {
        if (ObjectUtil.isNotNull(ids)) {
            String[] tempIds = ids.split(",");
            for (String id : tempIds) {
                Content content = new Content();
                content.id = id;
                content.del = Constant.DEL_YES;
                content.updateuser = user_id;
                content.updatetime = new Date();
                Ebean.getServer(dataSourceType).update(content);
                Config config=Config.getConfig(dataSourceType);
                String htmlPath=config.htmlpath;
                content=Content.getObject(content.id,dataSourceType);
                //Site site=Site.getSite(dataSourceType);
                File file=new File(htmlPath+"html/"+content.htmlpath);
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            }
        }
        //生成首页
        Column.generateColumn(false, false,dataSourceType);
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

    public String getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getHttpurl() {
        return httpurl;
    }

    public void setHttpurl(String httpurl) {
        this.httpurl = httpurl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHtmlpath() {
        return htmlpath;
    }

    public void setHtmlpath(String htmlpath) {
        this.htmlpath = htmlpath;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(Date releasetime) {
        this.releasetime = releasetime;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getAudituser() {
        return audituser;
    }

    public void setAudituser(String audituser) {
        this.audituser = audituser;
    }

    public Date getAudittime() {
        return audittime;
    }

    public void setAudittime(Date audittime) {
        this.audittime = audittime;
    }

    public String getAuditopinion() {
        return auditopinion;
    }

    public void setAuditopinion(String auditopinion) {
        this.auditopinion = auditopinion;
    }

    public String getStick() {
        return stick;
    }

    public void setStick(String stick) {
        this.stick = stick;
    }

    public Integer getReadsize() {
        return readsize;
    }

    public void setReadsize(Integer readsize) {
        this.readsize = readsize;
    }

    public Integer getCollectsize() {
        return collectsize;
    }

    public void setCollectsize(Integer collectsize) {
        this.collectsize = collectsize;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    public List<SqlRow> getImageList() {
        return imageList;
    }

    public void setImageList(List<SqlRow> imageList) {
        this.imageList = imageList;
    }

    public List<SqlRow> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<SqlRow> attachmentList) {
        this.attachmentList = attachmentList;
    }


}
