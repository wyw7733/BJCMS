package controllers;

import com.google.common.collect.Maps;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import views.html.systemManager.statics.generateIndex;
import views.html.systemManager.statics.generateColumn;
import views.html.systemManager.statics.generateContent;
import views.html.systemManager.statics.staticIndex;

import java.util.List;
import java.util.Map;

/**
 * Created by houyaohui on 2016/1/21.
 */
@Security.Authenticated(SecureAdmin.class)
public class StacicAction extends Controller {
    /**
     * 查询首页
     *
     * @param
     * @return
     */
    public static Result staticIndex() {
        return ok(staticIndex.render());
    }

    /**
     * 生成首页
     *
     * @return
     */
    public static Result generateIndex() {
        Column.generateColumn(false, false,ConfigUtil.getApplication("currentDataSource"));
        return ok("生成成功");
    }

    /**
     * 生成栏目
     *
     * @return
     */
    public static Result generateColumn() {
        String column_id = Form.form().bindFromRequest().data().get("column_id");
        Column.generateSelectColumn(column_id,ConfigUtil.getApplication("currentDataSource"));
        return ok("生成成功");
    }

    /**
     * 生成文章
     *
     * @return
     */
    public static Result generateContent() {
        String releasetime = Form.form().bindFromRequest().data().get("releasetime");
        String column_id = Form.form().bindFromRequest().data().get("column_id");
        Content.generateContents(releasetime, column_id,ConfigUtil.getApplication("currentDataSource"));
        return ok("生成成功");
    }

    /**
     * 跳转到生成首页页面
     *
     * @param
     * @return
     */
    public static Result staticHtml() {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("del", Constant.DEL_NO);
        searchMap.put("type_id", Constant.MOBILETYPE_INDEX);
        List<Mobile> mobileList = Mobile.selectList(searchMap, ConfigUtil.getApplication("currentDataSource"));
        return ok(generateIndex.render(mobileList));
    }

    /**
     * 跳转到生成栏目页面
     *
     * @param
     * @return
     */
    public static Result staticColumn() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("del", Constant.DEL_NO);
        //查询栏目类别
        List<ColumnType> typeList = ColumnType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        for (ColumnType type : typeList) {
            map.put("type_id", type.id);
            type.columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        }
        return ok(generateColumn.render(typeList));
    }

    /**
     * 跳转生成内容页面
     *
     * @param
     * @return
     */
    public static Result staticContent() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("del", Constant.DEL_NO);
        //查询栏目类别
        List<ColumnType> typeList = ColumnType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        for (ColumnType type : typeList) {
            map.put("type_id", type.id);
            type.columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        }
        return ok(generateContent.render(typeList));
    }

}
