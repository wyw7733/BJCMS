package controllers;

import com.google.common.collect.Maps;
import models.Config;
import models.Mobile;
import models.MobileType;
import models.Site;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.*;
import views.html.mobileManage.mobile.mobileAdd;
import views.html.mobileManage.mobile.mobileIndex;
import views.html.mobileManage.mobile.mobileList;
import views.html.mobileManage.mobile.mobileModify;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Security.Authenticated(SecureAdmin.class)
public class MobileAction extends Controller {
    private static String ROOT = Config.getConfig(ConfigUtil.getApplication("currentDataSource")).templatepath;
    private static Site site = Site.getSite(ConfigUtil.getApplication("currentDataSource"));

    /**
     * 加载模版内容首页
     *
     * @return
     */
    public static Result index() {
        return ok(mobileIndex.render());
    }

    /**
     * 创建模版
     *
     * @return
     */
    public static Result mobileAdd() {
        Form<Mobile> form = Form.form(Mobile.class).bindFromRequest();
        Mobile mobile = form.get();

        //查询模版类型
        MobileType mobileType = MobileType.getObject(mobile.type_id, ConfigUtil.getApplication("currentDataSource"));
        mobile.id = UUIDUtil.getUUID();
        String fileName = mobile.name;
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //如果后缀不为.ftl
        if (!"ftl".equalsIgnoreCase(prefix)) {
            fileName += ".ftl";
        }
        String path = ROOT + mobileType.name + "/" + fileName;
        mobile.path = path;
        mobile.del = Constant.DEL_NO;
        mobile.updatetime = new Date();
        mobile.updateuser = session("id");
        mobile.station_id = site.id;
        mobile.insert(mobile, ConfigUtil.getApplication("currentDataSource"));
        FileUtil.writeFile(path, mobile.memo);
        return ok("创建成功");
    }

    /**
     * 跳转至新增模版页面
     *
     * @return
     */
    public static Result toMobileAdd() {
        //获取模版类型(未删除)
        Map<String, Object> map = new HashMap<>();
        map.put("del", Constant.DEL_NO);
        List<MobileType> mobileTypeList = MobileType.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        return ok(mobileAdd.render(mobileTypeList));
    }

    /**
     * 跳转至修改模版页面
     *
     * @return
     */
    public static Result toModify() {
        DynamicForm form = Form.form().bindFromRequest();
        String path = form.data().get("path") + "";
        Mobile mobile = new Mobile();
        mobile.path = path;
        //查询文件
        Map<String, Object> map = Maps.newHashMap();
        map.put("path", path);
        map.put("del", Constant.DEL_NO);
        List<Mobile> list = Mobile.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        if (ObjectUtil.isNotNull(list)) {
            mobile = list.get(0);
        }
        map.remove("path");
        //获取模版类型(未删除)
        List<MobileType> mobileTypeList = MobileType.selectList(map, ConfigUtil.getApplication("currentDataSource"));
        String text = "";
        try {
            text = FileUtil.readFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ok(mobileModify.render(path, text, mobile, mobileTypeList));
    }

    /**
     * 修改模版
     *
     * @return
     */
    public static Result mobileModify() throws InvocationTargetException, IllegalAccessException {
        Form<Mobile> form = Form.form(Mobile.class).bindFromRequest();
        String oldPath = form.data().get("oldPath") + "";
        Mobile mobile = form.get();
        //获取模版类型id
        Object type_id1 = form.data().get("type_id");
        String type_id = type_id1.toString();
        //查询模版
        Map<String, Object> map = Maps.newHashMap();
        map.put("path", oldPath);
        map.put("del", Constant.DEL_NO);
        List<Mobile> list = Mobile.selectList(map, ConfigUtil.getApplication("currentDataSource"));

        if (ObjectUtil.isNotNull(list)) {
            Mobile temp = new Mobile();
            Mobile temp1 = list.get(0);
            temp.id = temp1.id;

            //更换模版类型
            if (!temp1.type_id.equals(type_id)) {
                temp.type_id = type_id;
            } else {
                temp.type_id = temp1.type_id;
            }
            String begin = ROOT;
            MobileType mobileType = MobileType.getObject(temp.type_id, ConfigUtil.getApplication("currentDataSource"));
            String path = begin + mobileType.name + "/" + mobile.name;
            //如果后缀不为.ftl
            String prefix = path.substring(path.lastIndexOf(".") + 1);
            if (!"ftl".equalsIgnoreCase(prefix)) {
                path += ".ftl";
            }
            temp.path = path;
            //修改文件
            temp.updatetime = new Date();
            temp.updateuser = session("id");
            temp.station_id = site.id;
            temp.memo = form.data().get("memo");
            temp.name = form.data().get("name");
            Mobile.update(temp, ConfigUtil.getApplication("currentDataSource"));
            //删除旧文件
            FileUtil.delete(oldPath);
            //写人新文件
            System.out.print("ROOT***************" + begin);
            FileUtil.writeFile(temp.path, temp.memo);
        }

        return ok("修改成功");
    }

    /**
     * 删除模版
     *
     * @return
     */
    public static Result mobileDelete() {
        DynamicForm form = Form.form().bindFromRequest();
        //获取删除文件的路径（批量）
        String paths = form.data().get("paths") + "";
        //循环删除文件
        for (String path : paths.split("-")) {
            //查询文件
            Map<String, Object> map = Maps.newHashMap();
            map.put("path", path);
            map.put("del", Constant.DEL_NO);
            List<Mobile> list = Mobile.selectList(map, ConfigUtil.getApplication("currentDataSource"));
            //逻辑删除
            if (ObjectUtil.isNotNull(list)) {
                Mobile temp = new Mobile();
                Mobile temp1 = list.get(0);
                temp.id = temp1.id;
                temp.del = Constant.DEL_YES;
                Mobile.update(temp, ConfigUtil.getApplication("currentDataSource"));
                //删除文件
                FileUtil.delete(path);
            }

        }
        return ok("删除成功");
    }

    /**
     * 查询模版列表
     *
     * @param path
     * @return
     */

    public static Result list(String path) {
        List<Map<String, String>> paths = new ArrayList<Map<String, String>>();

        try {
            //读取文件列表。如果为路径为空则读取根目录，反之则读取子目录
            if ("".equals(path)) {
                paths = FileUtil.openDir(ROOT);
            } else {
                path = path.replace("\\", "/").replace("//", "/").replace("全部文件", ROOT);
                paths = FileUtil.openDir(path);
            }
            path = path.replace(ROOT, "全部文件/");
            path = path.replace("//", "/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok(mobileList.render(paths, path, ""));
    }

    /**
     * 搜索文件或目录
     *
     * @return
     */
    public static Result search() {
        List<Map<String, String>> paths = new ArrayList<Map<String, String>>();
        DynamicForm form = Form.form().bindFromRequest();
        Map<String, String> map = form.data();
        //获取搜索目录
        String path = map.get("path");
        //获取搜索关键字
        String keyWord = map.get("keyWord");
        if (ObjectUtil.isNull(path)) {
            path = ROOT;
        }
        try {
            path = path.replace("\\", "/").replace("//", "/").replace("全部文件", ROOT);
            //如果路径为根路径并且没有输入关键字则打开根目录，反之搜素文件
            if (path.equals(ROOT) && ObjectUtil.isNull(keyWord)) {
                paths = FileUtil.openDir(path);
            } else {
                paths = new FileUtil().searchFile(path, keyWord);
            }
            path = path.replace(ROOT, "全部文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok(mobileList.render(paths, path, keyWord));
    }
}
