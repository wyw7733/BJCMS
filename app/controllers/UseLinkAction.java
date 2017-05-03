package controllers;
import models.BlogrollType;
import models.UseLink;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.UUIDUtil;
import views.html.expressTools.useLink.useLinkIndex;
import views.html.expressTools.useLink.useLinkManager;
import views.html.expressTools.useLink.useLinkAdd;
import views.html.expressTools.useLink.useLinkUpdate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuyawei on 2017/4/24.
 */

@Security.Authenticated(SecureAdmin.class)
public class UseLinkAction extends Controller {

    /**
     * 返回常用链接出事页面
     * @return
     */
    public static Result index() {
        return ok(useLinkIndex.render());
    }

    /**
     * 列表查询
     * @return
     */
    public static Result list(){
        List<UseLink> useLinkList =  UseLink.selectList(null,ConfigUtil.getApplication("currentDataSource"));
        return ok(useLinkManager.render(useLinkList));
    }

    /**
     * 跳转到添加页面
     * @return
     */
    public static Result toUseLinkPage(){
        return ok(useLinkAdd.render());
    }

    /**
     * 添加链接信息到数据库
     */
    public static Result addUseLink(){
        Form<UseLink> form=Form.form(UseLink.class).bindFromRequest();
        UseLink useLink = form.get();
        useLink.id = UUIDUtil.getUUID();
        useLink.del = Constant.DEL_NO;
        useLink.update_user = session("id");
        useLink.update_time = new Date();
        useLink.station_id = session("site_id");
        UseLink.insert(useLink, ConfigUtil.getApplication("currentDataSource"));
        return ok("添加成功!");
    }
    /**
     * 跳转到更新页面
     */
    public static Result updatePage(){
        Form<UseLink> form = Form.form(UseLink.class).bindFromRequest();
        String id = form.get().id;
        System.out.println("id=" + id);
        UseLink useLink=UseLink.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        return ok(useLinkUpdate.render(useLink));
    }

    /**
     * 更新链接数据
     * @return
     */
    public static Result updateUseLink(){
        Form<UseLink> form=Form.form(UseLink.class).bindFromRequest();
        UseLink useLink=form.get();
        useLink.update_time = new Date();
        useLink.update_user = session("id");
        useLink.update(useLink, ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }

    /**
     * 删除链接数据
     * @return
     */
    public static  Result deleteUseLink(){
        Form<UseLink> form=Form.form(UseLink.class).bindFromRequest();
        UseLink useLink=form.get();
        UseLink.delete(useLink, ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功！");
    }
}
