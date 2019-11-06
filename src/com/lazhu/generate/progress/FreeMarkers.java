package com.lazhu.generate.progress;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * FreeMarkers
 * 
 * @author naxj
 */
public class FreeMarkers
{
    private static Configuration config;
    
    static
    {
        config = new Configuration();
        config.setClassForTemplateLoading(FreeMarkers.class, "/template/");
        config.setDefaultEncoding("UTF-8");
    }
    
    /**
     * 获取模板填充model解析后的内容
     * 
     * @param template
     * @param model
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static String renderTemplate(Template template, Object model)
    {
        try
        {
            StringWriter result = new StringWriter();
            template.process(model, result);
            return result.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取IbatorConfig模板填充model后的内容
     * 
     * @param model
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String renderIbatorConfigTemplate(Map<String, Object> model)
    {
        try
        {
            Template template = config.getTemplate("config_ibator.xml.ftl");
            return renderTemplate(template, model);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取模板填充model解析后的内容
     * 
     * @param model
     * @param ftlName
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String renderTemplate(Map<String, Object> model, String ftlName)
    {
        try
        {
            Template template = config.getTemplate(ftlName);
            String result = FreeMarkers.renderTemplate(template, model);
            return result;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}