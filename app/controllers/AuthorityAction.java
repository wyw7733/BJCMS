package controllers;

import models.Authority;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/3/2.
 */
@Security.Authenticated(SecureAdmin.class)
public class AuthorityAction extends Controller {
    /**
     * 初始查询权限树列表
     * @return
     */
    public static Result selectTree() {
        Form<Authority> form=Form.form(Authority.class).bindFromRequest();
        String role_id=form.data().get("roleId");
        Map<String,Object> map=new HashMap<>();
        map.put("del", Constant.DEL_NO);
        map.put("role_id", role_id);
        String authorityTreeList= Authority.jsonTree(map, ConfigUtil.getApplication("currentDataSource"));
        return ok(Json.parse(authorityTreeList));
    }
}
