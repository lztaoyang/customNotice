package com.lazhu.generate.core;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.lazhu.generate.util.FreeMarkers;
import com.lazhu.generate.util.Resources;
import com.lazhu.generate.vo.Column;
import com.lazhu.generate.vo.Table;

import freemarker.template.Template;

public class GenerateCode extends AbstractGenerate implements Generate
{
    
    public GenerateCode()
    {
        super();
    }
    
    @Override
    public void generate(Table table)
    {
        try
        {
            // 特殊类型处理
            handleSpecial(table.getColumns());
            model.put("tableName", table.getTableName().toLowerCase());
            model.put("tableComment", table.getTableComment());
            System.err.println(table.getTableComment());
            model.put("columns", table.getColumns());
            model.put("pk", table.getPk());
            model.put("date", new Date());
            model.put("Integer_MAX_VALUE", Integer.MAX_VALUE);
            model.put("Long_MAX_VALUE", Long.MAX_VALUE);
            model.put("Float_MAX_VALUE", Float.MAX_VALUE);
            model.put("Double_MAX_VALUE", Double.MAX_VALUE);
            
            Map<String, String> map = new HashMap<String, String>();
            String className = (String)model.get("className");
            String instanceName = (String)model.get("instanceName");
            if ("all".equals(Resources.TPL_FILE_DIR))
            {
                // ftl映射java类
                map.put("web_controllerRest.java.ftl", className + "Controller.java");
                map.put("service_serviceImpl.java.ftl", className + "Service.java");
                
                map.put("mapper_mapper.java.ftl", className + "Mapper.java");
                map.put("mapper_xml_mapper.xml.ftl", className + "Mapper.xml");
                map.put("entity_model.java.ftl", className + ".java");
                map.put("html_rest.html.ftl", "index.html");
                map.put("html_rest.js.ftl", instanceName+"_index.js");
            }
            else if ("html".equals(Resources.TPL_FILE_DIR))
            {
                
                
            }
            for (String key : map.keySet())
            {
                // 将包名映射为路径、ftl中_转换为类路径
                String dirPath =
                    new File(javaPath + (Resources.TPL_PACKAGE_NAME+"."+className.toLowerCase()).replaceAll("\\.", Matcher.quoteReplacement(File.separator))) + separator
                        + StringUtils.substringBeforeLast(key, "_").replace("_", separator) + separator;
                String filePath = dirPath + map.get(key);
                File file = new File(filePath);
                // 文件不存在或者文件为1分钟之前生成的(保证此次生成的公共类文件不会重复生成)
                if (!file.exists() || System.currentTimeMillis() - file.lastModified() > 60 * 1000L)
                {
                    Template template = config.getTemplate(key);
                    String content = FreeMarkers.renderTemplate(template, model);
                    FileUtils.writeStringToFile(file, content, "UTF-8");
                    logger.info("filePath: {}", filePath);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void generate(List<Table> tables)
        throws Exception
    {
    }
    
    /**
     * 特殊类型处理
     * 
     * @param columns
     */
    private void handleSpecial(List<Column> columns)
    {
        boolean hasDate = false;
        boolean hasBigDecimal = false;
        for (Column column : columns)
        {
            if (column.getJavaType().equals("Date"))
            {
                hasDate = true;
            }
            else if (column.getJavaType().equals("BigDecimal"))
            {
                hasBigDecimal = true;
            }
        }
        model.put("hasDate", hasDate);
        model.put("hasBigDecimal", hasBigDecimal);
    }
}
