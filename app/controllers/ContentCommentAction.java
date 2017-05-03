package controllers;

import com.avaje.ebean.SqlRow;
import models.Column;
import models.ContentComment;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.mvc.Security;
import util.ConfigUtil;
import util.Constant;
import util.DateUtil;
import util.ObjectUtil;
import views.html.contentManage.contentCommentManage.contentCommentManage;
import views.html.countManager.commentCount.commentCount;
import views.html.countManager.commentCount.commentCountIndex;
/**
 * Created by zuopanpan on 2016/3/9.
 */
@Security.Authenticated(SecureAdmin.class)
public class ContentCommentAction extends Controller {
    /**
     * 初始查询评论列表
     *
     * @param
     * @return
     */
    public static Result list() {
        //查询评论列表
        List<SqlRow> cclist = ContentComment.selectMapList(null,ConfigUtil.getApplication("currentDataSource"));
        return ok(contentCommentManage.render(cclist));
    }
    /**
     * 批量审核评论
     *
     * @param
     * @return
     */
    public static Result auditComments() {
        Form<ContentComment> form=Form.form(ContentComment.class).bindFromRequest();
        ContentComment contentComment=form.get();
        contentComment.updatetime=new Date();
        contentComment.updateuser=session("id");
        String ids=form.data().get("ids");
        ContentComment.auditComments(ids,contentComment, ConfigUtil.getApplication("currentDataSource"));
        return ok("审核成功！");
    }
    /**
     * 批量删除
     *
     * @param
     * @return
     */
    public static Result deleteComments() {
        Form<ContentComment> form=Form.form(ContentComment.class).bindFromRequest();
        String ids=form.data().get("ids");
        ContentComment.deleteComments(ids,ConfigUtil.getApplication("currentDataSource"));
        return ok("删除成功！");
    }
    /**
     * 返回评论统计查询页面
     * @param
     * @return
     */
    public static Result commentCountIndex() {
        Map<String,Object> map=new HashMap<>();
        map.put("del", Constant.DEL_NO);
        List<Column> columnList=Column.selectList(map,ConfigUtil.getApplication("currentDataSource"));
        return ok(commentCountIndex.render(columnList, DateUtil.yearList()));
    }
    /**
     * 查询统计列表
     * @param
     * @return
     */
    public static Result commentCountList() {
        Form<ContentComment> form=Form.form(ContentComment.class).bindFromRequest();
        Map<String,String> searchMap=form.data();
        if(ObjectUtil.isNull(searchMap.get("year"))){
            searchMap.put("year",DateUtil.nowYear());
        }
        List<SqlRow> cclist = ContentComment.commentCountList(searchMap,ConfigUtil.getApplication("currentDataSource"));
        return ok(commentCount.render(cclist));
    }





}
