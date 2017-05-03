package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Http;
import util.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 领导
 * Created by zuopanpan on 2016/7/4.
 */
@Entity
@Table(name="M_Leader")
public class Leader extends Model {
    @Id
    public String id;
    /**机构id*/
    public String org_id;
    /**姓名*/
    public String name;
    /**是否删除0：未删除，1：已删除*/
    public String del;
    /**职务*/
    public String post;
    /**下属*/
    public String subordinate;
    /**职责*/
    public String duty;
    /**照片*/
    public String photo;
    /**更新时间*/
    public Date updatetime;
    /**更新人*/
    public String updateuser;
    /**介绍静态页路径*/
    public String htmlpath;
    /**code*/
    public String code;
    /**
     * 删除
     * @param
     * @return
     */
    @Transactional
    public static void delete(Leader leader) {
        Ebean.delete(Leader.class, leader.id);
    }
    /**
     *条件查询
     * @param
     * @return workFlow
     */
    @Transactional
    public static List<Leader> selectListByMap(Map<String, Object> searchMap) {
        List<Leader> list=new ArrayList<Leader>();
        list =Ebean.find(Leader.class).where().allEq(searchMap).findList();
        return list;
    }
    /**
     * 添加领导信息
     * @param
     * @return
     */
    @Transactional
    public static Leader insert(Http.MultipartFormData body, Form<Leader> form, String id,String dataSourceType) {
        Leader leader =form.get();
        leader.id=UUIDUtil.getUUID();
        leader.del= Constant.DEL_NO;
        leader.updatetime=new Date();
        leader.updateuser=id;
        Organizations org=Organizations.getObject(leader.org_id);

        String dutymemo = form.data().get("dutymemo");
        org.dutymemo = dutymemo;
        Organizations.update(org,dataSourceType);

        leader.code =org.code;
        List<Http.MultipartFormData.FilePart> filePartList = body.getFiles();
        leader.photo=null;
        //查询配置
        Config config = Config.getConfig(ConfigUtil.getApplication("currentDataSource"));
        if (ObjectUtil.isNotNull(config)) {

            //保存文件路径
            for (int i = 0; i < filePartList.size(); i++) {
                Http.MultipartFormData.FilePart filePart = filePartList.get(i);
                String fileName = filePart.getKey();
                String path = saveFile(filePart);
                switch (fileName) {
                    case "photo":
                        leader.photo = path;
                        break;
                }
            }
        }
        Ebean.save(leader);
        //生成信息公开栏目页面
        //Column.generateSelectColumn(Constant.INFOOPEN_COLUMN_ID);
        return leader;
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
    /**
     * 根据id获取对象
     *
     * @param id
     */
    @Transactional
    public static Leader getObject(String id) {
        return Ebean.find(Leader.class).where().idEq(id).findUnique();
    }
    /**
     * 修改对象
     * @param
     * @return
     */
    @Transactional
    public static Leader  update(Http.MultipartFormData body, Form<Leader> form, String id,String dataSourceType) {

        String orgId = form.data().get("orgId");
        String dutymemo = form.data().get("dutymemo");
        Organizations org =  Organizations.getObject(orgId);
        org.dutymemo = dutymemo;
        Organizations.update(org, dataSourceType);

        Leader leader=form.get();
           if(ObjectUtil.isNotNull(leader.id)){
            leader.updateuser=id;
            leader.updatetime=new Date();
            List<Http.MultipartFormData.FilePart> filePartList = body.getFiles();
            leader.photo=null;
            //查询配置
            Config config = Config.getConfig(ConfigUtil.getApplication("currentDataSource"));
            if (ObjectUtil.isNotNull(config)) {

                //保存文件路径
                for (int i = 0; i < filePartList.size(); i++) {
                    Http.MultipartFormData.FilePart filePart = filePartList.get(i);
                    String fileName = filePart.getKey();
                    switch (fileName) {
                        case "photo":
                            //删除本地文件
                            Leader temp = Leader.getObject(leader.id);
                            if (ObjectUtil.isNotNull(temp.photo)) {
                                FileUtil.delete(config.htmlpath + temp.photo);
                            }
                            String path = saveFile(filePart);
                            leader.photo=path;
                            break;
                    }
                }
            }
            Ebean.update(leader);
        }
        //生成信息公开栏目页面
       // Column.generateSelectColumn(Constant.INFOOPEN_COLUMN_ID);
        return leader;
    }
    /**
     * 条件查询
     * @param
     * @return
     */
    @Transactional
    public static Page selectList(Map<String, String> searchMap) {
        StringBuilder sql = new StringBuilder("select * FROM M_leader l WHERE 1=1");
        String sql1;
        List<Object> args = Lists.newArrayList();
        Page page = new Page();
        if (ObjectUtil.isNotNull(searchMap)) {
            if (ObjectUtil.isNotNull(searchMap.get("del"))) {
                sql.append(" AND l.del=?");
                args.add(searchMap.get("del"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("org_id"))) {
                sql.append(" AND l.org_id=? ");
                args.add(searchMap.get("org_id"));
            }
            if (ObjectUtil.isNotNull(searchMap.get("codeLeader"))) {
                sql.append(" AND (l.code like '001___' or l.code ='001' )");
            }

            if (ObjectUtil.isNotNull(searchMap.get("keywords"))) {//模糊查询所有字段
                String fuzzySense = searchMap.get("keywords").replaceAll("_", "\\\\_");
                fuzzySense = fuzzySense.replaceAll("%", "\\\\%");
                fuzzySense = fuzzySense.replaceAll("'", "");
                sql.append(" and l.name like '%" + fuzzySense + "%'");//领导姓名
            }
        }
        sql.append(" order by l.code ");
        int pageNum = 0;
        int rowsPerPage = 0;
        try {
            pageNum = Integer.parseInt(searchMap.get("pageNum"));
        } catch (Exception e) {
            pageNum = 1;
        }
        try {
            rowsPerPage = Integer.parseInt(searchMap.get("pageSize"));
        } catch (Exception e) {
            rowsPerPage = Constant.DEFAULT_PAGE_SIZE;
        }
        page.pageNum = pageNum;
        page.rowsPerPage = rowsPerPage;
        StringBuffer sqlPage = new StringBuffer();
        sqlPage.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
        sqlPage.append(sql);
        sqlPage.append(") tmp_tb where ROWNUM<=");
        sqlPage.append((pageNum - 1) * rowsPerPage + rowsPerPage);
        sqlPage.append(") where row_id>");
        sqlPage.append((pageNum - 1) * rowsPerPage);
        sql1 = sql.toString();
        sql.delete(0, sql.length());
        sql.append(sqlPage);
        SqlQuery sqlQuery = Ebean.createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> rowList = sqlQuery.findList();
        //查询总行数
        String countStr = sql1.toString();
        countStr = countStr.substring(countStr.indexOf("FROM"));
        countStr = "select count(*) as count  " + countStr;
        countStr = countStr.substring(0, countStr.indexOf("order"));
        SqlQuery sqlQueryCount = Ebean.createSqlQuery(countStr);
        for (int i = 0; i < args.size(); i++) {
            sqlQueryCount.setParameter(i + 1, args.get(i));
        }
        SqlRow sqlRow = sqlQueryCount.findUnique();
        String rowNum = sqlRow.getString("count");

        page.rowsNum = Integer.parseInt(rowNum);
        int maxPage = (page.rowsNum - 1 + page.rowsPerPage) / page.rowsPerPage;
        page.maxPage = maxPage;
        page.list=rowList;
        return page;
    }


    @Override
    public String toString() {
        return "Leader{" +
                "id='" + id + '\'' +
                ", org_id='" + org_id + '\'' +
                ", name='" + name + '\'' +
                ", del='" + del + '\'' +
                ", post='" + post + '\'' +
                ", subordinate='" + subordinate + '\'' +
                ", duty='" + duty + '\'' +
                ", photo='" + photo + '\'' +
                ", updatetime=" + updatetime +
                ", updateuser='" + updateuser + '\'' +
                ", htmlpath='" + htmlpath + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(String subordinate) {
        this.subordinate = subordinate;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getHtmlpath() {
        return htmlpath;
    }

    public void setHtmlpath(String htmlpath) {
        this.htmlpath = htmlpath;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
