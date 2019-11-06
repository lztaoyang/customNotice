package com.lazhu.generate.util;

import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkers
{
    private static Configuration configuration = new Configuration();
    
    static
    {
        configuration.setDefaultEncoding("UTF-8");
        configuration.setDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    public static String renderTemplate(Template template, Object model)
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
}