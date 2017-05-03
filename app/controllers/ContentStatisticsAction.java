package controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.avaje.ebean.SqlRow;
import models.ContentStatistics;
import models.Page;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import views.html.countManager.contentStatistics.contentStatisticsIndex;
import views.html.countManager.contentStatistics.contentStatisticsList;
import views.html.rankingList.viewList;
import views.html.rankingList.viewIndex;


import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/4/1.
 */
@Security.Authenticated(SecureAdmin.class)
public class ContentStatisticsAction extends Controller {
    /**
     * 初始栏目页面
     *
     * @param
     * @return
     */
    public static Result index() {
        return ok(contentStatisticsIndex.render());
    }
    /**
     * 初始栏目访问量页面
     *
     * @param
     * @return
     */
    public static Result list() {
        String path=request().path();
        Form<ContentStatistics> form=Form.form(ContentStatistics.class).bindFromRequest();
        Map<String,String> searchMap= form.data();
        searchMap.put("path",path);
        searchMap.put("del", Constant.DEL_NO);
        List<SqlRow> contentStatisticslist=ContentStatistics.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        return ok(contentStatisticsList.render(path,contentStatisticslist,form.data()));
    }
    /**
     * 返回点击率查询页
     *
     * @param
     * @return
     */
    public static Result viewIndex() {
        String path=request().path();
        return ok(viewIndex.render(path));
    }
    /**
     * 初始查询点击率
     *
     * @param
     * @return
     */
    public static Result viewList() {
        String path=request().path();
        Form<ContentStatistics> form=Form.form(ContentStatistics.class).bindFromRequest();
        Map<String,String> searchMap=form.data();
        searchMap.put("path",path);
        Page page=ContentStatistics.contentView(searchMap, ConfigUtil.getApplication("currentDataSource"));
        return ok(viewList.render(page,path));
    }
    /**
     * 分页查询点击率
     *
     * @param
     * @return
     */
    public static Result viewsDayListByPage() {
        Form<ContentStatistics> form=Form.form(ContentStatistics.class).bindFromRequest();
        Page page=ContentStatistics.contentView(form.data(),ConfigUtil.getApplication("currentDataSource"));
        SerializeConfig config = new SerializeConfig();
        config.put(java.sql.Date.class, new SimpleDateFormatSerializer(
                "yyyy-MM-dd"));

        return ok(JSONArray.toJSONString(page, config));
    }

}
