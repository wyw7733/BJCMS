package controllers;

import com.alibaba.fastjson.JSON;
import com.avaje.ebean.SqlRow;
import com.google.common.collect.Maps;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.ObjectUtil;
import views.html.orgManage.orgManage;
import views.html.orgManage.orgModify;
import views.html.orgManage.leaderAdd;

/**
 * Created by zuopanpan on 2016/7/4.
 */
@Security.Authenticated(SecureAdmin.class)
public class OrganizationsAction extends Controller{
    /**
     * 初始组织机构页面   *
     * @param
     * @return
     */
    public static Result index() {
        return ok(orgManage.render());
    }
    /**
     * 查询组织机构列表 *
     * @param
     * @return
     */
    public static Result list() {
        Map<String,String> searchMap=new HashMap<String,String>();
        searchMap.put("del",Constant.DEL_NO);
        List<SqlRow> orglist= Organizations.selectList(searchMap);
        for (SqlRow sqlRow : orglist) {
            //查询附件
            Map<String, Object> search = Maps.newHashMap();
            search.put("org_id", sqlRow.getString("id"));
            List<Leader> list =  Leader.selectListByMap(search);
            String name = "";
            for (int i=0; i<list.size(); i++) {
                name += list.get(i).name + "、";
            }
            if(name.length() > 0) {
                name = name.substring(0, name.length()-1);
            }
            sqlRow.put("leadername", name);
        }
        String orgList= JSON.toJSONString(orglist);
        return ok(orgList);
    }
    /**
     * 跳转修改页面 *
     * @param
     * @return
     */
    public static Result toModify() {
        Form<Organizations> form = Form.form(Organizations.class).bindFromRequest();
        String id = form.data().get("id");
        Organizations organizations = Organizations.getObject(id);
        Site site=Site.getSite(ConfigUtil.getApplication("currentDataSource"));
        return ok(orgModify.render(organizations,site));
    }

    /**
     * 添加领导 *
     * @param
     * @return
     */
    public static Result leaderAdd() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Form<Leader> form = Form.form(Leader.class).bindFromRequest();
        Leader.insert(body, form, session("id"),ConfigUtil.getApplication("currentDataSource"));
        return ok("添加成功！");
    }

}
