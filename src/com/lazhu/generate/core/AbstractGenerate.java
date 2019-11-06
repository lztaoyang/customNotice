package com.lazhu.generate.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lazhu.generate.util.Resources;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

public abstract class AbstractGenerate
{
    protected static Logger logger = LoggerFactory.getLogger(AbstractGenerate.class);
    
    protected Configuration config;
    
    protected String javaPath;
    
    protected String javaResourcesPath;
    
    protected String testJavaPath;
    
    protected String testJavaResourcesPath;
    
    protected String viewPath;
    
    protected String webappPath;
    
    protected String projectName;
    
    protected String separator;
    
    // 工程文件
    protected File projectFile;
    
    protected Map<String, Object> model;
    
    public AbstractGenerate()
    {
        init();
    }
    
    public void init()
    {
        try
        {
            // 获取文件分隔符
            separator = File.separator;
            
            // 获取工程路径
            projectFile = new File("code");
            String projectPath = Resources.TPL_CLASS_DIR;
            logger.info("Project Path: {}", projectPath);
            
            // 项目名称
            projectName = StringUtils.substring(projectPath.toString(), projectPath.toString().lastIndexOf(separator) + 1);
            logger.info("projectName : {}", projectName);
            
            // Java文件路径
            javaPath = StringUtils.replace(projectPath + "/", "/", separator);
            logger.info("Java Path: {}", javaPath);
            
            // 代码模板配置
            config = new Configuration();
            config.setDefaultEncoding("UTF-8");
            config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
            config.setDateFormat("yyyy-MM-dd");
            config.setTimeFormat("HH:mm:ss");
            config.setNumberFormat("#0.#");
            config.setObjectWrapper(new DefaultObjectWrapper());
            config.setClassForTemplateLoading(this.getClass(), File.separator+"template"+File.separator + Resources.TPL_FILE_DIR);
            
            // 定义模板变量
            model = new HashMap<String, Object>();
            model.put("packageName", Resources.TPL_PACKAGE_NAME);
            model.put("className", Resources.TPL_CLASS_NAME);
            model.put("instanceName", StringUtils.uncapitalize(Resources.TPL_CLASS_NAME));
            
            model.put("pknEntity", Resources.PKN_ENTITY);
            model.put("pknDAO", Resources.PKN_DAO);
            model.put("pknService", Resources.PKN_SERVICE);
            model.put("pknServiceImpl", Resources.PKN_SERVICE_IMPL);
            model.put("pknController", Resources.PKN_CONTROLLER);
            
            model.put("projectName", projectName);
            model.put("author", Resources.AUTHOR);
            model.put("appName", Resources.APP_NAME);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    };
}
