package util;

import models.Config;

/**
 * CopyRright (c)2007-2013: 陕西北佳信息技术有限责任公司<br>
 * Project:互联网项目管理系统<br>
 * Class Type:抽象类<br>
 * Comments:常量类<br>
 * JDK version:1.7<br>
 * Namespace:com.dl<br>
 * extends：<br>
 * implements：<br>
 * --------------------------------------------------------------<br>
 * V1.0 创建 houyaohui 2015-11-24 新项目<br>
 * --------------------------------------------------------------<br>
 */
public class Constant {
    /**
     * Description: 私有构造器
     */
    private Constant() {
    }

    private Config config;
    /**
     * 逻辑未删除
     */
    public static final String DEL_NO = "0";
    /**
     * 逻辑已删除
     */
    public static final String DEL_YES = "1";

    /**
     * 栏目类型_图库
     */
    public static final String COLUMN_TYPE_PHOTO = "133643B9709C41F8B2A529C177CD242C";

    /**
     * 模板类型_首页
     */
    public static final String MOBILETYPE_INDEX = "ED7DB8211DAA48BDB9726B61D31E977E";

    /**
     * 模板类型_栏目
     */
    public static final String MOBILETYPE_COLUMN = "1F50FDD1E1E54088BE5A2C3599FF19EA";
    /**
     * 模板类型_内容
     */
    public static final String MOBILETYPE_CONTENT = "9E125707F0F54D278C6E012B61B76338";
    /**
     * 未审核状态
     */
    public static final String AUDIT_NOT = "0";
    /**
     * 审核通过
     */
    public static final String AUDIT_PASS = "1";
    /**
     * 审核未通过
     */
    public static final String AUDIT_NOTPASS = "2";
    /**
     * 定时任务的审核状态
     */
    public static final String AUDIT_TIMER = "3";
    /**
     * 正常状态
     */
    public static final String AUDIT_NORMAL = "0";
    /**
     * 冻结状态
     */
    public static final String AUDIT_FROZEN = "1";
    /**
     * 删除状态
     */
    public static final String AUDIT_DELETE = "2";
    /**
     * 用户初始密码
     */

    public static final String INIT_PASSWORD = "123456";
    /**
     * 内容新增code
     */
    public static final String CONTENT_ADDCODE = "003001001";
    /**
     * 内容修改code
     */
    public static final String CONTENT_UPDATECODE = "003001002";
    /**
     * 内容删除code
     */
    public static final String CONTENT_DELETECODE = "003001003";

    /**
     * 未回复状态
     */
    public static final String AUDIT_UNREPLY= "0";
    /**
     * 已回复状态
     */
    public static final String AUDIT_REPLY= "1";
    //默认每页行数
    public static final int DEFAULT_PAGE_SIZE = 10;
    //留言板默认每页行数
    public static final int DEFAULT_PAGE_SIZE_MessageBoard = 5;
    /**
     * 超级部门标识
     */
    public static final String ISSUPPER_NO = "1";
    /**
     * 默认数据源(oracle)
     */
    public static final String DEFAULT_DATASOURCE = "default";
    /**
     * mysql数据源
     */
    public static final String MYSQL_DATASOURCE = "mysql";
}
