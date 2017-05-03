package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by yangshaofeng on 2016/3/23.
 */
@Entity
@Table(name = "M_BusinessException")
public class BusinessException {
    @Id
    public String id;
    public String name;
    //文章ID
    public String content_id;
    public Date updatetime;
    //是否成功
    public String is_success;

    /**
     * 添加
     *
     * @param
     * @return
     */
    @Transactional
    public static void insert(BusinessException businessException,String dataSourceType) {
        Ebean.getServer(dataSourceType).save(businessException);
    }
}
