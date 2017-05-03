package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.db.ebean.Model;
import play.mvc.Http;
import util.ObjectUtil;
import util.UUIDUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 组织机构
 * Created by zuopanpan on 2016/7/4.
 */
@Entity
@Table(name="E_OrganizationInfo")
public class OrganizationInfo extends Model {
    @Id
    public String id;
    /**职责*/
    public String duty;

    /**内设机构*/
    public String mechanism;
    /**基本信息*/
    public String basicinfo;
    /**更新时间*/
    public Date updatetime;
    /**是否删除0：未删除，1：已删除*/
    public String del;
    /**更新人*/
    public String updateuser;
    /**站点id*/
    public String station_id;
    /**
     * 查询
     * @param
     */
    @Transactional
    public static OrganizationInfo getOrganizationInfo(String currentDataSource) {
        return Ebean.getServer(currentDataSource).find(OrganizationInfo.class).findUnique();
    }
    /**
     * 添加
     * @param organization
     */
    @Transactional
    public static void insert(OrganizationInfo organization, String currentDataSource) {
        Ebean.getServer(currentDataSource).insert(organization);
    }
    /**
     *修改
     * @param organization
     */
    @Transactional
    public static void update(OrganizationInfo organization,String dataSourceType) {
        //修改用户信息
        Ebean.getServer(dataSourceType).update(organization);
    }

    /**
     * 条件查询
     * @param
     * @return
     */
    @Transactional
    public static List<SqlRow> selectList(Map<String,String> searchMap,String dataSourceType) {
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
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
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
    public static OrganizationInfo getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(OrganizationInfo.class).where().idEq(id).findUnique();
    }

    /**
     * 保存文件
     *
     * @param filePart
     */
    @Transactional
    private static String saveFile(Http.MultipartFormData.FilePart filePart,String dataSourceType) {
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


}
