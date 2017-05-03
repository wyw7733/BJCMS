package controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.avaje.ebean.SqlRow;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.ObjectUtil;
import views.html.orgManage.leaderAdd;
import views.html.orgManage.orgManage;
import views.html.orgManage.orgModify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/7/4.
 */
@Security.Authenticated(SecureAdmin.class)
public class LeaderAction extends Controller{
    /**
     * 删除
     * @param
     * @return
     */
    public static Result delete() {
        Form<Leader> form=Form.form(Leader.class).bindFromRequest();
        Leader leader=form.get();
        Leader.delete(leader);
        return ok("删除成功！");
    }

    /**
     * 跳转修改页面 *
     * @param
     * @return
     */
    public static Result leaderToAdd() {
        Form<Organizations> form = Form.form(Organizations.class).bindFromRequest();
        String id = form.data().get("id");

        String type = form.data().get("type");
        Organizations organizations = Organizations.getObject(id);
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("org_id", id);
        List<Leader> list =  Leader.selectListByMap(searchMap);
        Leader leader=new Leader();
        if(list.size() != 0) {
            if("1".equals(type)) {
                leader = list.get(0);
            } else {
                if(list.size() > 1) {
                    leader = list.get(1);
                }
            }
        }
        Site site=Site.getSite(ConfigUtil.getApplication("currentDataSource"));
        return ok(leaderAdd.render(organizations,leader,site));
    }
    /**
     * 修改领导信息
     * @param
     * @return
     */
    public static Result learderModify() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Form<Leader> form = Form.form(Leader.class).bindFromRequest();
        Leader.update(body, form, session("id"),ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }
    /**
     * 前台查询领导信息
     * @param
     * @return
     */
    public static Result leaderInfoWebSite() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        String id = form.data().get("id");
        Organizations organization = Organizations.getObject(id);
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("org_id", id);
        List<Leader> list =  Leader.selectListByMap(searchMap);
        Leader leader1=new Leader();
        Leader leader2=new Leader();
        String isExist1 = "0";
        String isExist2 = "0";
        if(list.size() != 0) {
            leader1 = list.get(0);
            isExist1 = "1";
            if(list.size() > 1) {
                leader2 = list.get(1);
                isExist2 = "1";
            }
        }

        Map<String,Object> result=new HashMap<String,Object>();
        result.put("leader1",leader1);
        result.put("leader2",leader2);
        result.put("isExist1",isExist1);
        result.put("isExist2",isExist2);
        result.put("org", organization);
        return ok(callbackparam + "(" + JSONArray.toJSONString(result) + ")");
    }

    /**
     * 前台分页查询领导列表
     * @param
     * @return
     */
    public static Result selectLeaderPage() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        Map<String,String> searchMap=form.data();
        searchMap.put("del", Constant.DEL_NO);
        searchMap.put("codeLeader", "1");
        Page page= Leader.selectList(searchMap);
        return ok(callbackparam + "(" + Json.toJson(page) + ")");
    }
}
