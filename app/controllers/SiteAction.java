package controllers;

import models.Site;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.ObjectUtil;
import util.UUIDUtil;
import views.html.config.configSite;

import java.util.Date;
import java.util.List;

/**
 * Created by zuopanpan on 2016/2/25.
 */
@Security.Authenticated(SecureAdmin.class)
public class SiteAction extends Controller {
    /**
     * 初始查询站点信息
     * @param
     * @return
     */
    public static Result siteInfo() {
        Site site=new Site();

        Site tempSite=Site.getSite(ConfigUtil.getApplication("currentDataSource"));
        //判断tempSite是否为null
        if(ObjectUtil.isNotNull(tempSite)){
            //若不为null,将列表第一个对象返回页面
            site=tempSite;
        }
        return ok(configSite.render(site));
    }
    /**
     * 更新
     * @param
     * @return
     */
    public static Result update() {
        Form<Site> form=Form.form(Site.class).bindFromRequest();
        Site site=form.get();
        site.updatetime=new Date();
        site.updateuser=session("id");
        //判断站点信息是否为空，若为空则新增，若不为空则更新
        if(ObjectUtil.isNotNull(Site.getSite(ConfigUtil.getApplication("currentDataSource")))){
            Site.update(site, ConfigUtil.getApplication("currentDataSource"));
        }else{
            site.id= UUIDUtil.getUUID();
            site.station_id=site.id;
            Site.insert(site,ConfigUtil.getApplication("currentDataSource"));
        }
        return ok("保存成功！");
    }
}

