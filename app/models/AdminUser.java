package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Http;
import util.FileUtil;
import util.ObjectUtil;
import util.UUIDUtil;

import javax.persistence.*;
import java.io.*;
import java.util.*;

/**
 * 管理员用户
 * Created by zuopanpan on 2016/2/18.
 */
@Entity
@Table(name = "M_AdminUser")
public class AdminUser extends Model {
    @Id
    public String id;
    /**
     * 用户名
     */
    public String name;
    /**
     * 密码
     */
    public String password;
    /**
     * 姓名
     */
    public String tname;
    /**
     * 电话
     */
    public String tel;
    /**
     * 邮箱
     */
    public String mail;
    /**
     * 状态  0:正常；1:冻结；2:删除
     */
    @javax.persistence.Column(name = "audit1", nullable = true, length = 1)
    public String audit;
    /**
     * 登录时间
     */
    public Date logintime;
    /**
     * 登录Ip
     */
    public String loginip;
    /**
     * 更新人
     */
    public String updateuser;
    /**
     * 更新时间
     */
    public Date updatetime;
    /**
     * 站点id
     */
    public String station_id;
    /**
     * 部门id
     */
    public String dept_id;
    /**
     * 头像
     */
    public String image;

    /**
     * 条件查询
     *
     * @param map
     * @return adminUser
     */
    @Transactional
    public static List<AdminUser> selectList(Map<String, Object> map, String dataSourceType) {
        List<AdminUser> list = new ArrayList<>();
        if (ObjectUtil.isNotNull(map)) {
            list = Ebean.getServer(dataSourceType).find(AdminUser.class).where().allEq(map).findList();
        } else {
            list = Ebean.getServer(dataSourceType).find(AdminUser.class).findList();
        }
        return list;
    }

    /**
     * 登录
     *
     * @param map
     * @return adminUser
     */
    @Transactional
    public static AdminUser login(Map<String, Object> map, String dataSourceType) {
        List<AdminUser> list = Ebean.getServer(dataSourceType).find(AdminUser.class).where().allEq(map).findList();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 添加
     *
     * @param adminUser
     * @return adminUser
     */
    @Transactional
    public static AdminUser insert(AdminUser adminUser, String role_id, String dataSourceType) {

        UserRole userRole = new UserRole();
        userRole.id = UUIDUtil.getUUID();
        userRole.role_id = role_id;
        userRole.user_id = adminUser.id;
        //添加用户角色信息
        UserRole.insert(userRole,dataSourceType);
        //添加用户信息
        Ebean.getServer(dataSourceType).save(adminUser);
        return adminUser;
    }

    /**
     * 修改
     *
     * @param adminUser
     * @return adminUser
     */
    @Transactional
    public static AdminUser update(AdminUser adminUser, UserRole userRole, String dataSourceType) {
        //修改用户角色信息
        userRole.user_id = adminUser.id;
        UserRole.update(userRole,dataSourceType);
        //修改用户信息
        Ebean.getServer(dataSourceType).update(adminUser);
        return adminUser;
    }

    /**
     * 修改
     *
     * @param adminUser
     * @return adminUser
     */
    @Transactional
    public static AdminUser update(AdminUser adminUser, String dataSourceType) {
        Ebean.getServer(dataSourceType).update(adminUser);
        return adminUser;
    }

    /**
     * 更新登录IP,时间
     *
     * @param adminUser
     * @return adminUser
     */
    @Transactional
    public static AdminUser setLoginlog(AdminUser adminUser, String site_id, String dataSourceType) {
        //更新用户登录日志
        LoginLogs loginLogs = new LoginLogs();
        loginLogs.id = UUIDUtil.getUUID();
        loginLogs.loginip = adminUser.loginip;
        loginLogs.loginuser = adminUser.id;
        loginLogs.updatetime = new Date();
        loginLogs.station_id = site_id;
        LoginLogs.insert(loginLogs,dataSourceType);
        //更新最后登录时间，IP

        AdminUser adminUser1 = new AdminUser();
        adminUser1.id = adminUser.id;
        adminUser1.loginip = loginLogs.loginip;
        adminUser1.logintime = loginLogs.updatetime;
        Ebean.getServer(dataSourceType).update(adminUser1);
        return adminUser1;
    }

    /**
     * 根据id查询对象
     *
     * @param id
     * @return adminUser
     */
    @Transactional
    public static AdminUser getObject(String id, String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(AdminUser.class).where().idEq(id).findUnique();
    }

    public static AdminUser modifyUserInfo(Http.MultipartFormData body, Form<AdminUser> form, String userId, String dataSourceType) {
        AdminUser adminUser = form.get();
        adminUser.updateuser = userId;
        adminUser.updatetime = new Date();
        List<Http.MultipartFormData.FilePart> filePartList = body.getFiles();
        adminUser.image = null;
        //查询配置
        Config config = Config.getConfig(dataSourceType);
        if (ObjectUtil.isNotNull(config)) {

            //保存文件路径
            for (int i = 0; i < filePartList.size(); i++) {
                Http.MultipartFormData.FilePart filePart = filePartList.get(i);
                String fileName = filePart.getKey();
                switch (fileName) {
                    case "image":
                        //删除本地文件
                        AdminUser tempUser = AdminUser.getObject(adminUser.id, dataSourceType);
                        if (ObjectUtil.isNotNull(tempUser.image)) {
                            FileUtil.delete(config.htmlpath + tempUser.image);
                        }
                        String path = saveFile(filePart, dataSourceType);
                        adminUser.image = path;
                        break;
                }
            }
        }
        Ebean.getServer(dataSourceType).update(adminUser);
        return adminUser;
    }


    /**
     * 保存文件
     *
     * @param filePart
     */
    private static String saveFile(Http.MultipartFormData.FilePart filePart, String dataSourceType) {
        //查询配置
        Config config = Config.getConfig(dataSourceType);
        String path = "";
        if (ObjectUtil.isNotNull(config)) {
            String fileName = filePart.getFilename();
            File file = filePart.getFile();

            try {
                FileInputStream is = new FileInputStream(file);
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                String dirName = filePart.getKey();
                //图片上传插件默认提交name=file
                if ("file".equals(dirName)) {
                    dirName = "photos";
                } else if ("thumbnail_".equals(dirName)) {
                    dirName = "thumbnail";
                }
                File sf = new File(config.wordpath + dirName);
                if (!sf.exists()) {
                    sf.mkdirs();
                }
                String prefix = fileName.substring(fileName.lastIndexOf("."));
                path = sf.getPath() + "/" + UUIDUtil.getUUID() + prefix;
                OutputStream os = new FileOutputStream(path);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
                path = path.substring(config.wordpath.length(), path.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }


    /**
     * 重名校验
     *
     * @param existMap
     * @return adminUser
     */
    @Transactional
    public static Boolean isExist(Map<String, String> existMap, String dataSourceType) {
        StringBuilder sql = new StringBuilder("select * from M_AdminUser au where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(existMap)) {
            if (ObjectUtil.isNotNull(existMap.get("name"))) {
                sql.append(" and au.name=?");
                args.add(existMap.get("name"));
            }
            if (ObjectUtil.isNotNull(existMap.get("del"))) {
                sql.append(" and au.del!=?");
                args.add(existMap.get("del"));
            }
            if (ObjectUtil.isNotNull(existMap.get("id"))) {
                sql.append(" and au.id!=?");
                args.add(existMap.get("id"));
            }
        }
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> list = sqlQuery.findList();
        if (ObjectUtil.isNotNull(list)) {
            return true;
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
