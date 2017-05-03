package controllers;


import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.UUIDUtil;
import views.html.userManager.roleManager.roleManage;
import views.html.userManager.roleManager.roleAdd;
import views.html.userManager.roleManager.roleModify;
import views.html.userManager.roleManager.roleIndex;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houyaohui on 2016/1/21.
 */
@Security.Authenticated(SecureAdmin.class)
public class RoleAction extends Controller {
    /**
     * 初始角色页面
     * @param
     * @return
     */
    public static Result index() {
        return ok(roleIndex.render());
    }
    /**
     * 初始查询角色列表
     * @param
     * @return
     */
    public static Result list() {
        Map<String,Object> map=new HashMap<>();
        map.put("del", Constant.DEL_NO);
        //查询未删除角色列表
        List<Role> roleList=Role.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(roleManage.render(roleList));
    }

    /**
     * 跳转新增页面
     * @param
     * @return
     */
    public static Result toRoleAdd() {
        return ok(roleAdd.render());
    }
    /**
     * 添加
     * @param
     * @return
     */
    public static Result add() {
        Form<Role> form=Form.form(Role.class).bindFromRequest();
        String parentId=form.data().get("parentId");
        Role role=form.get();
        role.id= UUIDUtil.getUUID();
        //设置code
        role.tier_code=Role.getCodeVal(parentId, ConfigUtil.getApplication("currentDataSource"));
        //设置未删除状态
        role.del=Constant.DEL_NO;
        role.updatetime=new Date();
        role.updateuser=session("id");
        role.station_id=session("site_id");
        Role.insert(role,ConfigUtil.getApplication("currentDataSource"));
        String str=role.id;
        str+=role.name;
        return ok(str);
    }
    /**
     * 跳转修改页面
     * @param
     * @return
     */
    public static Result toRoleModify() {
        Form<Role> form=Form.form(Role.class).bindFromRequest();
        Role role=Role.getObject(form.get().id,ConfigUtil.getApplication("currentDataSource"));
        return ok(roleModify.render(role));
    }
    /**
     * 修改
     * @param
     * @return
     */
    public static Result modify() {
        Form<Role> form=Form.form(Role.class).bindFromRequest();
        Role role=form.get();
        role.updatetime=new Date();
        role.updateuser=session("id");
        Role.update(role,ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }
    /**
     * 删除
     * @param
     * @return
     */
    public static Result delete() {
        Form<Role> form=Form.form(Role.class).bindFromRequest();
        Role role=form.get();
        role.del=Constant.DEL_YES;
        role.updatetime=new Date();
        role.updateuser=session("id");
        Role.update(role,ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功！");
    }
    /**
     * 为角色设置权限
     * @param
     * @return
     */
    public static Result roleAuthAdd() {
        Form<Role> form=Form.form(Role.class).bindFromRequest();
        //获取表单数据
        Map<String,String> map=form.data();
        RoleAuth.insert(map,ConfigUtil.getApplication("currentDataSource"));
        return ok("设置成功！");
    }
}








