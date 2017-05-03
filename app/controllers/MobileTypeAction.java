package controllers;

import models.Config;
import models.MobileType;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.FileUtil;
import util.UUIDUtil;
import views.html.mobileManage.mobileType.mobileTypeIndex;
import views.html.mobileManage.mobileType.mobileTypeManage;
import views.html.mobileManage.mobileType.mobileTypeAdd;
import views.html.mobileManage.mobileType.mobileTypeModify;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/1/14.
 */
@Security.Authenticated(SecureAdmin.class)
public class MobileTypeAction extends Controller {
    /**初始导航页面
     * @param
     * @return
     */
    public static Result index() {
        return ok(mobileTypeIndex.render());
    }

    /**初始查询列表
     * @param
     * @return
     */
    public static Result list() {
        String query=null;
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("del",Constant.DEL_NO);
        //查询未删除模板类型列表
        List<MobileType> mobileTypeList=MobileType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(mobileTypeManage.render(mobileTypeList,query));
    }
    /**跳转新增页面
     * @param
     * @return
     */
    public static Result toAdd() {
        return ok(mobileTypeAdd.render());
    }
    /**新增
     * @param
     * @return
     */
    public static Result add() {
        Form<MobileType> form = Form.form(MobileType.class).bindFromRequest();
        MobileType mobileType=form.get();
        mobileType.id= UUIDUtil.getUUID();
        //设置未删除状态
        mobileType.del=Constant.DEL_NO;
        mobileType.updatetime=new Date();
        mobileType.updateuser=session("id");
        mobileType.station_id=session("site_id");
        //在本地创建对应的文件夹
        Config config=Config.getConfig(ConfigUtil.getApplication("currentDataSource"));
        FileUtil.createDir(config.templatepath+mobileType.name);
        MobileType.insert(mobileType,ConfigUtil.getApplication("currentDataSource"));
        return ok("创建成功！");
    }
    /**
     * 跳转修改页面
     * @param id
     * @return
     */
    public static Result toModify(String id) {
        MobileType mobileType=MobileType.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        return ok(mobileTypeModify.render(mobileType));
    }
    /**
     * 修改
     * @param
     * @return
     */
    public static Result modify() {
        //获取表单数据mobileType
        Form<MobileType> form = Form.form(MobileType.class).bindFromRequest();
        MobileType mobileType=form.get();
        mobileType.updatetime=new Date();
        mobileType.updateuser=session("id");
        MobileType.upadte(mobileType,ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }
    /**
     * 批量删除
     *
     * @return
     */
    public static Result delete() {
        Form<MobileType> form = Form.form(MobileType.class).bindFromRequest();
        String ids=form.data().get("ids");
        MobileType.deleteTypes(ids,ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功！");
    }
    /**
     * 模糊查询模版类型列表
     * @param query
     * @return
     */
    public static Result find(String query) {
        List<MobileType> mobileTypeList=MobileType.find(query,ConfigUtil.getApplication("currentDataSource"));
        return ok(mobileTypeManage.render(mobileTypeList,query));
    }
}
