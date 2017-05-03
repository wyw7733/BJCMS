package controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.avaje.ebean.SqlRow;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import models.*;
import org.springframework.beans.BeanUtils;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import util.*;
import views.html.contentManage.contentManage.columnTree;
import views.html.contentManage.contentManage.contentAdd;
import views.html.contentManage.contentManage.contentManage;
import views.html.contentManage.contentManage.contentModify;
import views.html.contentManage.auditManage.auditColumnTree;
import views.html.contentManage.auditManage.contentDetail;
import views.html.contentManage.auditManage.auditManage;
import views.html.contentManage.recycleBinManage.recycleBinManage;
import views.html.countManager.contentCount.contentCountIndex;
import views.html.countManager.contentCount.contentCount;
import views.html.countManager.workloadCount.workloadCountIndex;
import views.html.countManager.workloadCount.workloadCount;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * Created by houyaohui on 2016/1/19.
 */
@Security.Authenticated(SecureAdmin.class)
public class ContentAction extends Controller {

    /**
     * 返回文章管理栏目列表
     */
    public static Result contentColumnTree() {

        return ok(columnTree.render());
    }

    /**
     * 返回审核管理栏目列表
     */
    public static Result auditColumnTree() {

        return ok(auditColumnTree.render());
    }



    /**
     * 修改访问量
     *
     * @return
     */
    public static Result contentReadsSizeMosify() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        String id = form.data().get("id");
        String column_id = form.data().get("column_id");
        Content content=new Content();
        String readsSize = "0";
        if(ObjectUtil.isNotNull(id)){
            content = Content.getObject(id,ConfigUtil.getApplication("currentDataSource"));
            if(ObjectUtil.isNull(column_id)){
                column_id=content.column_id;
            }
            if(ObjectUtil.isNotNull(content)) {
                int size=0;
                if(ObjectUtil.isNotNull(content.readsize)){
                    size = content.readsize.intValue();
                }
                Content content1 = new Content();
                content1.id = id;
                if(!content.audit.equals(Constant.AUDIT_PASS)){
                    size=0;
                }else{
                    size ++;
                }
                content1.readsize = size;
                Content.upadte(content1,ConfigUtil.getApplication("currentDataSource"));
                readsSize = size + "";
            }
        }
        Column column = Column.getObject(column_id,ConfigUtil.getApplication("currentDataSource"));
        String code = column.code.substring(0, 3);
        Map<String, String> searchMap = new HashMap<String, String>();
        searchMap.put("codeOne", code);
        List<SqlRow> sqlRowList = Column.selectListByMap(searchMap,ConfigUtil.getApplication("currentDataSource"));
        String level = "";
        Site site = Site.getSite(ConfigUtil.getApplication("currentDataSource"));
        if(sqlRowList.size() > 0) {
            SqlRow sqlRow = sqlRowList.get(0);
            level = ",href"+site.dns+"html/"+code+"/"+code+".html,columnName"+sqlRow.getString("name");
        }
        //生成和首页
        Column.generateColumn(false, false, ConfigUtil.getApplication("currentDataSource"));
        return ok(callbackparam + "([{message:'"+readsSize+level+"'}])");
    }

    /**
     * 查询文章列表
     *
     * @return
     */
    public static Result contentList() {
        Map<String, String> searchMap = Maps.newHashMap();
        searchMap.put("del", Constant.DEL_NO);
        DynamicForm form = Form.form().bindFromRequest();
        String column_id = form.data().get("column_id");
        if (ObjectUtil.isNotNull(column_id)) {
            Column column = Column.getObject(column_id,ConfigUtil.getApplication("currentDataSource"));
            String dept_id=session("dept_id");
            Dept dept=Dept.getObject(dept_id,ConfigUtil.getApplication("currentDataSource"));
            if(!Constant.ISSUPPER_NO.equals(dept.issupper)){
                String column_ids = getColumnIds();
                if("".equals(column_ids) || !column_ids.contains(column.id)) {
                    List<SqlRow> contentList1 = new ArrayList<SqlRow>();
                    Map<String, String> authMap = Maps.newHashMap();
                    setAuthMap(authMap);
                    return ok(contentManage.render(contentList1, searchMap, authMap));
                }
            }
            if (column == null) {
                return ok();
            }
            searchMap.put("code", column.code + "%");
        } else {
            String column_ids = getColumnIds();
            if(column_ids != null && !"".equals(column_ids)) {
                searchMap.put("column_ids", column_ids);
            }
            if("".equals(column_ids)) {
                searchMap.put("column_ids", "nocolumn_id");
            }

        }
        Map<String, String> authMap = Maps.newHashMap();
        setAuthMap(authMap);
        searchMap.put("isPage", "true");
        List<SqlRow> contentList = Content.selectList(searchMap, ConfigUtil.getApplication("currentDataSource"));
        return ok(contentManage.render(contentList, searchMap, authMap));
    }
    /**
     * 查询当前部门能看到的栏目
     *
     * @return
     */
    private static String getColumnIds() {
        String dept_id=session("dept_id");
        Dept dept=Dept.getObject(dept_id,ConfigUtil.getApplication("currentDataSource"));
        Map<String,String> searchMap=new HashMap<>();
        if(!Constant.ISSUPPER_NO.equals(dept.issupper)){
            searchMap.put("dept_id", dept_id);
            List<SqlRow> columnList=Column.selectListByMap(searchMap,ConfigUtil.getApplication("currentDataSource"));
            searchMap.remove("dept_id");
            String column_ids="";
            for (int i=0;i<columnList.size();i++){
                SqlRow sqlRow=columnList.get(i);
                column_ids+=sqlRow.getString("id");
                if(i<columnList.size()-1){
                    column_ids+=",";
                }
            }
            return column_ids;
        }else{
            return null;
        }

    }

    /**
     * 分页查询文章列表
     *
     * @return
     */
    public static Result contentListToJson() {
        Map<String, String> searchMap = Maps.newHashMap();
        searchMap.put("del", Constant.DEL_NO);
        DynamicForm form = Form.form().bindFromRequest();
        String column_id = form.data().get("column_id");
        if (ObjectUtil.isNotNull(column_id)) {
            Column column = Column.getObject(column_id,ConfigUtil.getApplication("currentDataSource"));
            String dept_id=session("dept_id");
            Dept dept=Dept.getObject(dept_id,ConfigUtil.getApplication("currentDataSource"));
            if(!Constant.ISSUPPER_NO.equals(dept.issupper)){
                String column_ids = getColumnIds();
                if("".equals(column_ids) || !column_ids.contains(column.id)) {
                    List<SqlRow> contentList1 = new ArrayList<SqlRow>();
                    Map<String, String> authMap = Maps.newHashMap();
                    setAuthMap(authMap);
                    return ok(contentManage.render(contentList1, searchMap, authMap));
                }
            }
            if (column == null) {
                return ok();
            }
            searchMap.put("code", column.code + "%");
        } else {
            String column_ids = getColumnIds();
            if(column_ids != null && !"".equals(column_ids)) {
                searchMap.put("column_ids", column_ids);
            }
            if("".equals(column_ids)) {
                searchMap.put("column_ids", "nocolumn_id");
            }

        }
        //分页参数
        //当前第几页
        String page = form.data().get("page");
        //每页行数
        String pageSize = form.data().get("pageSize");
        searchMap.put("isPage", "true");
        searchMap.put("page", page);
        searchMap.put("pageSize", pageSize);
        searchMap.put("likeTitle", form.data().get("likeTitle"));
        searchMap.put("likeReleasetime", form.data().get("likeReleasetime"));
        searchMap.put("audit", form.data().get("audit"));
        searchMap.put("stick", form.data().get("stick"));
        Map<String, String> authMap = Maps.newHashMap();
        setAuthMap(authMap);
        List<SqlRow> contentList = Content.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        Map<String, Object> result = Maps.newHashMap();
        result.put("contentList", contentList);
        result.put("searchMap", searchMap);
        result.put("authMap", authMap);
        SerializeConfig config = new SerializeConfig();
        config.put(java.sql.Date.class, new SimpleDateFormatSerializer(
                "yyyy-MM-dd"));
        return ok(JSONArray.toJSONString(result, config));
    }

    /**
     * 跳转至文章添加页面
     *
     * @return
     */
    public static Result toContentAdd() {
        DynamicForm form = Form.form().bindFromRequest();
        String id = form.data().get("id");
        //获取选中的列
        Column column = null;
        String type_id = "";
        String mobilecontent_id = "";
        if (ObjectUtil.isNotNull(id)) {
            Column column1 = Column.getObject(id,ConfigUtil.getApplication("currentDataSource"));
            String dept_id=session("dept_id");
            Dept dept=Dept.getObject(dept_id,ConfigUtil.getApplication("currentDataSource"));
            if(!Constant.ISSUPPER_NO.equals(dept.issupper)){
                String column_ids = getColumnIds();
                if("".equals(column_ids) || !column_ids.contains(column1.id)) {
                    id = null;
                } else {
                    //根据id查询选中栏目
                    column = Column.getObject(id,ConfigUtil.getApplication("currentDataSource"));
                    mobilecontent_id = column.mobilecontent_id;
                    type_id=column.type_id;
                }
            } else {
                //根据id查询选中栏目
                column = Column.getObject(id,ConfigUtil.getApplication("currentDataSource"));
                mobilecontent_id = column.mobilecontent_id;
                type_id=column.type_id;
            }


        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("del", Constant.DEL_NO);
        //查询栏目类别
        List<ColumnType> typeList = ColumnType.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        for (ColumnType type : typeList) {
            map.put("type_id", type.id);
            type.columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        }
        //查询模版类型（内容模版）
        map.put("type_id", Constant.MOBILETYPE_CONTENT);
        List<Mobile> mobileList = Mobile.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        //查询文章来源
        List<ArticleSource> articleSourceList = ArticleSource.selectList(ConfigUtil.getApplication("currentDataSource"));
        return ok(contentAdd.render(id, typeList, mobileList, type_id, mobilecontent_id, articleSourceList));
    }

    /**
     * 查询栏目树
     *
     * @return
     */
    public static Result selectTree() {
        String str = Column.selectTree(session("dept_id"),ConfigUtil.getApplication("currentDataSource"));
        return ok(Json.parse(str));
    }

    /**
     * 添加文章
     *
     * @return
     */
    public static Result add() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        String result= Content.insertContent(body, form, session("id"), session("site_id"),ConfigUtil.getApplication("currentDataSource"));
        return ok(result);
    }

    /**
     * 删除文章
     *
     * @return
     */
/*    public static Result delete() {
        Map<String, String> map = Form.form().bindFromRequest().data();
        String ids = map.get("ids");
        if (ObjectUtil.isNotNull(ids)) {
            String[] tempIds = ids.split(",");
            for (String id : tempIds) {
                Content content = new Content();
                content.id = id;
                content.del = Constant.DEL_YES;
                content.updateuser = session("id");
                content.updatetime = new Date();
                Content.upadte(content,ConfigUtil.getApplication("currentDataSource"));
            }
        }
        return ok("删除成功");
    }*/

    /**
     * 删除文章
     *
     * @return
     */
    public static Result delete() {
        Map<String, String> map = Form.form().bindFromRequest().data();
        String ids = map.get("ids");
        Content.deleteContent(ids,session("id"),ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功");
    }

    /**
     * 跳转到修改文章页面
     *
     * @return
     */
    public static Result toModify() {
        DynamicForm form = Form.form().bindFromRequest();
        String id = form.data().get("id");
        //获取选中的列
        Column column = null;
        String type_id = "";
        //查询文章对象
        Content content = Content.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        if (ObjectUtil.isNotNull(id)) {
            //根据id查询选中栏目
            column = Column.getObject(content.column_id,ConfigUtil.getApplication("currentDataSource"));
            //根据栏目type_id查询栏目类别code
            ColumnType columnType = ColumnType.getObject(column.type_id,ConfigUtil.getApplication("currentDataSource"));
            type_id=column.type_id;
            Map<String, Object> map = Maps.newHashMap();
            map.put("del", Constant.DEL_NO);
            //查询栏目类别
            List<ColumnType> typeList = ColumnType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
            for (ColumnType type : typeList) {
                map.put("type_id", type.id);
                type.columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
            }
            //查询模版类型（内容模版）
            map.put("type_id", Constant.MOBILETYPE_CONTENT);
            List<Mobile> mobileList = Mobile.selectList(map,ConfigUtil.getApplication("currentDataSource"));
            //如果类型是图库（code=003）则需要查询图文信息
            if (Constant.COLUMN_TYPE_PHOTO.equals(columnType.id)) {
                Map<String, String> searchMap = Maps.newHashMap();
                //searchMap.put("del", Constant.DEL_NO);
                searchMap.put("content_id", content.id);
                List<SqlRow> imagetList = ContentImages.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
                content.imageList = imagetList;
            }
            //查询文章来源
            List<ArticleSource> articleSourceList = ArticleSource.selectList(ConfigUtil.getApplication("currentDataSource"));
            //查询站点信息（域名）
            Site site = Site.getSite(ConfigUtil.getApplication("currentDataSource"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String releasetime = "";
            if(ObjectUtil.isNotNull(content.releasetime)) {
                releasetime = sdf.format(content.releasetime);
            }
            if (ObjectUtil.isNotNull(site)) {
                content.content1=content.content1.replace("\r","");
                content.content1=content.content1.replace("\n","");
                return ok(contentModify.render(content, typeList, mobileList, type_id, site, column.mobilecontent_id, articleSourceList, releasetime));
            }
        }
        return badRequest();
    }
    /**
     * 跳转到修改文章页面
     *
     * @return
     */
    public static Result toDetail() {
        DynamicForm form = Form.form().bindFromRequest();
        String id = form.data().get("id");
        //获取选中的列
        Column column = null;
        String type_id = "";
        //查询文章对象
        Content content = Content.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        if (ObjectUtil.isNotNull(id)) {
            //根据id查询选中栏目
            column = Column.getObject(content.column_id,ConfigUtil.getApplication("currentDataSource"));
            //根据栏目type_id查询栏目类别code
            ColumnType columnType = ColumnType.getObject(column.type_id,ConfigUtil.getApplication("currentDataSource"));
            type_id=column.type_id;
            Map<String, Object> map = Maps.newHashMap();
            map.put("del", Constant.DEL_NO);
            //查询栏目类别
            List<ColumnType> typeList = ColumnType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
            for (ColumnType type : typeList) {
                map.put("type_id", type.id);
                type.columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
            }
            //查询模版类型（内容模版）
            map.put("type_id", Constant.MOBILETYPE_CONTENT);
            List<Mobile> mobileList = Mobile.selectList(map,ConfigUtil.getApplication("currentDataSource"));
            //如果类型是图库（code=003）则需要查询图文信息
            if (Constant.COLUMN_TYPE_PHOTO.equals(columnType.id)) {
                Map<String, String> searchMap = Maps.newHashMap();
                //searchMap.put("del", Constant.DEL_NO);
                searchMap.put("content_id", content.id);
                List<SqlRow> imagetList = ContentImages.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
                content.imageList = imagetList;
            }
            //查询文章来源
            List<ArticleSource> articleSourceList = ArticleSource.selectList(ConfigUtil.getApplication("currentDataSource"));
            //查询站点信息（域名）
            Site site = Site.getSite(ConfigUtil.getApplication("currentDataSource"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String releasetime = "";
            if(ObjectUtil.isNotNull(content.releasetime)) {
                releasetime = sdf.format(content.releasetime);
            }
            if (ObjectUtil.isNotNull(site)) {
                return ok(contentDetail.render(content, typeList, mobileList, type_id, site, column.mobilecontent_id, articleSourceList, releasetime));
            }
        }
        return badRequest();
    }


    /**
     * 修改文章
     *
     * @return
     */
    public static Result modify() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Content.updateContent(body, form, session("id"), session("site_id"),ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功");
    }

    /**
     * 置顶、取消置顶、审核、取消审核等操作
     *
     * @return
     */
    public static Result otherModify() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Content content = form.get();
        String ids = form.data().get("ids");
        //批量修改
        if (ObjectUtil.isNotNull(ids)) {
            for (String id : ids.split(",")) {
                Content temp = new Content();
                BeanUtils.copyProperties(content, temp);
                temp.id = id;
                temp.updatetime = new Date();
                Content.upadte(temp,ConfigUtil.getApplication("currentDataSource"));
            }
        }
        return ok("修改成功");
    }

    /**
     * 搜索
     *
     * @return
     */
    public static Result search() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Content content = form.get();
        Map<String, String> searchMap = Maps.newHashMap();
        //标题
        if (ObjectUtil.isNotNull(content.title) && content.title.trim().length() > 0) {
            searchMap.put("likeTitle", content.title);
        }
        //审核类型
        if (ObjectUtil.isNotNull(content.audit) && content.audit.trim().length() > 0) {
            searchMap.put("audit", content.audit);
        }
        //查询发布时间年月日,不包含时分秒
        if (ObjectUtil.isNotNull(content.releasetime) && content.releasetime.toString().trim().length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            searchMap.put("likeReleasetime", sdf.format(content.releasetime));
        }
        //推荐
        if (ObjectUtil.isNotNull(content.stick) && content.stick.trim().length() > 0) {
            searchMap.put("stick", content.stick);
        }
        //查询没有删除的数据
        searchMap.put("del", Constant.DEL_NO);
        //根据条件查询数据
        searchMap.put("isPage", "true");
        String column_id = form.data().get("column_id");
        if (ObjectUtil.isNotNull(column_id)) {
            Column column = Column.getObject(column_id,ConfigUtil.getApplication("currentDataSource"));
            String dept_id=session("dept_id");
            Dept dept=Dept.getObject(dept_id,ConfigUtil.getApplication("currentDataSource"));
            if(!Constant.ISSUPPER_NO.equals(dept.issupper)){
                String column_ids = getColumnIds();
                if("".equals(column_ids) || !column_ids.contains(column.id)) {
                    List<SqlRow> contentList1 = new ArrayList<SqlRow>();
                    Map<String, String> authMap = Maps.newHashMap();
                    setAuthMap(authMap);
                    return ok(contentManage.render(contentList1, searchMap, authMap));
                }
            }
            if (column == null) {
                return ok();
            }
            searchMap.put("code", column.code + "%");
        } else {
            String column_ids = getColumnIds();
            if(column_ids != null && !"".equals(column_ids)) {
                searchMap.put("column_ids", column_ids);
            }
            if("".equals(column_ids)) {
                searchMap.put("column_ids", "nocolumn_id");
            }

        }
        List<SqlRow> contentList = Content.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        Map<String, String> authMap = Maps.newHashMap();
        //设置内容增删改权限
        setAuthMap(authMap);
        return ok(contentManage.render(contentList, searchMap, authMap));
    }

    /**
     * 设置增删改权限
     *
     * @return
     */
    public static void setAuthMap(Map<String, String> authMap) {
        if (ObjectUtil.isNotNull(session("contentAdd"))){//新增
            authMap.put("contentAdd", session("contentAdd"));
        }
        if (ObjectUtil.isNotNull(session("contentUpdate"))){//修改
            authMap.put("contentUpdate", session("contentUpdate"));
        }
        if (ObjectUtil.isNotNull(session("contentDelete"))){//删除
            authMap.put("contentDelete",session("contentUpdate"));
        }
    }

    //上传图片插件
   /* public static Result fileUpload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> filePartList = body.getFiles();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        //保存文件路径
        String attachmentpath = "";
        String attachmentname = "";
        for (Http.MultipartFormData.FilePart filePart : filePartList) {
            String path = saveFile(filePart);
            attachmentpath += path;
            attachmentname += filePart.getFilename();
        }
        node.put("attachmentpath", attachmentpath);
        node.put("attachmentname", attachmentname);
        String result = "";
        try {
            result = mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ok(result);
    }
*/
    /**
     * 插件上传图片、批量上传
     *
     * @return
     */
   /* public static Result imgeUpload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> filePartList = body.getFiles();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        List<Config> configList = Config.selectList(new HashMap<String, Object>());
        if (ObjectUtil.isNull(configList)) {
            return ok();
        }
        Config config = configList.get(0);
        //获取域名路径
        List<Site> siteList = Site.selectList(new HashMap<String, Object>());
        if (ObjectUtil.isNull(siteList)) {
            throw new RuntimeException("站点设置不能为空");
        }

        for (Http.MultipartFormData.FilePart filePart : filePartList) {
            String path = saveFile(filePart);
            node.put("error", 0);
            path = path.substring(path.indexOf(config.htmlpath) + config.htmlpath.length(), path.length());


            node.put("url", siteList.get(0).dns + path);
        }
        String result = "";
        try {
            result = mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ok(result);
    }
*/

    /**
     * 保存文件
     *
     * @param filePart
     */
   /* private static String saveFile(Http.MultipartFormData.FilePart filePart) {
        //查询配置
        List<Config> configList = Config.selectList(new HashMap<String, Object>());
        String path = "";
        if (ObjectUtil.isNotNull(configList)) {
            Config config = configList.get(0);
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
                if ("file".equals(filePart.getKey())) {
                    dirName = "photos";
                }
                File sf = new File(config.htmlpath + "/" + dirName);
                if (!sf.exists()) {
                    sf.mkdirs();
                }
                String prefix = fileName.substring(fileName.lastIndexOf("."));
                path = sf.getPath() + "\\" + UUIDUtil.getUUID() + prefix;
                OutputStream os = new FileOutputStream(path);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }*/

    /**
     * 阿帕奇访问获取文章返回 json
     *
     * @return
     */
    public static Result selectListToJson() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        Map<String, String> searchMap = Maps.newHashMap();
        //查询内容(未删除。审核通过)
        searchMap.put("audit", Constant.AUDIT_PASS);
        searchMap.put("del", Constant.DEL_NO);
        String column_id = form.data().get("column_id");
        if (ObjectUtil.isNotNull(column_id)) {
            searchMap.put("column_id", column_id);
        }
        List<SqlRow> sqlRowList = Content.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        return ok(callbackparam + "(" + Json.toJson(sqlRowList) + ")");
    }

    /**
     * 查询未审核列表并返回审核管理页面
     *
     * @return
     */
    public static Result auditContentList() {
        Map<String, String> searchMap = Maps.newHashMap();
        searchMap.put("del", Constant.DEL_NO);
        DynamicForm form = Form.form().bindFromRequest();
        String column_id = form.data().get("column_id");
        String audit = Constant.AUDIT_NOT;
        searchMap.put("audit", audit);
        if (ObjectUtil.isNotNull(column_id)) {
            Column column = Column.getObject(column_id,ConfigUtil.getApplication("currentDataSource"));
            String dept_id=session("dept_id");
            Dept dept=Dept.getObject(dept_id,ConfigUtil.getApplication("currentDataSource"));
            if(!Constant.ISSUPPER_NO.equals(dept.issupper)){
                String column_ids = getColumnIds();
                if("".equals(column_ids) || !column_ids.contains(column.id)) {
                    List<SqlRow> contentList1 = new ArrayList<SqlRow>();
                    Map<String, String> authMap = Maps.newHashMap();
                    setAuthMap(authMap);
                    return ok(auditManage.render(contentList1, searchMap));
                }
            }
            if (column == null) {
                return ok();
            }
            searchMap.put("code", column.code + "%");
        } else {
            String column_ids = getColumnIds();
            if(column_ids != null && !"".equals(column_ids)) {
                searchMap.put("column_ids", column_ids);
            }
            if("".equals(column_ids)) {
                searchMap.put("column_ids", "nocolumn_id");
            }
        }
        if (ObjectUtil.isNotNull(form.data().get("audit"))) {
            audit = form.data().get("audit");
            searchMap.put("audit", audit);
        }
        searchMap.put("isPage", "true");
        List<SqlRow> contentList = Content.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        return ok(auditManage.render(contentList, searchMap));
    }
    /**
     * 条件查询文章列表且返回到审核页面
     *
     * @return
     */
    public static Result searchAuditContent() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Content content = form.get();
        String releasetime=form.data().get("releasetime");
        Map<String, String> searchMap = Maps.newHashMap();
        String audit = Constant.AUDIT_NOT;
        searchMap.put("audit", audit);
        //   searchMap.put("audit", Constant.AUDIT_NOT);
        //标题
        if (ObjectUtil.isNotNull(content.title) && content.title.trim().length() > 0) {
            searchMap.put("likeTitle", content.title);
        }
        //审核类型
        if (ObjectUtil.isNotNull(content.audit) && content.audit.trim().length() > 0) {
            searchMap.put("audit", content.audit);
        }
        if (ObjectUtil.isNull(content.audit)){
            searchMap.put("audit", "");
        }
        //查询发布时间年月日,不包含时分秒
        if (ObjectUtil.isNotNull(releasetime) && releasetime.trim().length() > 0) {
            searchMap.put("likeReleasetime", releasetime);
        }
        //推荐状态
        if (ObjectUtil.isNotNull(content.stick) && content.stick.trim().length() > 0) {
            searchMap.put("stick", content.stick);
        }
        //根据条件查询数据
        searchMap.put("isPage", "true");
        //查询没有删除的数据
        searchMap.put("del", Constant.DEL_NO);
        String column_id = form.data().get("column_id");
        if (ObjectUtil.isNotNull(column_id)) {
            Column column = Column.getObject(column_id,ConfigUtil.getApplication("currentDataSource"));
            String dept_id=session("dept_id");
            Dept dept=Dept.getObject(dept_id, ConfigUtil.getApplication("currentDataSource"));
            if(!Constant.ISSUPPER_NO.equals(dept.issupper)){
                String column_ids = getColumnIds();
                if("".equals(column_ids) || !column_ids.contains(column.id)) {
                    List<SqlRow> contentList1 = new ArrayList<SqlRow>();
                    Map<String, String> authMap = Maps.newHashMap();
                    setAuthMap(authMap);
                    return ok(contentManage.render(contentList1, searchMap, authMap));
                }
            }
            if (column == null) {
                return ok();
            }
            searchMap.put("code", column.code + "%");
        } else {
            String column_ids = getColumnIds();
            if(column_ids != null && !"".equals(column_ids)) {
                searchMap.put("column_ids", column_ids);
            }
            if("".equals(column_ids)) {
                searchMap.put("column_ids", "nocolumn_id");
            }

        }
        //根据条件查询数据
        List<SqlRow> contentList = Content.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        return ok(auditManage.render(contentList, searchMap));
    }



    /**
     * 审核单个文章未通过
     *
     * @return
     */
    public static Result auditContentUnPass() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Content content = form.get();
        //设置审核人
        content.audituser = session("id");
        //设置审核时间
        content.audittime = new Date();
        //设置更新人
        content.updateuser = session("id");
        //设置更新时间
        content.updatetime = new Date();
        Column.generateColumn(false, false,ConfigUtil.getApplication("currentDataSource"));
        Config config = new Config();
        config = Config.getObject(ConfigUtil.getApplication("currentDataSource"));
        Content content1 = Content.getObject(content.id,ConfigUtil.getApplication("currentDataSource"));
        Content previousContent = Content.getPreviousContent(content1,ConfigUtil.getApplication("currentDataSource"));
        Content nextContent = Content.getNextContent(content1, ConfigUtil.getApplication("currentDataSource"));
        Content.auditConten(content, ConfigUtil.getApplication("currentDataSource"));
        if (ObjectUtil.isNotNull(previousContent.id)) {
            Content.generateContent(previousContent, config, "", "", true, false,ConfigUtil.getApplication("currentDataSource"));
        }
        if (ObjectUtil.isNotNull(nextContent.id)) {
            Content.generateContent(nextContent, config, "", "", true, false,ConfigUtil.getApplication("currentDataSource"));
        }
        return ok("取消审核成功！");
    }

    /**
     * 重新发布定时器 生成页面
     *
     * @return
     */
    public static Result reStartTimer() {
        Map<String, String> searchMap = Maps.newHashMap();
        searchMap.put("audit", "1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        searchMap.put("restartReleasetime", sdf.format(new Date()));
        List<SqlRow> contentList = Content.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        for (SqlRow sqlRow : contentList) {
            final Content content = Content.getObject(sqlRow.getString("id"),ConfigUtil.getApplication("currentDataSource"));
            if(ObjectUtil.isNotNull(content)) {
                final Config config = Config.getConfig(ConfigUtil.getApplication("currentDataSource"));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        boolean flag = Content.generateContent(content, config, "", "", true, false,ConfigUtil.getApplication("currentDataSource"));
                        if(flag) {
                            BusinessException businessException = new BusinessException();
                            businessException.id = UUIDUtil.getUUID();
                            businessException.name = "添加成功";
                            businessException.is_success = "1";
                            businessException.updatetime = new Date();
                            businessException.content_id = content.id;
                            BusinessException.insert(businessException,ConfigUtil.getApplication("currentDataSource"));
                        }
                    }
                }, content.releasetime);
            }
        }
        return ok("审核成功！");
    }


    /**
     * 批量审核文章
     *
     * @return
     */
    public static Result auditContents() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Content content = form.get();
        //设置审核人
        content.audituser = session("id");
        //设置审核时间
        content.audittime = new Date();
        //设置更新人
        content.updateuser = session("id");
        //设置更新时间
        content.updatetime = new Date();
        String ids = form.data().get("ids");
        List<Content> previousContentList = Lists.newArrayList();
        List<Content> nextContentList = Lists.newArrayList();
        if (Constant.AUDIT_NOTPASS.equals(content.audit)) {
            for (String id : ids.split(",")) {
                Content content1 = Content.getObject(id,ConfigUtil.getApplication("currentDataSource"));
                previousContentList.add(Content.getPreviousContent(content1,ConfigUtil.getApplication("currentDataSource")));
                nextContentList.add(Content.getNextContent(content1,ConfigUtil.getApplication("currentDataSource")));
            }
        }
        //批量审核
        Content.auditContens(ids, content,ConfigUtil.getApplication("currentDataSource"));
        Config config = new Config();
        config = Config.getObject(ConfigUtil.getApplication("currentDataSource"));
        if (Constant.AUDIT_NOTPASS.equals(content.audit)) {
            for (int i = 0; i < previousContentList.size(); i++) {
                if (ObjectUtil.isNotNull(previousContentList.get(i).id)) {
                    Content.generateContent(previousContentList.get(i), config, "", "", true, false,ConfigUtil.getApplication("currentDataSource"));
                }
                if (ObjectUtil.isNotNull(nextContentList.get(i).id)) {
                    Content.generateContent(nextContentList.get(i), config, "", "", true, false,ConfigUtil.getApplication("currentDataSource"));
                }
            }
        }
        String info = "";
        if (Constant.AUDIT_PASS.equals(content.audit)) {
            info = "审核成功！";
        } else {
            info = "取消审核成功！";
        }
        return ok(info);
    }


    /**
     * 查询已删除文章列表
     *
     * @return
     */
    public static Result recycleBin() {
        Map<String, String> searchMap = Maps.newHashMap();
        searchMap.put("del", Constant.DEL_YES);
        List<SqlRow> contentList = Content.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        return ok(recycleBinManage.render(contentList));
    }

    /**
     * 恢复已删除文章
     *
     * @return
     */
    public static Result recovery() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Content content = form.get();
        content.updatetime = new Date();
        content.updateuser = session("id");
        String ids = form.data().get("ids");
        Content.recovery(ids, content,ConfigUtil.getApplication("currentDataSource"));
        return ok("恢复成功！");
    }

    /**
     * 内容统计初始页面
     *
     * @return
     */
    public static Result contentCountIndex() {
        Map<String, Object> map = new HashMap<>();
        map.put("del", Constant.DEL_NO);
        List<Column> columnList = Column.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        return ok(contentCountIndex.render(columnList, DateUtil.yearList()));
    }

    /**
     * 初始统计内容
     *
     * @return
     */
    public static Result countContentList() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        String year = form.data().get("year");
        String column_id = form.data().get("column_id");
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("year", DateUtil.nowYear());
        searchMap.put("column_id", column_id);
        if (ObjectUtil.isNotNull(year)) {
            searchMap.put("year", year);
        }
        List<SqlRow> countList = Content.countContentList(searchMap, ConfigUtil.getApplication("currentDataSource"));

        return ok(contentCount.render(countList));
    }

    /**
     * 工作量统计初始页面
     *
     * @return
     */
    public static Result workloadCountIndex() {
        Map<String, Object> map = new HashMap<>();
        map.put("del", Constant.DEL_NO);
        List<Column> columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        map.remove("del");
        map.put("audit", Constant.AUDIT_NORMAL);
        List<AdminUser> userList = AdminUser.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        return ok(workloadCountIndex.render(columnList, userList, DateUtil.yearList()));
    }

    /**
     * 工作量统计
     *
     * @return
     */
    public static Result workloadContentList() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Map<String, String> searhMap = form.data();
        List<WorkCount> countList = Content.workloadContentList(searhMap, ConfigUtil.getApplication("currentDataSource"));
        return ok(workloadCount.render(countList));
    }

    /**
     * 根据栏目联动显示文章
     *
     * @return
     */
    public static Result linkageContentList() {
        DynamicForm form = Form.form().bindFromRequest();
        Map<String, String> searchMap = form.data();
        List<SqlRow> contentList = Content.selectList(searchMap, ConfigUtil.getApplication("currentDataSource"));
        return ok(JSON.toJSONString(contentList));
    }

    /**
     * 预览文章
     *
     * @return path
     */
    public static Result contentShow() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        String path = Content.showContent(body, form, session("id"), ConfigUtil.getApplication("currentDataSource"));
        return ok(path);
    }
    /**
     * 根据id预览文章
     *
     * @return path
     */
    public static Result previewContent() {
        Form<Content> form = Form.form(Content.class).bindFromRequest();
        Content content=Content.getObject(form.data().get("id"), ConfigUtil.getApplication("currentDataSource"));
        String path=Content.showContentByContent(content, ConfigUtil.getApplication("currentDataSource"));
        return ok(path);
    }


    /**
     * 前台阿帕奇访问分页
     *
     * @return
     */
    public static Result selectContentByPage() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        String pageNum = form.data().get("pageNum");
        String pageSize=form.data().get("pageSize");
        String keywords=form.data().get("keywords");
        String excludeSql=form.data().get("excludeSql");
        Map<String, String> searchMap = Maps.newHashMap();
        searchMap.put("pageNum", pageNum);
        searchMap.put("pageSize", pageSize);
        searchMap.put("keywords", keywords);
        searchMap.put("excludeSql", excludeSql);
        //查询内容(未删除。审核通过)
        searchMap.put("audit", Constant.AUDIT_PASS);
        searchMap.put("del", Constant.DEL_NO);
        String column_id = form.data().get("column_id");
        if (ObjectUtil.isNotNull(column_id)) {
            Column column = Column.getObject(column_id,ConfigUtil.getApplication("currentDataSource"));
            if (column == null) {
                return ok();
            }
            searchMap.put("code", column.code + "%");
        }
        Page page = Content.selectListByPage(searchMap,ConfigUtil.getApplication("currentDataSource"));
        SerializeConfig config = new SerializeConfig();
        config.put(java.sql.Date.class, new SimpleDateFormatSerializer(
                "yyyy-MM-dd"));
        return ok(callbackparam + "(" + JSONArray.toJSONString(page, config) + ")");
    }
    /**
     * 网站前台附件下载
     * @param
     * @return
     */
    public static Result conAccessorysDownloadWebsite() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        String id = form.data().get("id");
        HashMap<String, String> searchMap = new HashMap();
        searchMap.put("content_id", id);
        List<SqlRow> attachments=ContentAttachment.selectList(searchMap, ConfigUtil.getApplication("currentDataSource"));
      /*  Config config = Config.getConfig(ConfigUtil.getApplication("currentDataSource"));
        String time = System.currentTimeMillis()+"";
        String pathDownload = config.htmlpath + "/download/"+time;
        File sf = new File(pathDownload);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        String zipDownload = config.htmlpath + "/download/zip/";
        File sf1 = new File(zipDownload);
        if (!sf1.exists()) {
            sf1.mkdirs();
        }
        SqlRow sqlRow = null;
        for(int i = 0; i < attachments.size(); i++) {
            sqlRow = attachments.get(i);
            String path = config.htmlpath + sqlRow.get("attachmentpath")+"";
            File file = new File(path);
            //String type = path.substring(path.lastIndexOf("."));
            //File file1 = new File(pathDownload+"/"+sqlRow.get("attachmentname")+type);
            File file1 = new File(pathDownload+"/"+sqlRow.get("attachmentname"));
            copy(file, file1);
        }

        String downloadZipPath = "";
        try {
            fileToZip(pathDownload, zipDownload + time + ".zip");
            downloadZipPath = "download/zip/" + time + ".zip";
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String downloadPath="";
        if(attachments.size()>0){
            downloadPath=attachments.get(0).getString("attachmentpath");
        }
        return ok(callbackparam + "(" + Json.toJson(downloadPath) + ")");
    }
    /**
     * 上传功能
     * */
    private static void copy(File src, File target) {
        InputStream in = null;
        OutputStream out = null;
        final int BUFFERED_SIZE = 4 * 1024;
        try {
            in = new BufferedInputStream(new FileInputStream(src),
                    BUFFERED_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(target),
                    BUFFERED_SIZE);
            byte[] bs = new byte[BUFFERED_SIZE];
            int i;
            while ((i = in.read(bs)) > 0) {
                out.write(bs, 0, i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void fileToZip(String inputFileName,String zipFileName) throws Exception {
        System.out.println("in :" + zipFileName);
        zip1(zipFileName, new File(inputFileName));
    }
    private static void zip1(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip2(out, inputFile, "");
        out.close();
    }

    private static void zip2(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip2(out, fl[i], base + fl[i].getName());
            }
        }else {
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ( (b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }
}
