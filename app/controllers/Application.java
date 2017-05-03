package controllers;

import com.avaje.ebean.SqlRow;
import models.*;
import models.Content;
import play.Play;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;

import util.ConfigUtil;
import util.Constant;
import util.ObjectUtil;
import util.Ueditor;
import views.html.*;

import java.io.File;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    /**
     * 前台展示
     *
     * @return
     */
    public static Result index() {
        return ok(aaa.render());
    }

    /**
     * 后台系统
     *
     * @return
     */
    public static Result admin() {
        session().remove("contentAdd");
        session().remove("contentUpdate");
        session().remove("contentDelete");
        //获取session中存储的id
        String id = session("id");
        //String id= (String)Cache.get("id");
        // if(ObjectUtil.isNull(ssionId)){
        //返回登录页面
        //    return ok(login.render(""));
        //  }
        //判断用户id是否为null
        if (ObjectUtil.isNotNull(id)) {
            //不为null,根据id获取用户信息
            AdminUser adminUser = AdminUser.getObject(id,ConfigUtil.getApplication("currentDataSource"));
            //查询角色权限
            List<Authority> authList = selectAuths(id);
            //查询站点信息
            Site site=new Site();
            Site tempsite=Site.getSite(ConfigUtil.getApplication("currentDataSource"));
            if(ObjectUtil.isNotNull(tempsite)){
                site=tempsite;
            }
            //查询最新文章
            List<SqlRow> contentNewest= Content.selectNewest(ConfigUtil.getApplication("currentDataSource"));
            Date date=(Date)Cache.get("lastlogintime");
            //返回主页
            return ok(index.render(site,adminUser, authList,contentNewest,session("lastlogintime"),session("lastloginip")));
        } else {
            //返回登录页面
            return ok(login.render(""));
        }
    }
    /**
     * 查询权限列表
     *
     * @param user_id
     * @return
     */
    public static List<Authority> selectAuths(String user_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        //根据用户id查询角色信息
        List<UserRole> userRoleList = UserRole.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        String role_id = "";
        if (userRoleList.size() > 0) {
            UserRole userRole = userRoleList.get(0);
            role_id = userRole.role_id;
        }
        Map<String, String> map1 = new HashMap<>();
        map1.put("role_id", role_id);
        //根据角色查询权限
        List<SqlRow> roleAuthList = RoleAuth.selectList(map1,ConfigUtil.getApplication("currentDataSource"));
        List<Authority> authList = new ArrayList<>();
        for (int i = 0; i < roleAuthList.size(); i++) {
            Authority authority = Authority.getObject((String) roleAuthList.get(i).get("authority_id"),ConfigUtil.getApplication("currentDataSource"));
            //将内容增删改权限保存至session
            saveContentAuth(authority);
            authList.add(authority);
        }
        return authList;
    }

    /**
     * 将内容增删改权限保存至session
     *
     * @return
     */
    public static void saveContentAuth(Authority authority) {
        if (authority.name.equals("新增")) {//增加
            session("contentAdd", "增加");
        }
        if (authority.name.equals("修改")) {//修改
            session("contentUpdate", "修改");
        }
        if (authority.name.equals("删除")) {//删除
            session("contentDelete", "删除");
        }
    }


    public static Result ueConfig() {

        return ok(Ueditor.config());
    }

    public static Result uePostAction(String url) {

        String result = "";

        String imageActionName = Play.application().configuration()
                .getString("ueditor_conf.imageActionName");
        String scrawlActionName = Play.application().configuration()
                .getString("ueditor_conf.scrawlActionName");
        String snapscreenActionName = Play.application().configuration()
                .getString("ueditor_conf.snapscreenActionName");
        String videoActionName = Play.application().configuration()
                .getString("ueditor_conf.videoActionName");
        String fileActionName = Play.application().configuration()
                .getString("ueditor_conf.fileActionName");
        String catchActionName = Play.application().configuration()
                .getString("ueditor_conf.catcherActionName");

        String imageManagerActionName = Play.application().configuration()
                .getString("ueditor_conf.imageManagerActionName");
        String fileManagerActionName = Play.application().configuration()
                .getString("ueditor_conf.fileManagerActionName");

        DynamicForm requestData = Form.form().bindFromRequest();

        if (url.equals(imageActionName) || url.equals(scrawlActionName)
                || url.equals(snapscreenActionName)
                || url.equals(videoActionName) || url.equals(fileActionName) || url.equals(catchActionName)) {

            //上传
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart upFile = null;

            String uploadType;
            String upContent = "";
            List<String> catchList = new ArrayList<String>();

            if (url.equals(imageActionName) || url.equals(snapscreenActionName)) {

                upFile = body.getFile(Play.application().configuration()
                        .getString("ueditor_conf.imageFieldName"));

                //图片
                uploadType = "image";

            } else if (url.equals(scrawlActionName)) {

                upContent = requestData.get(Play.application().configuration()
                        .getString("ueditor_conf.scrawlFieldName"));

                //涂鸦
                uploadType = "scrawl";

            } else if (url.equals(videoActionName)) {
                upFile = body.getFile(Play.application().configuration()
                        .getString("ueditor_conf.videoFieldName"));
                //视频
                uploadType = "video";
            } else if (url.equals(fileActionName)) {
                upFile = body.getFile(Play.application().configuration()
                        .getString("ueditor_conf.fileFieldName"));
                //文件
                uploadType = "file";
            } else {
                Map<String, String> datas = new HashMap<String, String>();
                datas = requestData.data();

                for (Map.Entry<String, String> entry : datas.entrySet()) {
                    catchList.add(entry.getValue());
                }

                //远程图片
                uploadType = "catch";
            }

            if (uploadType.equals("scrawl")) {
                result = Ueditor.uploadBase64Deal(upContent);
            } else if (uploadType.equals("catch")) {
                result = Ueditor.catchRemote(catchList);
            } else {
                String fileName = upFile.getFilename();
                File file = upFile.getFile();
                result = Ueditor.uploadFileDeal(file, fileName, uploadType);
            }

        } else if (url.equals(imageManagerActionName)
                || url.equals(fileManagerActionName)) {

            //在线管理
            result = Ueditor.listManager(url,
                    Integer.valueOf(requestData.get("start")),
                    Integer.valueOf(requestData.get("size")));
        }

        return ok(result);

    }
}
