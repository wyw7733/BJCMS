package controllers;

import models.AdminUser;
import models.Site;
import play.cache.Cache;
import play.mvc.Controller;
import play.data.Form;
import play.mvc.Result;
import util.ConfigUtil;
import util.MD5Util;
import util.ObjectUtil;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import views.html.*;

/**
 * Created by zuopanpan on 2016/2/22.
 */
public class LoginUserAction extends Controller {
    /**
     * @return
     * @Description: 用户登录
     */
    public static Result login() {
        Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        String name = form.data().get("name");
        String password = form.data().get("password");
        //验证码判断
        if (checkRand()) {
            //用户名密码是否为空
            if (ObjectUtil.isNull(name)) {
                return ok(login.render("您输入的用户名为空！"));
            }
            if (ObjectUtil.isNull(password)) {
                return ok(login.render("您输入的密码为空！"));
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("password", MD5Util.MD5(password));
            AdminUser adminUser = AdminUser.login(map, ConfigUtil.getApplication("currentDataSource"));
            //登录判断
            if (null != adminUser) {
                if ("1".equals(adminUser.audit)) {//冻结
                    return ok(login.render("该用户已冻结！请联系管理员"));
                } else if ("2".equals(adminUser.audit)) {//删除
                    return ok(login.render("该用户已删除！请联系管理员"));
                } else {//成功
                    //设置缓存
                    setCache(adminUser);
                    //更新登录时间，日志
                    updateLoginLog(adminUser);
    //                VerifyCodeAction.verifyCode();
               //     return redirect("admin");
                    return ok(login.render("true"));
                }
            } else {
                return ok(login.render("用户名或密码错误！"));
            }
        } else {
            return ok(login.render("您输入的验证码有误！"));
        }
    }

    /**
     * @return
     * @Description: 判断验证码
     */
    private static boolean checkRand() {
       /* Form<AdminUser> form = Form.form(AdminUser.class).bindFromRequest();
        //页面输入的验证码
        String tvery = form.data().get("tvery");
        //生成的验证码
       // String rand = session("verifyCode");
        String rand = VerifyCodeAction.test;
        //session().remove("verifyCode");
        VerifyCodeAction.test = null;
        //判断页面输入的验证码是否为空
        if (!"".equals(tvery) && null != tvery) {
            //判断页面输入的验证码是否等于session中保存的验证码
            if (!tvery.equalsIgnoreCase(rand)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }*/
        return true;
    }

    /**
     * @return
     * @Description: 退出登录
     */
    public static Result logout() {
        //清除session
       // session().clear();
        session().remove("id");
        Cache.remove("id");
        return ok(login.render(""));
    }

    /**
     * @return
     * @Description: 更新登录时间,日志
     */
    public static void updateLoginLog(AdminUser adminUser) {
        String ip = request().getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request().getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request().getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request().remoteAddress();
        }
        //用户最新登录ip、时间
        adminUser.loginip=ip;
        AdminUser.setLoginlog(adminUser, session("site_id"),ConfigUtil.getApplication("currentDataSource"));
    }
    /**
     * @return
     * @Description: 设置缓存
     */
    public static void setCache(AdminUser adminUser) {
        Cache.set("id", adminUser.id, 7200);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String lastlogintime="";
        String lastloginip="";
        if(ObjectUtil.isNotNull(adminUser.logintime)){
            lastlogintime=sdf.format(adminUser.logintime);
        }
        if(ObjectUtil.isNotNull(adminUser.loginip)){
            lastloginip=adminUser.loginip;
        }
        session("lastlogintime", lastlogintime);
        session("lastloginip", lastloginip);
        session("id", adminUser.id);
        session("dept_id", adminUser.dept_id);
        //查询站点信息
        Site site=Site.getSite(ConfigUtil.getApplication("currentDataSource"));
        //判断site是否为空
        if(ObjectUtil.isNotNull(site)){
            session("site_id", site.id);
        }
    }

}
