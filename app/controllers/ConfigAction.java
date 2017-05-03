package controllers;

import models.Config;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.UUIDUtil;
import views.html.config.configIndex;
import views.html.config.configAll;

import java.util.Date;
import java.util.List;

@Security.Authenticated(SecureAdmin.class)
public class ConfigAction extends Controller {

    /***
     * 返回配置管理首页
     * @return
     */
    public  static  Result index(){
        return ok(configIndex.render());
    }
    /***
     * 初始显示全局设置信息
     * @return
     */
    public  static  Result configInfo(){
        //查询全局设置信息
        Config config=Config.getObject(ConfigUtil.getApplication("currentDataSource"));
        return ok(configAll.render(config));
    }
    /***
     * 更新
     * @return
     */
    public  static  Result update(){
        Form<Config> form=Form.form(Config.class).bindFromRequest();
        Config config=form.get();
        config.updatetime=new Date();
        config.updateuser=session("id");
        //查询全局设置列表
        List<Config> list=Config.selectList(null, ConfigUtil.getApplication("currentDataSource"));
        //若列表有数据,则更新,若没有,则新增
        if(list.size()>0){
            Config.update(config,ConfigUtil.getApplication("currentDataSource"));
        }else{
            config.id= UUIDUtil.getUUID();
            config.station_id=session("site_id");
            Config.insert(config,ConfigUtil.getApplication("currentDataSource"));
        }
        return ok("保存成功！");
    }
}
