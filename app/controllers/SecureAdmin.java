package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import util.ObjectUtil;
import views.html.login;

import java.lang.annotation.Annotation;

/**
 * Created by yangshaofeng on 2016/4/5.
 */
@Security.Authenticated(SecureAdmin.class)
public class SecureAdmin extends Security.Authenticator{
    @Override
    public String getUsername(Http.Context context) {
        Http.Request request = context.request();
        String username = request.uri();
        boolean flag = true;

        if(username.contains("workFlowListWebsite")) {
            flag = false;
        }
        if(username.contains("workFlowTableListWebsite")) {
            flag = false;
        }
        if(username.contains("selectContentByPage")) {
            flag = false;
        }
        if(username.contains("tourismDirectoryListFore")) {
            flag = false;
        }
        if(username.contains("interactListWebsite")) {
            flag = false;
        }
        if(username.contains("interactAdd")) {
            flag = false;
        }
        if(username.contains("contentReadsSizeMosify")) {
            flag = false;
        }
        if(username.contains("blogrollListWeb")) {
            flag = false;
        }
        if(username.contains("conAccessorysDownloadWebsite")) {
            flag = false;
        }
        if(username.contains("selectLeaderWebSite")) {
            flag = false;
        }
        if(username.contains("selectOrgInfoWebSite")) {
            flag = false;
        }
        if(username.contains("leaderInfoWebSite")) {
            flag = false;
        }
        if(flag) {
            String id = context.session().get("id");
            if(ObjectUtil.isNotNull(id)) {
                return id;
            }
            return null;
        }
        return "1";

    }
    @Override
    public Result onUnauthorized(Http.Context context) {
        return ok(login.render("请先登录！"));
    }

}
