package controllers;

import com.alibaba.fastjson.JSONArray;
import models.User;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import util.ConfigUtil;
import util.MD5Util;
import util.ObjectUtil;
import util.UUIDUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/4/7.
 */
public class UserAction extends Controller {
    /**
     * 前台注册
     *
     * @return
     */
    public static Result add() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        Map<String,String> existMap=form.data();
        if(User.isExist(existMap,ConfigUtil.getApplication("currentDataSource"))){
            return ok(callbackparam + "([{message:'nameIsExist'}])");
        }
        User user =new User();
        user.id=UUIDUtil.getUUID();
        user.name=form.data().get("name");
        user.password= MD5Util.MD5(form.data().get("password"));
        user.tel=form.data().get("mail");
        user.type=form.data().get("type");
        user.updateuser=user.id;
        user.updatetime=new Date();
        User.insert(user, ConfigUtil.getApplication("currentDataSource"));
        return ok(callbackparam + "([{message:'注册成功!'}])");
    }

}
