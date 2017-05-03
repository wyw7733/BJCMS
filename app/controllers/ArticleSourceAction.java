package controllers;

import models.ArticleSource;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.UUIDUtil;
import views.html.contentManage.articleSource.articleSourceAdd;
import views.html.contentManage.articleSource.articleSourceIndex;
import views.html.contentManage.articleSource.articleSourceList;
import views.html.contentManage.articleSource.articleSourceModify;

import java.util.Date;
import java.util.List;

/**
 * 文章来源
* Created by ysf on 2016/3/15.
*/
@Security.Authenticated(SecureAdmin.class)
public class ArticleSourceAction extends Controller {
    /**
     * 跳转主页
     *
     * @param
     * @return
     */
    public static Result toSearch() {

        return ok(articleSourceIndex.render());
    }

    /**
     * 列表查询
     *
     * @param
     * @return
     */
    public static Result searchList() {

        List<ArticleSource> list = ArticleSource.selectList(ConfigUtil.getApplication("currentDataSource"));
        return ok(articleSourceList.render(list));
    }

    /***
     *跳转新增页面
     * @return
     */
    public static Result toAdd() {
        return ok(articleSourceAdd.render());
    }
    /**
     * 添加
     * @param
     * @return
     */
    public static Result add() {
        Form<ArticleSource> form=Form.form(ArticleSource.class).bindFromRequest();
        ArticleSource articleSource=form.get();
        articleSource.id= UUIDUtil.getUUID();
        //添加
        articleSource.updateuser = session("id");
        articleSource.updatetime = new Date();
        articleSource.station_id = session("site_id");
        ArticleSource.insert(articleSource,ConfigUtil.getApplication("currentDataSource"));
        return ok("添加成功！");
    }
    /***
     *跳转修改页面
     * @return
     */
    public static Result toModify() {
        Form<ArticleSource> form=Form.form(ArticleSource.class).bindFromRequest();
        String id=form.get().id;
        ArticleSource articleSource=ArticleSource.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        return ok(articleSourceModify.render(articleSource));
    }
    /**
     * 修改
     * @param
     * @return
     */
    public static Result modify() {
        Form<ArticleSource> form=Form.form(ArticleSource.class).bindFromRequest();
        ArticleSource articleSource=form.get();
        articleSource.updateuser = session("id");
        articleSource.updatetime = new Date();
        articleSource.station_id = session("site_id");
        //添加
        ArticleSource.update(articleSource,ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }
    /**
     * 删除
     * @param
     * @return
     */
    public static Result delete() {
        Form<ArticleSource> form=Form.form(ArticleSource.class).bindFromRequest();
        ArticleSource articleSource=form.get();
        ArticleSource.delete(articleSource,ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功！");
    }

}
