package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import util.*;
import views.html.personalManage.modifyPassword;
import views.html.personalManage.userInfo;
import views.html.personalManage.userInfoModify;
import views.html.userManager.adminUserManage.adminUserAdd;
import views.html.userManager.adminUserManage.adminUserIndex;
import views.html.userManager.adminUserManage.adminUserManage;
import views.html.userManager.adminUserManage.adminUserModify;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houyaohui on 2016/1/21.
 */
@Security.Authenticated(SecureAdmin.class)
public class AdminUserAction extends Controller {


    /**
     * 初始查询用户列表
     *
     * @param
     * @return
     */
    public static Result list() {
        Map<String, Object> map = new HashMap<>();
        //正常用户列表
        map.put("audit", Constant.AUDIT_NORMAL);
        List<AdminUser> normalAdminUser = AdminUser.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        //冻结用户列表
        map.put("audit", Constant.AUDIT_FROZEN);
        List<AdminUser> frozenlAdminUser = AdminUser.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(adminUserManage.render(normalAdminUser, frozenlAdminUser));
    }

    /**
     * 初始用户管理页面
     *
     * @param
     * @return
     */
    public static Result index() {
        return ok(adminUserIndex.render());
    }

    /**
     * 跳转新增页面
     *
     * @param
     * @return
     */
    public static Result toAdd() {
        Map<String, Object> map = new HashMap<>();
        map.put("del", Constant.DEL_NO);
        List<Role> roleList = Role.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(adminUserAdd.render(roleList));
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    public static Result add() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        Map<String,String> existMap=form.data();
        existMap.put("audit", Constant.AUDIT_DELETE);
        if(AdminUser.isExist(existMap,ConfigUtil.getApplication("currentDataSource"))){
            return ok("该用户名已存在！");
        }
        String role_id =  form.data().remove("role_id");
        AdminUser adminUser = form.get();
        adminUser.id = UUIDUtil.getUUID();
        //设置正常状态
        adminUser.audit = Constant.AUDIT_NORMAL;
        //设置初始密码
        adminUser.password = MD5Util.MD5(Constant.INIT_PASSWORD);
        adminUser.updatetime = new Date();
        adminUser.updateuser = session("id");
        adminUser.station_id = session("site_id");
        //添加
        AdminUser.insert(adminUser, role_id,ConfigUtil.getApplication("currentDataSource"));
        return ok("true");
    }

    /**
     * 跳转修改页面
     *
     * @param
     * @return
     */
    public static Result toModify() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        String id = form.get().id;
        //根据id插查询用户信息
        Map<String, Object> map = new HashMap<>();
        map.put("del", Constant.DEL_NO);
        List<Role> roleList = Role.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        map.remove("del");
        map.put("user_id", id);
        List<UserRole> userRoleList = UserRole.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        UserRole userRole = new UserRole();
        if (userRoleList.size() > 0) {
            userRole = userRoleList.get(0);
        }
        AdminUser adminUser = AdminUser.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        return ok(adminUserModify.render(adminUser, userRole, roleList));
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    public static Result modify() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        UserRole userRole = new UserRole();
        //获取用户角色信息
        userRole.id = form.data().remove("userRoleId");
        userRole.role_id = form.data().remove("role_id");
        userRole.updatetime = new Date();
        userRole.updateuser = session("id");
        //获取用户信息
        AdminUser adminUser = form.get();
        AdminUser.update(adminUser, userRole,ConfigUtil.getApplication("currentDataSource"));
        return ok("true");
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    public static Result delete() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        AdminUser adminUser = form.get();
        //设置删除状态
        adminUser.audit = Constant.AUDIT_DELETE;
        AdminUser.update(adminUser,ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功！");
    }
    /**
     * 冻结
     *
     * @param
     * @return
     */
    public static Result frozen() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        AdminUser adminUser = form.get();
        //设置冻结状态
        adminUser.audit = Constant.AUDIT_FROZEN;
        AdminUser.update(adminUser,ConfigUtil.getApplication("currentDataSource"));
        return ok("冻结成功！");
    }
    /**
     * 重置密码
     *
     * @param
     * @return
     */
    public static Result initPassword() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        AdminUser adminUser = form.get();
        //设置初始密码
        adminUser.password =MD5Util.MD5(Constant.INIT_PASSWORD);
        AdminUser.update(adminUser,ConfigUtil.getApplication("currentDataSource"));
        return ok("密码重置成功，重置后的密码为：123456");
    }
    /**
     * 解冻
     *
     * @param
     * @return
     */
    public static Result unfrozening() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        AdminUser adminUser = form.get();
        //设置冻结状态
        adminUser.audit = Constant.AUDIT_NORMAL;
        AdminUser.update(adminUser,ConfigUtil.getApplication("currentDataSource"));
        return ok("解冻成功！");
    }




    /**
     * 查询个人信息
     *
     * @param
     * @return
     */
    public static Result personalInfo() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        //根据id查询个人信息
        AdminUser adminUser = AdminUser.getObject(form.get().id,ConfigUtil.getApplication("currentDataSource"));
        //查询站点信息
        Site site=new Site();
        Site tempSite=Site.getSite(ConfigUtil.getApplication("currentDataSource"));
        if(ObjectUtil.isNotNull(tempSite)){
            site=tempSite;
        }
        return ok(userInfo.render(site,adminUser));
    }

    /**
     * 跳转修改密码页面
     *
     * @param
     * @return
     */
    public static Result toModifyPwd() {
        return ok(modifyPassword.render());
    }

    /**
     * 修改密码
     *
     * @param
     * @return
     */
    public static Result modifyPwd() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        String id = session("id");
        //根据id获取用户信息
        AdminUser adminUser = AdminUser.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        String message = "修改成功！";
        String newPassword = MD5Util.MD5(form.data().get("password"));
        //判断输入的原密码是否正确
        if (!newPassword.equals(adminUser.password)) {
            message = "原密码错误！";
        } else {
            adminUser=new AdminUser();
            adminUser.id=id;
            adminUser.password = MD5Util.MD5(form.data().get("newPassword1"));
            AdminUser.update(adminUser,ConfigUtil.getApplication("currentDataSource"));
        }
        return ok(message);
    }

    /**
     * 跳转修改用户信息页面
     *
     * @param
     * @return
     */
    public static Result toUserInfoModify() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        String id = session("id");
        //根据id获取用户信息
        AdminUser adminUser = AdminUser.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        //查询站点信息（域名）
        Site site = Site.getSite(ConfigUtil.getApplication("currentDataSource"));
        if (ObjectUtil.isNotNull(site)) {
            return ok(userInfoModify.render(adminUser,site));
        }
        return badRequest();
    }

    /**
     * 修改用户信息
     *
     * @param
     * @return
     */
    public static Result userInfoModify() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        AdminUser.modifyUserInfo(body,form,session("id"),ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }


}
