package controllers;

import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import util.ConfigUtil;
import util.MD5Util;
import util.ObjectUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/4/7.
 */
public class WebsiteLoginUserAction extends Controller{
    /**
     * 前台登录
     *
     * @return
     */
    public static Result login() {
        DynamicForm form = Form.form().bindFromRequest();
        String callbackparam = form.data().get("callbackparam");
        String name=form.data().get("name");
        String password=form.data().get("password");
        //用户名密码是否为空
        if (ObjectUtil.isNull(name)) {//用户名为空
            return ok(callbackparam + "({message:'nameISNULL'})");
        }
        if (ObjectUtil.isNull(password)) {//密码为空
            return ok(callbackparam + "({message:'passwordISNULL'})");
        }
        Map<String,Object> searchMap=new HashMap<>();
        searchMap.put("name",name);
        searchMap.put("password", MD5Util.MD5(password) );
        String userid=User.login(searchMap,ConfigUtil.getApplication("currentDataSource"));
        if("namedontExist".equals(userid)){//用户不存在
            return ok(callbackparam + "({message:'nameerror'})");
        }else if(userid==null){//密码错误
            return ok(callbackparam + "({message:'passworderror'})");
        } else{//成功
            User user=User.getObject(userid, ConfigUtil.getApplication("currentDataSource"));
            return ok(callbackparam + "({message:'"+user.name+"',id:'"+userid+"'})");
        }
    }
}
