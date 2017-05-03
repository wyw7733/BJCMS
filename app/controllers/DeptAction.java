package controllers;

import com.alibaba.fastjson.JSON;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;
import models.AdminUser;
import models.Authority;
import models.Column;
import models.Dept;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.ObjectUtil;
import util.UUIDUtil;
import views.html.userManager.deptManage.deptAdd;
import views.html.userManager.deptManage.deptModify;
import views.html.userManager.deptManage.deptManage;

import java.util.*;

/**
 * Created by houyaohui on 2016/1/21.
 */
@Security.Authenticated(SecureAdmin.class)
public class DeptAction extends Controller {
    /**
     * 初始部门页面
     * @param
     * @return
     */
    public static Result index(){
        return ok(deptManage.render());
    }
    /**
     * 初始查询部门列表
     * @param
     * @return
     */
    public static Result list() {
        List<SqlRow> deptlist=Dept.selectList(null,ConfigUtil.getApplication("currentDataSource"));
        String deptList= JSON.toJSONString(deptlist);
        return ok(deptList);
    }

    /**
     * 增加
     * @param
     * @return
     */
    public static Result add() {
        Form<Dept> form=Form.form(Dept.class).bindFromRequest();
        Dept dept=form.get();
        dept.id= UUIDUtil.getUUID();
        String parent=null;
        if(ObjectUtil.isNotNull(dept.parent)){
            parent=dept.parent;
        }
        dept.code=Dept.getCodeVal(parent,"select MAX(code) code from M_Dept where 1=1",ConfigUtil.getApplication("currentDataSource"));
        dept.updatetime=new Date();
        dept.updateuser=session("id");
        dept.station_id=session("site_id");
        Dept.insert(dept,ConfigUtil.getApplication("currentDataSource"));
        return ok("添加成功！");
    }
    /**
     * 跳转增加页面
     * @param
     * @return
     */
    public static Result toAdd() {
       // List<SqlRow> deptlist=Dept.selectList(null);
        return ok(deptAdd.render());
    }
    /**
     * 跳转修改页面
     * @param
     * @return
     */
    public static Result toModify() {
        Form<Dept> form=Form.form(Dept.class).bindFromRequest();
        Dept dept=Dept.getObject(form.get().id,ConfigUtil.getApplication("currentDataSource"));
        List<SqlRow> deptlist=Dept.selectList(null,ConfigUtil.getApplication("currentDataSource"));
        deptlist.remove(dept);
        return ok(deptModify.render(dept));
    }
    /**
     * 修改
     * @param
     * @return
     */
    public static Result modify() {
        Form<Dept> form = Form.form(Dept.class).bindFromRequest();
        Dept dept=form.get();
        dept.updatetime=new Date();
        dept.updateuser=session("id");
        String parent=null;
        if(ObjectUtil.isNotNull(dept.parent)){
            parent=dept.parent;
        }
        dept.code = Dept.getCodeVal(parent, "select MAX(code) code from M_Dept where 1=1", ConfigUtil.getApplication("currentDataSource"));
        Dept.upadte(dept,ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }
    /**
     * 删除
     * @param
     * @return
     */
    public static Result delete() {
        String result="删除成功！";
        Form<Dept> form = Form.form(Dept.class).bindFromRequest();
        String id=form.get().id;
        Map<String,String> searchMap=new HashMap<>();
        searchMap.put("parent", id);
        Map<String,Object> map=new HashMap<>();
        map.put("dept_id", id);
        //查询该部门下用户
        List<AdminUser> adminUserList=AdminUser.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        //若有用户，则不能删除
        if(ObjectUtil.isNotNull(adminUserList)){
            result="该部门下有用户，不能删除！";
        }else{
            //查询子节点
            List<SqlRow> deptList=Dept.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
            //若有子节点,则不能删除,若没有则删除
            if(ObjectUtil.isNotNull(deptList)){
                result="请先删除子节点！";
            }else{
                Dept dept=new Dept();
                dept.id=id;
                Dept.delete(dept,ConfigUtil.getApplication("currentDataSource"));
            }
        }
        return ok(result);
    }
    /**
     * 查询部门列表（树形）
     * @param
     * @return
     */
    public static Result selectDeptJsonTree() {
        String josnStr=Dept.selectDeptJsonTree(true,ConfigUtil.getApplication("currentDataSource"));
        return ok(josnStr);
    }
    /**
     * 查询部门列表（jsTree树形）
     * @param
     * @return
     */
    public static Result selectJsTreeDept(){
        Form<Dept> form = Form.form(Dept.class).bindFromRequest();
        String deptJsonTree=Dept.jsonTree(form.data(),ConfigUtil.getApplication("currentDataSource"));
        return ok(Json.parse(deptJsonTree));
    }
}
