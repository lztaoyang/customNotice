package com.lazhu.generate.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lazhu.generate.vo.DicValue;

/**
 * 各类资源配置
 * 
 */
public class Resources
{
    protected static Logger logger = LoggerFactory.getLogger(Resources.class);
    
    public static String JDBC_DRIVER;
    
    public static String JDBC_URL;
    
    public static String JDBC_USERNAME;
    
    public static String JDBC_PASSWORD;
    
    public static String TPL_TABLE_NAME;
    
    public static String TPL_PACKAGE_NAME;
    
    public static String TPL_CLASS_NAME;
    
    public static String TPL_CLASS_DIR;
    
    // 包名配置
    public static String PKN_ENTITY;
    
    public static String PKN_DAO;
    
    public static String PKN_SERVICE;
    
    public static String PKN_SERVICE_IMPL;
    
    public static String PKN_CONTROLLER;
    
    // 模板文件目录名
    public static String TPL_FILE_DIR;
    
    //系统名
    public static String APP_NAME;
    
    //注释author
    public static String AUTHOR="";
    //字典列表
    public static Map<String,List<DicValue>> DIC_MAP = new HashMap<String,List<DicValue>>();
    
    
    /**
     * 初始化配置
     * 
     * @param tabName 表名
     * @param packName java包路径
     * @param projectSrcDir 代码存放目录
     * @param className 类名
     * @param tplFileDir 模板文件目录名
     * @see [类、类#方法、类#成员]
     */
    public static void init(String driver, String dburl, String username, String password, String tabName, String packName, String projectSrcDir,
        String className, String tplFileDir)
    {
        // JDBC
        JDBC_DRIVER = driver;
        JDBC_URL = dburl;
        JDBC_USERNAME = username;
        JDBC_PASSWORD = password;
        
        // TPL模板参数
        TPL_TABLE_NAME = tabName;
        TPL_PACKAGE_NAME = packName;
        TPL_CLASS_DIR = projectSrcDir;
        TPL_CLASS_NAME = className;
        TPL_FILE_DIR = tplFileDir;
        if (StringUtils.isBlank(TPL_TABLE_NAME) || StringUtils.isBlank(TPL_PACKAGE_NAME) || StringUtils.isBlank(TPL_CLASS_DIR)
            || StringUtils.isBlank(TPL_CLASS_NAME))
        {
            logger.error("参数设置错误：表名、包名、模块名、类名不能为空。");
            System.exit(-1);
        }
        PKN_ENTITY = TPL_PACKAGE_NAME + "."+className.toLowerCase()+".entity";
        PKN_DAO = TPL_PACKAGE_NAME  + "."+className.toLowerCase()+ ".mapper";
        PKN_SERVICE = TPL_PACKAGE_NAME  + "."+className.toLowerCase()+ ".service";
        PKN_SERVICE_IMPL = TPL_PACKAGE_NAME  + "."+className.toLowerCase()+ ".service.impl";
        PKN_CONTROLLER = TPL_PACKAGE_NAME  + "."+className.toLowerCase()+ ".web";
        APP_NAME = StringUtils.substringAfterLast(TPL_PACKAGE_NAME, ".");
    }
    
}
