package controllers;

import play.mvc.Security;
import util.ConfigUtil;
import util.UUIDUtil;
import views.html.config.configEmail;
import models.Email;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.List;

/**
 * Created by zuopanpan on 2016/2/25.
 */
@Security.Authenticated(SecureAdmin.class)
public class EmailAction extends Controller {
    /**
     * 初始查询邮件信息
     * @param
     * @return
     */
    public static Result emailInfo() {
        //判查询邮件列表
        List<Email> list=Email.selectList(null,ConfigUtil.getApplication("currentDataSource"));
        Email email=new Email();
        //判断列表是否有值
        if(list.size()>0){
            //若有值,则将列表第一条数据返回页面
            email=list.get(0);
        }
        return ok(configEmail.render(email));
    }
    /**
     * 更新
     * @param
     * @return
     */
    public static Result update() {
        Form<Email> form=Form.form(Email.class).bindFromRequest();
        Email email=form.get();
        email.updatetime=new Date();
        email.updateuser=session("id");
        //查询邮件设置列表
        List<Email> list=Email.selectList(null, ConfigUtil.getApplication("currentDataSource"));
        //若列表有数据,则更新,若没有,则新增
        if(list.size()>0){
           Email.update(email,ConfigUtil.getApplication("currentDataSource"));
        }else{
            email.id= UUIDUtil.getUUID();
            email.station_id=session("site_id");
            Email.insert(email,ConfigUtil.getApplication("currentDataSource"));
        }
        return ok("保存成功！");
    }
}
