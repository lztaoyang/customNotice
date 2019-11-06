package com.lazhu.generate;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.lazhu.generate.core.GenerateFactory;
import com.lazhu.generate.db.Mysql;
import com.lazhu.generate.util.Resources;
import com.lazhu.generate.vo.Table;

/**
 * 
 * 命令行交互方式代码生成器
 * 
 * @author 00fly
 * @version [版本号, 2016-12-10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommandRun
{
    /**
     * 测试
     * 
     * @param args
     * @throws Exception
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws Exception
    {
        String input;
        Scanner sc = new Scanner(System.in);
        do
        {
            generateCode("java");
            System.out.println("------------输入q退出,输入其他值继续生成代码------------");
            input = StringUtils.trimToEmpty(sc.nextLine());
        } while (!input.equalsIgnoreCase("q"));
        System.out.println("----------系统退出成功----------");
        sc.close();
    }
    
    /**
     * generateCode
     * 
     * @param tplFileDir 模板文件目录名
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public static void generateCode(String tplFileDir)
        throws Exception
    {
        String tableName = null;
        String packName = null;
        String className = null;
        
        ResourceBundle config = ResourceBundle.getBundle("jdbc");
        String driver = config.getString("jdbc.driver");
        String dburl = config.getString("jdbc.url");
        String username = config.getString("jdbc.username");
        String password = config.getString("jdbc.password");
        
        // JDBC
        Resources.JDBC_DRIVER = driver;
        Resources.JDBC_URL = dburl;
        Resources.JDBC_USERNAME = username;
        Resources.JDBC_PASSWORD = password;
        
        // 取得数据库表
        List<Table> tables = new Mysql().getTables();
        Scanner sc = new Scanner(System.in);
        boolean success = false;
        List<String> tableToSelect = new ArrayList<String>();
        do
        {
            if (tableToSelect.isEmpty())
            {
                for (Table table : tables)
                {
                    tableToSelect.add(table.getTableName());
                }
            }
            // 显示表名供选择
            int index = 1;
            for (String it : tableToSelect)
            {
                System.out.println(index + ": " + it);
                index++;
            }
            System.out.println("请输入数据库表名过滤后按序号选择数据库表");
            String input = StringUtils.trimToEmpty(sc.nextLine());
            if (StringUtils.isNumeric(input))
            {
                int selectIndex = NumberUtils.toInt(input);
                if (selectIndex > 0 && selectIndex <= tableToSelect.size())
                {
                    tableName = tableToSelect.get(selectIndex - 1);
                    success = true;
                }
            }
            tableToSelect.clear();
            if (!success)
            {
                for (Table table : tables)
                {
                    if (table.getTableName().equals(input))
                    {
                        tableName = input;
                        success = true;
                    }
                    else if (table.getTableName().contains(input))
                    {
                        tableToSelect.add(table.getTableName());
                    }
                }
            }
        } while (!success);
        System.out.println("你选择了数据库表：" + tableName);
        System.out.println();
        
        // 取java包路径
        do
        {
            System.out.print("请输入java包路径,回车使用默认值com.fly.demo：");
            packName = StringUtils.defaultIfEmpty(sc.nextLine(), "com.fly.demo");
        } while (StringUtils.isEmpty(packName));
        System.out.println("你选择了java包路径：" + packName);
        System.out.println();
        
        // 取类名称
        do
        {
            String defaultclass = camelCase(tableName, true);
            System.out.print(String.format("请输入类名,回车使用默认值%s：", defaultclass));
            className = StringUtils.defaultIfEmpty(sc.nextLine(), defaultclass);
            className = camelCase(className, true);
        } while (StringUtils.isEmpty(className));
        System.out.println("你选择了类名：" + className);
        
        System.out.println();
        System.out.println("请确认以下信息：");
        System.out.println("1.数据库表" + tableName);
        System.out.println("2.包路径：" + packName);
        System.out.println("3.类名：" + className);
        System.out.println("回车继续,其他取消");
        if (StringUtils.isEmpty(sc.nextLine()))
        {
            // 代码生成
            String srcDir = new File("code").getAbsolutePath();
            try
            {
                // 清空目录下的文件
                File srcFile = new File(srcDir);
                if (srcFile.exists())
                {
                    FileUtils.cleanDirectory(srcFile);
                }
            }
            catch (IOException e)
            {
            }
            
            System.out.println("------------开始生成代码------------");
            Resources.init(driver, dburl, username, password, tableName, packName, srcDir, className, tplFileDir);
            GenerateFactory factory = new GenerateFactory();
            factory.genJavaTemplate();
            System.out.println("------------生成代码结束------------");
        }
        else
        {
            System.out.println("------------你成功取消代码生成------------");
        }
    }
    
    /**
     * 驼峰命名
     * 
     * @param input
     * @param firstCharacterUppercase
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static String camelCase(String input, boolean firstCharacterUppercase)
    {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            switch (c)
            {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                    if (sb.length() > 0)
                    {
                        nextUpperCase = true;
                    }
                    break;
                default:
                    if (nextUpperCase)
                    {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    }
                    else
                    {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }
        if (firstCharacterUppercase)
        {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        return sb.toString();
    }
    
}