package models;

import com.avaje.ebean.SqlRow;

import java.util.List;

/**
 * 分页
 */
public class Page {
    /**
     * 总行数
     */
    public int rowsNum;
    /**
     * 每页行数
     */
    public int rowsPerPage;
    /**
     * 最大页码数
     */
    public int maxPage;
    /**
     * 当    前页数
    */
    public int pageNum;
    /**
     * 分页数据
     */
    public List<SqlRow> list;
    /**
     * 分页数据
     */
    public List<Object> listObj;
}
