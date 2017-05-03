package controllers;

import com.alibaba.fastjson.JSONArray;
import models.Blogroll;
import models.BlogrollType;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.UUIDUtil;
import views.html.expressTools.blogrollManage.blogrollAdd;
import views.html.expressTools.blogrollManage.blogrollIndex;
import views.html.expressTools.blogrollManage.blogrollManage;
import views.html.expressTools.blogrollManage.blogrollModify;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/2/26.
 */
@Security.Authenticated(SecureAdmin.class)
public class BlogrollAction extends Controller {
    /**
     * 返回友情连接初始页面
     * @param
     * @return
     */
    public static Result index() {
        return ok(blogrollIndex.render());
    }
    /**
     * 初始查询连接列表
     * @param
     * @return
     */
    public static Result list() {
        Map<String,Object> map=new HashMap<String,Object>();
        //查询未删除连接类型列表
        map.put("del", Constant.DEL_NO);
        List<BlogrollType> bgtList=BlogrollType.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        //查询未删除友情连接列表
        List<Blogroll> bgList= Blogroll.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(blogrollManage.render(bgtList, bgList));
    }
    /**
     * 添加页面
     * @param
     * @return
     */
    public static Result toAdd() {
        Map<String,Object> map=new HashMap<String,Object>();
        //查询未删除类型列表
        map.put("del", Constant.DEL_NO);
        List<BlogrollType> bgtList=BlogrollType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(blogrollAdd.render(bgtList));
    }
    /**
     * 添加
     * @param
     * @return
     */
    public static Result add() {
        Form<Blogroll> form=Form.form(Blogroll.class).bindFromRequest();
        String parentId=form.data().get("parentId");
        Blogroll blogroll=form.get();
        blogroll.id= UUIDUtil.getUUID();
        blogroll.code=Blogroll.getCodeVal(parentId,ConfigUtil.getApplication("currentDataSource"));
        //设置未删除状态
        blogroll.del= Constant.DEL_NO;
        blogroll.updatetime=new Date();
        blogroll.updateuser=session("id");
        blogroll.station_id=session("site_id");
        Blogroll.insert(blogroll,ConfigUtil.getApplication("currentDataSource"));
        return ok("添加成功！");
    }
    /**
     * 跳转修改页面
     * @param
     * @return
     */
    public static Result toModify() {
        Map<String,Object> map=new HashMap<String,Object>();
        //查询未删除类型列表
        map.put("del", Constant.DEL_NO);
        List<BlogrollType> bgtList=BlogrollType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        Form<Blogroll> form=Form.form(Blogroll.class).bindFromRequest();
        String id=form.get().id;
        Blogroll blogroll=Blogroll.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        return ok(blogrollModify.render(bgtList,blogroll));
    }
    /**
     * 更新
     * @param
     * @return
     */
    public static Result modify() {
        Form<Blogroll> form=Form.form(Blogroll.class).bindFromRequest();
        Blogroll blogroll=form.get();
        blogroll.updatetime=new Date();
        blogroll.updateuser=session("id");
        Blogroll.update(blogroll,ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }
    /**
     * 删除
     * @param
     * @return
     */
    public static Result delete() {
        Form<Blogroll> form=Form.form(Blogroll.class).bindFromRequest();
        Blogroll blogroll=form.get();
        //设置删除状态
        blogroll.del= Constant.DEL_YES;
        Blogroll.update(blogroll,ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功！");
    }
    /**
     * 阿帕奇前台查询
     * @param
     * @return
     */
    public static Result blogrollListWeb() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        List<Object> list=Blogroll.selectListWeb(ConfigUtil.getApplication("currentDataSource"));
        return ok(callbackparam + "(" + JSONArray.toJSONString(list) + ")");
    }

}
