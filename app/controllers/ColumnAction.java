package controllers;

import com.avaje.ebean.SqlRow;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.ObjectUtil;
import util.UUIDUtil;
import views.html.columnManage.columnAdd;
import views.html.columnManage.columnModify;
import views.html.columnManage.columnManage;
import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zuopanpan on 2016/1/19.
 */
@Security.Authenticated(SecureAdmin.class)
public class ColumnAction extends Controller {
    /**
     * 初始查询栏目列表
     *
     * @param
     * @return
     */
    public static Result columnList() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("del", Constant.DEL_NO);
        //查询未删除栏目列表
        List columnlist = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        //查询未删除栏目类型列表
        List typeList = ColumnType.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        //将类型列表放进栏目列表
        columnlist.addAll(typeList);
        String columnList = JSON.toJSONString(columnlist);
        return ok(columnList);
    }

    /**
     * 初始栏目页面
     *
     * @param
     * @return
     */
    public static Result index() {
        return ok(columnManage.render());
    }

    /**
     * 跳转新增页面
     *
     * @param
     * @return
     */
    public static Result toAdd() {
        Form<Column> form = Form.form(Column.class).bindFromRequest();
        String type_id = form.get().type_id;
        String id = form.get().id;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("del", Constant.DEL_NO);
        //查询未删除栏目列表
        List<Column> columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        //查询未删除栏目类型列表
        List<ColumnType> typeList = ColumnType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        map.put("type_id", Constant.MOBILETYPE_COLUMN);
        //查询未删除栏目模板列表
        List<Mobile> mobileColumn = Mobile.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        map.put("type_id", Constant.MOBILETYPE_CONTENT);
        //查询未删除内容模板列表
        List<Mobile> mobileContent = Mobile.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(columnAdd.render(type_id, id, columnList, mobileColumn, mobileContent, typeList));
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    public static Result add() {
        Form<Column> form = Form.form(Column.class).bindFromRequest();
        Column column = form.get();
        column.id = UUIDUtil.getUUID();
        //设置栏目创建日期
        column.createtime = new Date();
        column.del = Constant.DEL_NO;
        column.updatetime = new Date();
        column.updateuser = session("id");
        column.station_id=session("site_id");
        String parent = null;
        //若栏目父节点不为null,则设置父节点值
        if (ObjectUtil.isNotNull(column.parent)) {
            parent = column.parent;
        }
        //设置code
        column.code = Column.getCodeVal(parent, "select MAX(code) as code from C_Column where del='0'",ConfigUtil.getApplication("currentDataSource"));
        Column.insert(column,ConfigUtil.getApplication("currentDataSource"));
        return ok("添加成功！");
    }

    /**
     * 跳转修改页面
     *
     * @param id
     * @return
     */
    public static Result toModify(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("del", Constant.DEL_NO);
        //查询未删除栏目列表
        List<Column> columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        //查询未删除栏目类型列表
        List<ColumnType> typeList = ColumnType.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        Column column = Column.getObject(id,ConfigUtil.getApplication("currentDataSource"));
        //查询未删除栏目模板列表
        map.put("type_id", Constant.MOBILETYPE_COLUMN);
        List<Mobile> mobileColumn = Mobile.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        //查询未删除内容模板列表
        map.put("type_id", Constant.MOBILETYPE_CONTENT);
        List<Mobile> mobileContent = Mobile.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(columnModify.render(column, columnList, typeList, mobileColumn, mobileContent));
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    public static Result modify() {
        Form<Column> form = Form.form(Column.class).bindFromRequest();
        Column column = form.get();
        column.updatetime = new Date();
        column.updateuser = session("id");
        Column.upadte(column,ConfigUtil.getApplication("currentDataSource"));
        return ok("修改成功！");
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    public static Result delete() {
        Form<Column> form = Form.form(Column.class).bindFromRequest();
        String id = form.data().get("id");
        //查询子节点
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parent", id);
        map.put("del", Constant.DEL_NO);
        List<Column> columnList = Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        String result = "删除成功！";
        //若有子节点,则不能删除
        if (columnList.size() > 0) {
            result = "该栏目下有子栏目，不能删除！";
        } else {
            //查询该栏目下的子文章
            Map<String, String> searchMap = new HashMap<>();
            searchMap.put("column_id", id);
            List<SqlRow> contentList = Content.selectList(searchMap,ConfigUtil.getApplication("currentDataSource"));
            if (contentList.size() > 0) { //若有子文章,则不能删除,若没有则删除
                result = "该栏目下有子文章，不能删除！";
            } else {
                //设置删除状态
                Column column = new Column();
                column.id = id;
                column.del = Constant.DEL_YES;
                Column.upadte(column,ConfigUtil.getApplication("currentDataSource"));
                Column.generateColumn(true, true,ConfigUtil.getApplication("currentDataSource"));
            }
        }
        return ok(result);
    }

    /**
     * 联动显示栏目类型
     *
     * @param
     * @return
     */
    public static Result linkageShowType() {
        Form<Column> form = Form.form(Column.class).bindFromRequest();
        //根据id查询栏目类型
        Column column = Column.getObject(form.get().id,ConfigUtil.getApplication("currentDataSource"));
        String type_id = column.type_id;
        return ok(type_id);
    }

    /**
     * 树形栏目
     *
     * @return
     */
    public static Result selectColumnJsonTree() {
        String jsonStr = Column.selectColumnJsonTree(Form.form(),false, session("dept_id"),ConfigUtil.getApplication("currentDataSource"));
        return ok(jsonStr);
    }
    /**
     * 树形栏目
     *
     * @return
     */
    public static Result selectColumnJsonTreeDep() {
        String jsonStr = Column.selectColumnJsonTreeDep(Form.form(), false, session("dept_id"),ConfigUtil.getApplication("currentDataSource"));
        return ok(jsonStr);
    }
    /**
     * 有空选择的树形栏目
     *
     * @return
     */
    public static Result selectJsonTreeHasNullValue() {
        String jsonStr = Column.selectColumnJsonTree(Form.form(), true, session("dept_id"),ConfigUtil.getApplication("currentDataSource"));
        return ok(jsonStr);
    }
}

