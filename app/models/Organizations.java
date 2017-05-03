package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Http;
import util.ConfigUtil;
import util.FileUtil;
import util.ObjectUtil;
import util.UUIDUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * 组织机构
 * Created by zuopanpan on 2016/7/4.
 */
@Entity
@Table(name="M_Organizations")
public class Organizations extends Model {
    @Id
    public String id;
    /**名称*/
    public String name;
    /**是否删除0：未删除，1：已删除*/
    public String del;
    /**父id*/
    public String parent;
    /**更新时间*/
    public Date updatetime;
    /**更新人*/
    public String updateuser;
    /**code*/
    public String code;
    /**职责*/
    public String dutymemo;
    /**
     *修改
     * @param organizations
     */
     /*     /**
@Transactional
    public static void update(Organizations organizations,String dataSourceType) {
        //修改用户信息
    //    Ebean.update(organizations);
        Ebean.getServer(dataSourceType).update(organizations);
    }*/
    /**
     * 更新
     * @param @param organizations
     * @return
     */
    @Transactional
    public static Organizations update(Organizations organizations,String dataSourceType)  {
        String sql="update M_Organizations set dutymemo='"+organizations.dutymemo+"' where id='"+organizations.id+"'";
        Ebean.getServer(dataSourceType).update(organizations);
        SqlUpdate sqlUpdate =Ebean.getServer(dataSourceType).createSqlUpdate(sql);
        sqlUpdate.execute();
        return organizations;
    }
    @Transactional
    public static Organizations insert(Organizations organizations,String dataSourceType)  {
        Ebean.getServer(dataSourceType).insert(organizations);
        return organizations;
    }
    /**
     * 条件查询
     * @param
     * @return
     */
    @Transactional
    public static List<SqlRow> selectList(Map<String,String> searchMap) {
        StringBuilder sql = new StringBuilder("select * from M_Organizations org where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("code"))) {
                sql.append("and org.code like'").append(searchMap.get("code")).append("'");
            }
            if (ObjectUtil.isNotNull(searchMap.get("parent"))) {
                sql.append("and org.parent=?");
                args.add(searchMap.get("parent"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("del"))) {
                sql.append("and org.del=?");
                args.add(searchMap.get("del"));
            }

        }
        sql.append("order by org.code");
        SqlQuery sqlQuery = Ebean.createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();

        return rowList;
    }
    /**
     * 根据id查询对象
     * @param
     * @return
     */
    @Transactional
    public static Organizations getObject(String id) {
        return Ebean.find(Organizations.class).where().idEq(id).findUnique();
    }

    /**
     * 保存文件
     *
     * @param filePart
     */
    @Transactional
    private static String saveFile(Http.MultipartFormData.FilePart filePart) {
        //查询配置
        Config config = Config.getConfig(ConfigUtil.getApplication("currentDataSource"));
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

    @Override
    public String toString() {
        return "Organizations{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", del='" + del + '\'' +
                ", parent='" + parent + '\'' +
                ", updatetime=" + updatetime +
                ", updateuser='" + updateuser + '\'' +
                ", code='" + code + '\'' +
                ", dutymemo='" + dutymemo + '\'' +
                '}';
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

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDutymemo() {
        return dutymemo;
    }

    public void setDutymemo(String dutymemo) {
        this.dutymemo = dutymemo;
    }
}
