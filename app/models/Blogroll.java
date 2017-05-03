package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.Transactional;
import com.google.common.collect.Lists;
import play.db.ebean.Model;
import util.Constant;
import util.ObjectUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

/**
 * Created by zuopanpan on 2016/2/26.
 */
@Entity
@Table(name="E_Blogroll")
public class Blogroll extends Model {
    @Id
    public String id;
    /** 类型id*/
    public String type_id;
    /** 链接名称*/
    public String name;
    /** code*/
    public String code;
    /** 链接地址*/
    public String address;
    /** 是否删除 0:否  1：是*/
    public String del;
    /** 更新人*/
    public String updateuser;
    /** 更新时间*/
    public Date updatetime;
    /** 站点id*/
    public String station_id;

    /**
     * 条件查询
     * @param
     * @return
     */
    @Transactional
    public static List<Blogroll> selectList(Map<String, Object> map,String dataSourceType) {
        List<Blogroll> list = Ebean.getServer(dataSourceType).find(Blogroll.class).where().allEq(map).findList();
        return list;
    }
    /**
     * 条件查询
     * @param
     * @return
     */
    @Transactional
    public static List<SqlRow> selectSqlRowList(Map<String, String> searchMap,String dataSourceType) {
        StringBuilder sql=new StringBuilder("select * from E_Blogroll where 1=1 ");
        List<Object> args = Lists.newArrayList();
        if(ObjectUtil.isNotNull(searchMap)){
            if(ObjectUtil.isNotNull(searchMap.get("type_id"))){
                sql.append("and type_id=? ");
                args.add(searchMap.get("type_id"));
            }
            if(ObjectUtil.isNotNull(searchMap.get("del"))){
                sql.append("and del=? ");
                args.add(searchMap.get("del"));
            }
        }
        sql.append("order by code ");
        SqlQuery sqlQuery=Ebean.getServer(dataSourceType).createSqlQuery(sql.toString());
        for (int i = 0; i < args.size(); i++) {
            sqlQuery.setParameter(i + 1, args.get(i));
        }
        List<SqlRow> list = sqlQuery.findList();
        return list;
    }
    /**
     * 添加
     * @param
     * @return
     */
    @Transactional
    public static Blogroll insert(Blogroll blogroll,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(blogroll);
        return blogroll;
    }
    /**
     * 更新
     * @param
     * @return
     */
    @Transactional
    public static Blogroll update(Blogroll blogroll,String dataSourceType) {
        Ebean.getServer(dataSourceType).update(blogroll);
        return blogroll;
    }
    /**
     * 根据id获取对象
     * @param
     * @return
     */
    @Transactional
    public static Blogroll getObject(String id,String dataSourceType) {
        return Ebean.getServer(dataSourceType).find(Blogroll.class).where().idEq(id).findUnique();
    }
    /**
     * 查询最大code
     */
    @Transactional
    private static String searchCodeMax(String sql,String dataSourceType) {
        SqlQuery sqlQuery = Ebean.getServer(dataSourceType).createSqlQuery(sql);
        SqlRow sqlRow = sqlQuery.findUnique();
        return sqlRow.getString("code");
    }
    /**
     * Description: 生成code
     *
     * @param parentId
     * @return
     */
    public static String getCodeVal(String parentId,String dataSourceType){
        String parentCode="";
        StringBuilder sql=new StringBuilder("select MAX(code) code from E_Blogroll where del=0");
        if(ObjectUtil.isNull(parentId)){
            sql.append(" and LENGTH(code)=3");
        }else if ( !"0".equals(parentId)) {
            Blogroll blogroll = getObject(parentId,dataSourceType);
            parentCode = blogroll.code;
            sql.append(" and tier_code like'" + parentCode + "___'");
        }
        // 获取当前最大code
        String codeMax = searchCodeMax(sql.toString(),dataSourceType);
        if (codeMax != null) {
            if ((parentCode != null) && (!("".equals(parentCode)))) {
                String last3 = codeMax.substring(codeMax.length() - 3,
                        codeMax.length());
                String dcode = Integer.parseInt(last3) + 1 + "";
                switch (dcode.length()) {
                    case 1:
                        return parentCode + "00" + dcode;
                    case 2:
                        return parentCode + "0" + dcode;
                }
                return parentCode + dcode;
            }
            String dcode = Integer.parseInt(codeMax) + 1 + "";
            switch (dcode.length()) {
                case 1:
                    return "00" + dcode;
                case 2:
                    return "0" + dcode;
            }
            return dcode;
        }
        if ((parentCode != null) && (!("".equals(parentCode)))) {
            return parentCode + "001";
        }
        return "001";
    }
    /**
     * Description: 前台查询
     *
     * @param
     * @return
     */
    @Transactional
    public static List<Object> selectListWeb(String dataSourceType) {
        List<Object> result=new ArrayList<>();
        //查询类型
        List<BlogrollType> typeList=Ebean.getServer(dataSourceType).find(BlogrollType.class).where().eq("del", Constant.DEL_NO).order("priority").findList();

        for (int i=0;i<typeList.size();i++){
            List<Object> blogrollList=new ArrayList<>();
            BlogrollType type=typeList.get(i);
            Map<String,String> searchMap=new HashMap<>();
            searchMap.put("type_id", type.id);
            searchMap.put("del", Constant.DEL_NO);
            blogrollList.add(type);
            blogrollList.add(selectSqlRowList(searchMap,dataSourceType));
          /*  if(ObjectUtil.isNotNull(type.priority)){
                result.put("list" + type.priority, blogrollList);
            }else{
                result.put("list" + (i+1), blogrollList);
            }*/
            result.add(blogrollList);
        }
        return result;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
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


}
