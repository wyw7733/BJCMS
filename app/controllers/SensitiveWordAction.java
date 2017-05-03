package controllers;

import com.avaje.ebean.SqlRow;
import com.google.common.collect.Maps;
import models.AdminUser;
import models.Sensitive;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import sun.management.Sensor;
import util.ConfigUtil;
import util.ObjectUtil;
import util.UUIDUtil;
import views.html.systemManager.sensitiveWords.wordIndex;
import views.html.systemManager.sensitiveWords.wordAdd;
import views.html.systemManager.sensitiveWords.wordManage;
import views.html.systemManager.sensitiveWords.wordModify;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by houyaohui on 2016/1/21.
 */
@Security.Authenticated(SecureAdmin.class)
public class SensitiveWordAction extends Controller {
    /**
     * 首页
     *
     * @param
     * @return
     */
    public static Result wordIndex() {
        return ok(wordIndex.render());
    }

    /**
     * 敏感词管理
     *
     * @return
     */
    public static Result wordManage() {
        List<SqlRow> wordList = Sensitive.selectList(new HashMap<String, String>(), ConfigUtil.getApplication("currentDataSource"));
        return ok(wordManage.render(wordList));
    }

    /**
     * 跳转新增敏感词页面
     *
     * @param
     * @return
     */
    public static Result toWordAdd() {
        return ok(wordAdd.render());
    }

    /**
     * 敏感词添加
     *
     * @return
     */
    public static Result wordAdd() {
        setValue("add");
        return ok("添加成功");
    }

    /**
     * 跳转修改敏感词页面
     *
     * @param
     * @return
     */
    public static Result toWordModify() {
        DynamicForm form = Form.form().bindFromRequest();
        String id = form.data().get("id");
        if (ObjectUtil.isNotNull(id)) {
            Sensitive sensitive = Sensitive.getObject(id,ConfigUtil.getApplication("currentDataSource"));
            return ok(wordModify.render(sensitive));
        }
        return badRequest("id参数有误");
    }

    /**
     * 敏感词修改
     *
     * @return
     */
    public static Result wordModify() {
        setValue("modify");
        return ok("修改成功");
    }

    /**
     * 敏感词删除
     *
     * @return
     */
    public static Result wordDelete() {
        DynamicForm form = Form.form().bindFromRequest();
        String id = form.data().get("id");
        if (ObjectUtil.isNotNull(id)) {
            Sensitive.deleteObject(id,ConfigUtil.getApplication("currentDataSource"));
        }
        return ok("删除成功");
    }

    private static Sensitive setValue(String flag) {
        Form<Sensitive> form = Form.form(Sensitive.class).bindFromRequest();
        Sensitive sensitive = form.get();
        sensitive.updatetime = new Date();
        sensitive.updateuser = session("id");
        if ("add".equals(flag)) {
            sensitive.id = UUIDUtil.getUUID();
            Sensitive.insert(sensitive,ConfigUtil.getApplication("currentDataSource"));
        } else {
            Sensitive.update(sensitive,ConfigUtil.getApplication("currentDataSource"));
        }
        return sensitive;
    }
}
