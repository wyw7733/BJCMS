package controllers;

import models.Blogroll;
import models.BlogrollType;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.ObjectUtil;
import util.UUIDUtil;
import views.html.expressTools.blogrollTypeManage.blogrollTypeAdd;
import views.html.expressTools.blogrollTypeManage.blogrollTypeManage;
import views.html.expressTools.blogrollTypeManage.blogrollTypeModify;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/2/26.
 */
@Security.Authenticated(SecureAdmin.class)
public class BlogrollTypeAction extends Controller{
    /**
     * 初始查询链接类型
     * @param
     * @return
     */
    public static Result list() {
        Map<String,Object> map=new HashMap<String,Object>();
        //查询未删除列表
        map.put("del", Constant.DEL_NO);
        List<BlogrollType> bgtList=BlogrollType.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        return ok(blogrollTypeManage.render(bgtList));
    }
    /**
     * 跳转新增页面
     * @param
     * @return
     */
    public static Result toAdd() {
        Map<String,Object> map=new HashMap<String,Object>();
        //查询未删除列表
        map.put("del", Constant.DEL_NO);
        List<BlogrollType> bgtList=BlogrollType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(blogrollTypeAdd.render(bgtList));
    }

    /**
     * 添加
     * @param
     * @return
     */
    public static Result add() {
        Form<BlogrollType> form=Form.form(BlogrollType.class).bindFromRequest();
        BlogrollType blogrollType=form.get();
        //判断类型的排序是否为"",若为"",则赋值为null
        if("".equals(blogrollType.priority)){
            blogrollType.priority=null;
        }
        blogrollType.id= UUIDUtil.getUUID();
        //设置未删除属性
        blogrollType.del= Constant.DEL_NO;
        blogrollType.updatetime=new Date();
        blogrollType.updateuser=session("id");
        blogrollType.station_id=session("site_id");
        BlogrollType.insert(blogrollType,ConfigUtil.getApplication("currentDataSource"));
        return ok("添加成功！");
    }
    /**
     * 跳转修改页面
     * @param
     * @return
     */
    public static Result toModify() {
        Form<BlogrollType> form=Form.form(BlogrollType.class).bindFromRequest();
        String id=form.get().id;
        //根据id获取链接类型信息
        BlogrollType blogrollType=BlogrollType.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        return ok(blogrollTypeModify.render(blogrollType));
    }
    /**
     * 更新
     * @param
     * @return
     */
    public static Result modify() {
        Form<BlogrollType> form=Form.form(BlogrollType.class).bindFromRequest();
        BlogrollType blogrollType=form.get();
        //判断类型的排序是否为"",若为"",则赋值为null
        if(ObjectUtil.isNull(blogrollType.priority)){
            blogrollType.priority=null;
        }
        blogrollType.updatetime=new Date();
        blogrollType.updateuser=session("id");
        BlogrollType.update(blogrollType,ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }
    /**
     * 更新
     * @param
     * @return
     */
    public static Result delete() {
        Form<BlogrollType> form=Form.form(BlogrollType.class).bindFromRequest();
        BlogrollType blogrollType=form.get();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("del", Constant.DEL_NO);
        map.put("type_id",blogrollType.id);
        //查询当前类型下未删除友情连接列表
        List<Blogroll> bgList= Blogroll.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        //若列表有数据,则不能删除
        if(bgList.size()>0){
            return ok("该类型下有链接数据，不能删除！");
        }
        //设置删除属性
        blogrollType.del= Constant.DEL_YES;
        BlogrollType.update(blogrollType,ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功！");
    }
}
